package com.test.address.utils;

public class ValidationUtil {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // 전화번호 유효성 검사 로직
        return phoneNumber.matches("^010-?\\d{3,4}-?\\d{4}$");
    }

    public static boolean isValidEmail(String email) {
        // 이메일 유효성 검사 로직
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

}
