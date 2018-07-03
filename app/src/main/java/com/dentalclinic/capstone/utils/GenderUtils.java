package com.dentalclinic.capstone.utils;

public class GenderUtils {

    private static final String MALE = "MALE";
    private static final String FORMAT_MALE = "Nam";
    private static final String FEMALE = "FEMALE";
    private static final String FORMAT_FEMALE = "Nữ";
    private static final String OTHER = "OTHER";
    private static final String FORMAT_OTHER = "Khác";

    public static String toString(String gender) {
        String rs = FORMAT_OTHER;
        switch (gender) {
            case MALE:
                rs = FORMAT_MALE;
                break;
            case FEMALE:
                rs = FORMAT_FEMALE;
                break;
            case OTHER:
                rs = FORMAT_OTHER;
                break;
        }
        return rs;
    }
}
