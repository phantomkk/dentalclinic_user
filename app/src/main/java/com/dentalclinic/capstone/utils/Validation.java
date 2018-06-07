package com.dentalclinic.capstone.utils;

public class Validation {
    public static boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^\\d{9,}$");
    }

    public static boolean isNullOrEmpty(String source) {
        if (source != null && !source.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isPasswordValid(String password) {
        if (isNullOrEmpty(password)) {
            return false;
        }
        return password.length() > 8;
    }

    public static boolean isNameValid(String name) {
        if (isNullOrEmpty(name)) {
            return false;
        }
        return name.length() > 2;
    }

    public static boolean isAddressValid(String address) {
        if (isNullOrEmpty(address)) {
            return false;
        }
        return address.length() > 0;
    }
}
