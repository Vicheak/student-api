package com.acledabank.vicheak.api.core.util;

public class FormatUtil {

    public static boolean checkPhoneFormat(String phone) {
        for (char digit : phone.toCharArray()) {
            if (digit == '-') continue;
            if (!Character.isDigit(digit))
                return false;
        }
        return true;
    }

}
