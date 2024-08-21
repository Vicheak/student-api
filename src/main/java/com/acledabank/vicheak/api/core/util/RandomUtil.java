package com.acledabank.vicheak.api.core.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {

    private RandomUtil(){

    }

    public static String getRandomNumber() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static String randomTokenGenerator(int tokenLength) {
        // Characters allowed in the token
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom secureRandom = new SecureRandom();
        StringBuilder token = new StringBuilder(tokenLength);

        for (int i = 0; i < tokenLength; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }

        return token.toString();
    }

}
