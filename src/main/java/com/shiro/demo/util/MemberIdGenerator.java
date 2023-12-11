package com.shiro.demo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MemberIdGenerator {

    private static final int ID_LENGTH = 8;

    public static long generateMemberId() {
        long currentTimeMillis = System.currentTimeMillis();
        long shortenedValue = shortenAndHash(currentTimeMillis,8);



        // 截取后 8 位并转为整数
        return  shortenedValue;
    }

    private static long shortenAndHash(long value, int desiredLength) {
        try {
            // 将长整数值转换为字节数组
            byte[] bytes = new byte[8];
            for (int i = 0; i < 8; i++) {
                bytes[i] = (byte) (value >>> (i * 8));
            }

            // 使用SHA-256哈希函数
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(bytes);

            // 计算取多少位数字
            int numDigits = Math.min(desiredLength, hashBytes.length * 2);

            // 将哈希值的一部分转换为十进制数字
            StringBuilder numericValue = new StringBuilder();
            for (int i = 0; i < numDigits; i++) {
                int decimalValue = (hashBytes[i / 2] >>> ((i % 2 == 0) ? 4 : 0)) & 0x0F;
                numericValue.append(decimalValue);
            }

            return Long.parseLong(numericValue.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        // 获取当前日期和时间
        Date currentDate = new Date();

        // 格式化日期和时间
        String formattedDate = sdf.format(currentDate);
        String trimmedDate = formattedDate.substring(2);


        // 输出结果
        System.out.println("Formatted Date: " + Long.parseLong(trimmedDate));

    }
}
