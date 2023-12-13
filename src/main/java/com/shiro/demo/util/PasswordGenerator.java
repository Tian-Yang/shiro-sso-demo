package com.shiro.demo.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "@*";

    public static String generatePassword() {
        // 生成数字
        List<Character> passwordChars = generateRandomCharacters(DIGITS, 5);

        // 生成大小写字母
        passwordChars.addAll(generateRandomCharacters(LOWERCASE_LETTERS, 1));
        passwordChars.addAll(generateRandomCharacters(UPPERCASE_LETTERS, 1));

        // 生成符号
        passwordChars.addAll(generateRandomCharacters(SYMBOLS, 1));

        // 打乱字符顺序
        Collections.shuffle(passwordChars);

        // 将字符列表转换为字符串
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    private static List<Character> generateRandomCharacters(String characters, int count) {
        List<Character> result = new ArrayList<>();
        Random random = new SecureRandom();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(characters.length());
            result.add(characters.charAt(index));
        }
        return result;
    }

    public static void main(String[] args) {
        String generatedPassword = generatePassword();
        System.out.println("Generated Password: " + generatedPassword);
    }
}
