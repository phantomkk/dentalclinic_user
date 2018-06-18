package com.dentalclinic.capstone.utils;

public enum DateTimeFormat {
    DATE_TIME_DB {
        public String toString() {
            return "yyyy-MM-dd HH:mm:ss";
        }
    },
    DATE_TIME_APP {
        public String toString() {
            return "h:mm a dd-MM-yyyy";
        }
    },
    DATE_FOTMAT{
        public String toString() {
            return "dd-MM-yyyy";
        }
    },
    /**
     * dd-MM-yyyy
     */
    DATE_APP {
        public String toString() {
            return "dd-MM-yyyy";
        }
    },
    DATE_APP_2 {
        public String toString() {
            return "dd/MM/yyyy";
        }
    };
}
