package com.fasfood.commonexcel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExcelHelper {
    /**
     * Kiểm tra xem file có phải là file Excel hay không
     */
    public static boolean isExcelFile(String fileName) {
        return fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"));
    }

    /**
     * Kiểm tra xem MultipartFile có phải là file Excel hay không
     */
    public static boolean isExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return isExcelFile(fileName);
    }

    public static Workbook getWorkbookFromInputStream(String fileName, InputStream is) throws IOException {
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        } else if (fileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else {
            throw new IllegalArgumentException("Not an Excel file: " + fileName);
        }
    }

    public static Workbook getWorkbook(String filePath, InputStream is) throws IOException {
        if (is == null) {
            // Creating new workbook
            if (filePath.endsWith(".xlsx")) {
                return new XSSFWorkbook();
            } else {
                return new HSSFWorkbook();
            }
        } else {
            // Reading existing workbook
            if (filePath.endsWith(".xlsx")) {
                return new XSSFWorkbook(is);
            } else {
                return new HSSFWorkbook(is);
            }
        }
    }

    public static void setCellValue(Cell cell, Object value) {
        switch (value) {
            case null -> cell.setCellValue("");
            case String s -> cell.setCellValue(s);
            case Double v -> cell.setCellValue(v);
            case Integer i -> cell.setCellValue(i);
            case Long l -> cell.setCellValue(l);
            case Boolean b -> cell.setCellValue(b);
            case Date date -> cell.setCellValue(date);
            default -> cell.setCellValue(value.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<String> validateCell(ExcelUtil.ExcelColumn<T> column, Object value) {
        try {
            return column.validate((T) value);
        } catch (ClassCastException e) {
            return Collections.singletonList("Invalid type");
        }
    }
}
