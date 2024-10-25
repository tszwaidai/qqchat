package com.echat.easychat.utils;

import java.security.SecureRandom;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: 随机形成12位ID
 * @date 2024/10/25 19:40
 */
public class RandomUserId {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int USER_ID_LENGTH = 12;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomUserId() {
        StringBuilder userId = new StringBuilder(USER_ID_LENGTH);
        for (int i = 0; i < USER_ID_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            userId.append(CHARACTERS.charAt(index));
        }
        return userId.toString();
    }

}
