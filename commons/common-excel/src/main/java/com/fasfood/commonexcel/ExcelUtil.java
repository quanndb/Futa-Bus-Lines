package com.fasfood.commonexcel;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for reading and writing Excel files with data validation
 */
public class ExcelUtil {

    /**
     * Class representing an Excel column with validation
     */
    public static class ExcelColumn<T> {
        private final String header;
        private final Function<Cell, T> converter;
        private final List<ValidationRule<T>> validationRules;
        private final String fieldName;
        private final BiConsumer<Object, T> setter;
        private final Function<Object, T> getter;

        public ExcelColumn(String header, Function<Cell, T> converter,
                           String fieldName, BiConsumer<Object, T> setter, Function<Object, T> getter) {
            this.header = header;
            this.converter = converter;
            this.validationRules = new ArrayList<>();
            this.fieldName = fieldName;
            this.setter = setter;
            this.getter = getter;
        }

        public ExcelColumn<T> addValidationRule(ValidationRule<T> rule) {
            validationRules.add(rule);
            return this;
        }

        public T convertCell(Cell cell) {
            if (cell == null) {
                return null;
            }
            return converter.apply(cell);
        }

        public List<String> validate(T value) {
            return validationRules.stream()
                    .filter(rule -> !rule.isValid(value))
                    .map(ValidationRule::getErrorMessage)
                    .collect(Collectors.toList());
        }

        public void setValue(Object obj, T value) {
            if (setter != null) {
                setter.accept(obj, value);
            }
        }

        public String getHeader() {
            return header;
        }

        public T getValue(Object obj) {
            if (getter != null) {
                return getter.apply(obj);
            }
            return null;
        }
    }

    /**
     * Class representing a validation error
     */
    @Getter
    public static class ValidationError {
        private final int rowNum;
        private final String columnName;
        private final String message;

        public ValidationError(int rowNum, String columnName, String message) {
            this.rowNum = rowNum;
            this.columnName = columnName;
            this.message = message;
        }

        @Override
        public String toString() {
            return "Row " + (rowNum + 1) + ", Column '" + columnName + "': " + message;
        }
    }

    /**
     * Class to hold import result with data and errors
     */
    public static class ImportResult<T> {
        private final List<T> validData;
        private final List<ValidationError> errors;

        public ImportResult(List<T> validData, List<ValidationError> errors) {
            this.validData = validData;
            this.errors = errors;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public List<T> getValidData() {
            return validData;
        }

        public List<ValidationError> getErrors() {
            return errors;
        }
    }

    /**
     * Builder for configuring Excel import/export
     */
    public static class ExcelMapper<T> {
        private final List<ExcelColumn<?>> columns = new ArrayList<>();
        private Function<Map<String, Object>, T> mapToObject;

        public ExcelMapper(Class<T> targetClass) {
            // Default mapper tries to use reflection
            this.mapToObject = map -> {
                try {
                    T instance = targetClass.getDeclaredConstructor().newInstance();
                    for (ExcelColumn<?> column : columns) {
                        try {
                            Field field = findField(targetClass, column.fieldName);
                            if (field != null) {
                                field.setAccessible(true);
                                field.set(instance, map.get(column.header));
                            }
                        } catch (Exception e) {
                            // Skip if field not found or can't be set
                        }
                    }
                    return instance;
                } catch (Exception e) {
                    throw new RuntimeException("Error creating instance: " + e.getMessage(), e);
                }
            };
        }

        private Field findField(Class<?> cls, String fieldName) {
            if (cls == null || Object.class.equals(cls)) {
                return null;
            }
            try {
                return cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                return findField(cls.getSuperclass(), fieldName);
            }
        }

        public <V> ExcelMapper<T> addColumn(String header, Function<Cell, V> converter,
                                            String fieldName, BiConsumer<T, V> setter, Function<T, V> getter) {
            @SuppressWarnings("unchecked")
            BiConsumer<Object, V> setterWrapper = (obj, val) -> setter.accept((T) obj, val);
            @SuppressWarnings("unchecked")
            Function<Object, V> getterWrapper = obj -> getter.apply((T) obj);

            columns.add(new ExcelColumn<>(header, converter, fieldName, setterWrapper, getterWrapper));
            return this;
        }

        public <V> ExcelColumn<V> column(String header) {
            @SuppressWarnings("unchecked")
            ExcelColumn<V> column = (ExcelColumn<V>) columns.stream()
                    .filter(c -> c.getHeader().equals(header))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Column not found: " + header));
            return column;
        }

        public ExcelMapper<T> withCustomMapper(Function<Map<String, Object>, T> mapToObject) {
            this.mapToObject = mapToObject;
            return this;
        }

        public ImportResult<T> importFromFile(MultipartFile file, String sheetName, boolean hasHeaderRow) {
            Map<String, Object> result = readExcelFromMultipartFile(file, sheetName, columns, hasHeaderRow);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("data");
            @SuppressWarnings("unchecked")
            List<ValidationError> errors = (List<ValidationError>) result.get("errors");

            List<T> objectList = dataList.stream()
                    .map(mapToObject)
                    .collect(Collectors.toList());

            return new ImportResult<>(objectList, errors);
        }

        /**
         * Export list of objects to a file on disk
         */
        public void exportToFile(String filePath, String sheetName, List<T> data) {
            List<Map<String, Object>> rowsData = data.stream()
                    .map(item -> {
                        Map<String, Object> rowData = new HashMap<>();
                        for (ExcelColumn<?> column : columns) {
                            @SuppressWarnings("unchecked")
                            ExcelColumn<Object> objectColumn = (ExcelColumn<Object>) column;
                            rowData.put(column.getHeader(), objectColumn.getValue(item));
                        }
                        return rowData;
                    })
                    .collect(Collectors.toList());

            writeExcel(filePath, sheetName, columns, rowsData);
        }

        /**
         * Export list of objects to byte array
         */
        public byte[] exportToBytes(String sheetName, List<T> data) {
            List<Map<String, Object>> rowsData = data.stream()
                    .map(item -> {
                        Map<String, Object> rowData = new HashMap<>();
                        for (ExcelColumn<?> column : columns) {
                            @SuppressWarnings("unchecked")
                            ExcelColumn<Object> objectColumn = (ExcelColumn<Object>) column;
                            rowData.put(column.getHeader(), objectColumn.getValue(item));
                        }
                        return rowData;
                    })
                    .collect(Collectors.toList());

            return writeExcelToBytes(sheetName, columns, rowsData);
        }
    }

    /**
     * Create a new mapper for the specified class
     */
    public static <T> ExcelMapper<T> mapperFor(Class<T> targetClass) {
        return new ExcelMapper<>(targetClass);
    }

    /**
     * Read data from Excel file with validation
     */
    public static <T> Map<String, Object> readExcelFromMultipartFile(MultipartFile file, String sheetName,
                                                                     List<ExcelColumn<?>> columns, boolean hasHeaderRow) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = ExcelHelper.getWorkbookFromInputStream(Objects.requireNonNull(file.getOriginalFilename()), is)) {

            Sheet sheet = (sheetName != null) ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            // Create column index map
            Map<String, Integer> columnIndexMap = new HashMap<>();
            if (hasHeaderRow) {
                Row headerRow = sheet.getRow(0);
                if (headerRow != null) {
                    for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                        Cell cell = headerRow.getCell(i);
                        if (cell != null) {
                            String header = cell.getStringCellValue();
                            columnIndexMap.put(header, i);
                        }
                    }
                }
            } else {
                // If no header row, assume columns are in order
                for (int i = 0; i < columns.size(); i++) {
                    columnIndexMap.put(columns.get(i).getHeader(), i);
                }
            }

            // Process data rows
            int startRow = hasHeaderRow ? 1 : 0;
            for (int rowNum = startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                Map<String, Object> rowData = new HashMap<>();
                boolean rowHasData = false;

                for (ExcelColumn<?> column : columns) {
                    String header = column.getHeader();
                    Integer columnIndex = columnIndexMap.get(header);

                    if (columnIndex == null) {
                        errors.add(new ValidationError(rowNum, header, "Column not found"));
                        continue;
                    }

                    Cell cell = row.getCell(columnIndex);
                    Object value = column.convertCell(cell);

                    if (value != null) {
                        rowHasData = true;
                    }

                    // Validate cell value
                    List<String> validationErrors = ExcelHelper.validateCell(column, value);
                    for (String errorMsg : validationErrors) {
                        errors.add(new ValidationError(rowNum, header, errorMsg));
                    }

                    rowData.put(header, value);
                }

                if (rowHasData) {
                    dataList.add(rowData);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", dataList);
        result.put("errors", errors);
        return result;
    }

    /**
     * Write data to Excel file
     */
    public static void writeExcel(String filePath, String sheetName,
                                  List<ExcelColumn<?>> columns, List<Map<String, Object>> data) {
        try (Workbook workbook = ExcelHelper.getWorkbook(filePath, null)) {
            createExcelWorkbook(workbook, sheetName, columns, data);

            // Write the workbook to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Write data to Excel and return as byte array
     */
    public static byte[] writeExcelToBytes(String sheetName, List<ExcelColumn<?>> columns,
                                           List<Map<String, Object>> data) {
        try (Workbook workbook = new XSSFWorkbook()) { // Default to XLSX format for byte array export
            createExcelWorkbook(workbook, sheetName, columns, data);

            // Write workbook to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error creating Excel byte array: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to create workbook content
     */
    private static void createExcelWorkbook(Workbook workbook, String sheetName,
                                            List<ExcelColumn<?>> columns, List<Map<String, Object>> data) {
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i).getHeader());
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        for (int rowNum = 0; rowNum < data.size(); rowNum++) {
            Row row = sheet.createRow(rowNum + 1);
            Map<String, Object> rowData = data.get(rowNum);

            for (int colNum = 0; colNum < columns.size(); colNum++) {
                String header = columns.get(colNum).getHeader();
                Object value = rowData.get(header);
                Cell cell = row.createCell(colNum);
                ExcelHelper.setCellValue(cell, value);
            }
        }

        // Auto-size columns
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }
}