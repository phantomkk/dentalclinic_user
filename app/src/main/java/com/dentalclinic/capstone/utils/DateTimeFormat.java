package com.dentalclinic.capstone.utils;

public enum DateTimeFormat {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    DATE_TIME_DB {
        public String toString() {
            return "yyyy-MM-dd HH:mm:ss";
        }
    },
    /**
     * h:mm a dd-MM-yyyy
     */
    DATE_TIME_APP {
        public String toString() {
            return "h:mm a dd-MM-yyyy";
        }
    },
    /**
     * dd-MM-yyyy
     */
    DATE_APP {
        public String toString() {
            return "dd-MM-yyyy";
        }
    };
}
