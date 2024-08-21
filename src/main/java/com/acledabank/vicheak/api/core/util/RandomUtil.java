package com.acledabank.vicheak.api.core.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomUtil(){

    }

    public static String getRandomNumber() {
        // It will generate a 6-digit random number from 0 to 999999
        int number = RANDOM.nextInt(999999);

        // This will convert any number sequence into 6 characters
        return String.format("%06d", number);
    }

    public static String randomTokenGenerator(int tokenLength) {
        // Characters allowed in the token
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder(tokenLength);

        for (int i = 0; i < tokenLength; i++) {
            int index = SECURE_RANDOM.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }

        return token.toString();
    }

}
