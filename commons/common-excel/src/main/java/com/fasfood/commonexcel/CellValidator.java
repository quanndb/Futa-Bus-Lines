package com.fasfood.commonexcel;

public class CellValidator {
    public static <T> ValidationRule<T> notNull() {
        return new ValidationRule<T>() {
            @Override
            public boolean isValid(T value) {
                return value != null;
            }

            @Override
            public String getErrorMessage() {
                return "Value cannot be null";
            }
        };
    }

    public static ValidationRule<String> minLength(int length) {
        return new ValidationRule<String>() {
            @Override
            public boolean isValid(String value) {
                return value == null || value.length() >= length;
            }

            @Override
            public String getErrorMessage() {
                return "Value must be at least " + length + " characters";
            }
        };
    }

    public static ValidationRule<String> maxLength(int length) {
        return new ValidationRule<String>() {
            @Override
            public boolean isValid(String value) {
                return value == null || value.length() <= length;
            }

            @Override
            public String getErrorMessage() {
                return "Value must be at most " + length + " characters";
            }
        };
    }

    public static ValidationRule<String> pattern(String regex) {
        return new ValidationRule<String>() {
            @Override
            public boolean isValid(String value) {
                return value == null || value.matches(regex);
            }

            @Override
            public String getErrorMessage() {
                return "Value does not match pattern";
            }
        };
    }

    public static ValidationRule<Number> min(Number min) {
        return new ValidationRule<Number>() {
            @Override
            public boolean isValid(Number value) {
                return value == null || value.doubleValue() >= min.doubleValue();
            }

            @Override
            public String getErrorMessage() {
                return "Value must be at least " + min;
            }
        };
    }

    public static ValidationRule<Number> max(Number max) {
        return new ValidationRule<Number>() {
            @Override
            public boolean isValid(Number value) {
                return value == null || value.doubleValue() <= max.doubleValue();
            }

            @Override
            public String getErrorMessage() {
                return "Value must be at most " + max;
            }
        };
    }

    public static ValidationRule<String> email() {
        return new ValidationRule<String>() {
            @Override
            public boolean isValid(String value) {
                if (value == null || value.trim().isEmpty()) {
                    return true;
                }
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                if (value.length() > 254) {
                    return false;
                }
                String[] parts = value.split("@");
                if (parts.length != 2) {
                    return false;
                }
                String localPart = parts[0];
                String domainPart = parts[1];
                if (localPart.length() > 64) {
                    return false;
                }
                if (domainPart.length() > 253) {
                    return false;
                }
                return value.matches(emailRegex);
            }

            @Override
            public String getErrorMessage() {
                return "Email không đúng định dạng";
            }
        };
    }
}
