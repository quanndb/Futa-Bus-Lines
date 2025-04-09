package com.fasfood.commonexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Date;
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
}
