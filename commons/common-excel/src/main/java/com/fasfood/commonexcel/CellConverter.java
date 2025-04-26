package com.fasfood.commonexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class CellConverter {
    public static Function<Cell, String> stringConverter() {
        return cell -> {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else {
                return "";
            }
        };
    }

    public static Function<Cell, UUID> uuidConverter() {
        return cell -> {
            if (cell == null) return null;
            try {
                String value;
                if (cell.getCellType() == CellType.STRING) {
                    value = cell.getStringCellValue().trim();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    // In case UUID is stored as a number (not common, but for completeness)
                    value = String.valueOf((long) cell.getNumericCellValue()); // risky!
                } else {
                    return null;
                }
                return UUID.fromString(value);
            } catch (IllegalArgumentException e) {
                // Log error if needed
                return null;
            }
        };
    }

    public static Function<Cell, Double> doubleConverter() {
        return cell -> {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        };
    }

    public static Function<Cell, Integer> intConverter() {
        return cell -> {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        };
    }

    public static Function<Cell, Long> longConverter() {
        return cell -> {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC) {
                return (long) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return Long.parseLong(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        };
    }

    public static Function<Cell, Date> dateConverter() {
        return cell -> {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else {
                return null;
            }
        };
    }

    public static Function<Cell, Instant> instantConverter() {
        return cell -> {
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    LocalDate localDate = LocalDate.parse(cell.getStringCellValue().trim());
                    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                } catch (DateTimeParseException e) {
                    // Log nếu cần
                    return null;
                }
            }

            return null;
        };
    }

    public static Function<Cell, LocalDate> localDateConverter() {
        return cell -> {
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                try {
                    return LocalDate.parse(cell.getStringCellValue().trim());
                } catch (DateTimeParseException e) {
                    // Log nếu cần
                    return null;
                }
            }

            return null;
        };
    }

    public static Function<Cell, LocalTime> localTimeConverter() {
        return cell -> {
            if (cell == null) return null;

            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            }

            if (cell.getCellType() == CellType.STRING) {
                try {
                    String timeStr = cell.getStringCellValue().trim();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                    return LocalTime.parse(timeStr, formatter);
                } catch (Exception e) {
                    return null; // or log warning if needed
                }
            }

            return null;
        };
    }

    public static Function<Cell, Boolean> booleanConverter() {
        return cell -> {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().toLowerCase();
                return "true".equals(value) || "yes".equals(value) || "1".equals(value);
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue() != 0;
            } else {
                return null;
            }
        };
    }

    // NEW: Enum converter
    public static <E extends Enum<E>> Function<Cell, E> enumConverter(Class<E> enumClass) {
        return cell -> {
            if (cell == null || cell.getCellType() != CellType.STRING) return null;
            try {
                return Enum.valueOf(enumClass, cell.getStringCellValue().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    // NEW: List<String> converter (split by delimiter)
    public static Function<Cell, List<String>> listStringConverter(String delimiter) {
        return cell -> {
            if (cell == null || cell.getCellType() != CellType.STRING) return Collections.emptyList();
            String value = cell.getStringCellValue();
            if (value == null || value.trim().isEmpty()) return Collections.emptyList();
            return Arrays.stream(value.split(delimiter))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        };
    }
}
