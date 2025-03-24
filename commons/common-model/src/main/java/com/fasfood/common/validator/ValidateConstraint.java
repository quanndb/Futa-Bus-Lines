package com.fasfood.common.validator;//

public interface ValidateConstraint {
    String UTILITY_CLASS = "Utility class";

    public static final class LENGTH {
        public static final int CODE_MAX_LENGTH = 50;
        public static final int GL_CODE_MAX_LENGTH = 50;
        public static final int NAME_MAX_LENGTH = 100;
        public static final int TITLE_MAX_LENGTH = 200;
        public static final int DESC_MAX_LENGTH = 1000;
        public static final int NOTE_MAX_LENGTH = 1000;
        public static final int ENUM_MAX_LENGTH = 20;
        public static final int ID_MIN_LENGTH = 1;
        public static final int ID_MAX_LENGTH = 36;
        public static final int PASSWORD_MIN_LENGTH = 3;
        public static final int PASSWORD_MAX_LENGTH = 50;
        public static final int CONTENT_MAX_LENGTH = 2000;
        public static final int VALUE_MAX_LENGTH = 200;
        public static final int PHONE_MAX_LENGTH = 20;
        public static final int EMAIL_MAX_LENGTH = 50;
        public static final int ADDRESS_MAX_LENGTH = 200;
        public static final int ACCOUNT_NUMBER_MAX_LENGTH = 20;
        public static final int COMPANY_NAME = 200;
        public static final int ISSUED_UNIT = 200;
        public static final int POSITION = 100;
        public static final int COMMISSIONER = 100;
        public static final int WEBSITE = 200;
        public static final int IDENTIFY_CARD = 50;
        public static final int BUILDING_NAME_MAX_LENGTH = 200;
        public static final int INVOICE_ISSUING_EMAIL_MAX_LENGTH = 150;
        public static final int KEYWORD_MAX_LENGTH = 200;
        public static final int FILE_NAME = 300;
        public static final int BUSINESS_PLAN_NUMBER_MAX_LENGTH = 100;
        public static final int QUESTION_MAX_LENGTH = 1000;
        public static final int ANSWER_MAX_LENGTH = 1000;

        private LENGTH() {
            throw new IllegalStateException("Utility class");
        }
    }

    public static final class VALUE {
        public static final int FLOOR_NUMBER_MAX = 200;
        public static final int BASEMENT_NUMBER_MAX = 20;
        public static final int BUILDING_AREA_MAX = 1000000000;
        public static final int INVOICE_ISSUING_EMAIL_MAX = 3;

        private VALUE() {
            throw new IllegalStateException("Utility class");
        }
    }

    public static final class FORMAT {
        public static final String PHONE_NUMBER_PATTERN = "^(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?([0-9][0-9\\- \\.]+[0-9])$";
        public static final String EMAIL_PATTERN = "^(\\s){0,}[a-zA-Z0-9-_\\.]{1,50}[a-zA-Z0-9]{1,50}@[a-zA-Z0-9_-]{2,}(\\.[a-zA-Z0-9]{2,10}){1,2}(\\s){0,}$";
        public static final String CODE_PATTERN = "^[A-Za-z0-9_.]{4,50}$";
        public static final String BUSINESS_CODE = "^[A-Za-z0-9-]{10,20}$";
        public static final String WEBSITE = "((http|https)?:\\/\\/)?[-a-zA-Z0-9]{1,}\\.((\\-?[a-zA-Z0-9])+\\.)*([a-zA-Z0-9]+\\-?)*([a-zA-Z0-9]+)";
        public static final String ACCOUNT_NUMBER = "^(\\d{3,20})$";
        public static final String GL_CODE_PATTERN = "[A-Za-z0-9]{2,50}";
        public static final String BUSINESS_PLAN_NUMBER_PATTERN = "\\S{1,100}";
        public static final String MONTH_YEAR_PATTERN = "^((0[1-9])|(1[0-2]))\\/(\\d{4})$";
        public static final String NORM_3_DATE_PATTERN = "^([0-2][0-9]|3[0-1])\\/(0[0-9]|1[0-2])\\/([0-9][0-9])?[0-9][0-9]$";
        public static final String SPECIAL_CHARACTER = "[^a-zA-Z0-9]";

        private FORMAT() {
            throw new IllegalStateException("Utility class");
        }
    }

    public static final class DWH_LENGTH {
        public static final int CODE_MAX_LENGTH = 100;
        public static final int NAME_MAX_LENGTH = 1000;
        public static final int ASSET_CODE_MAX_LENGTH = 1000;
        public static final int NOTE_MAX_LENGTH = 1000;
        public static final int UNIT_MAX_LENGTH = 100;
        public static final int TAX_CODE_MAX_LENGTH = 1000;
        public static final int ADDRESS_MAX_LENGTH = 1000;
        public static final int PHONE_MAX_LENGTH = 100;
        public static final int SERIAL_MAX_LENGTH = 1000;
        public static final int MODEL_MAX_LENGTH = 1000;
        public static final int LOCATION_MAX_LENGTH = 1000;
        public static final int EMAIL_MAX_LENGTH = 1000;
        public static final int ID_MAX_LENGTH = 100;

        private DWH_LENGTH() {
            throw new IllegalStateException("Utility class");
        }
    }

    public static final class ACQUISITION_LENGTH {
        public static final int DESC_MAX_LENGTH = 200;
        public static final int CODE_MAX_LENGTH = 30;
        public static final int NAME_MAX_LENGTH = 200;

        private ACQUISITION_LENGTH() {
            throw new IllegalStateException("Utility class");
        }
    }
}
