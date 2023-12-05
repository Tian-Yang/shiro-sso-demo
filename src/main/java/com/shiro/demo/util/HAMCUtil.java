package com.shiro.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * HAMC算法加签验 签工具类。
 * Author TianYang
 * Date 2023/12/5 17:51
 */
@Slf4j
public class HAMCUtil {

    /**
     * 签名算法枚举
     * Author TianYang
     * Date 2023/12/5 17:44
     */
    public enum Algorithms {
        HmacMD5("HmacMD5"),
        HmacSHA1("HmacSHA1"),
        HmacSHA256("HmacSHA256"),
        HmacSHA384("HmacSHA384"),
        HmacSHA512("HmacSHA512");
        private String code;

        Algorithms(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * 加签
     *
     * @param key       签名密钥
     * @param body      签名消息体
     * @param algorithm 签名算法
     * @return java.lang.String 签名
     * @Author TianYang
     * @Date 2023/12/5 17:43
     */
    public static String signature(String key, String body, Algorithms algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        Assert.notNull(key, "HAMCUtil signature key is null");
        Assert.notNull(body, "HAMCUtil signature body is null");
        Assert.notNull(algorithm, "HAMCUtil signature algorithms is null");

        Mac hmacSha1 = Mac.getInstance(algorithm.getCode());
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm.getCode());
        hmacSha1.init(secretKeySpec);

        byte[] macData = hmacSha1.doFinal(body.getBytes());

        // 将结果转换为十六进制字符串
        StringBuilder result = new StringBuilder();
        try (Formatter formatter = new Formatter(result)) {
            for (byte b : macData) {
                formatter.format("%02x", b);
            }
        }
        return result.toString();
    }


    /**
     * 解签
     *
     * @param key
     * @param signature
     * @param body
     * @param algorithm
     * @return boolean
     * @Author TianYang
     * @Date 2023/12/5 17:44
     */
    public static boolean verifySignature(String key, String signature, String body, Algorithms algorithm) {
        Assert.notNull(signature, "HAMCUtil verifySignature signature is null");
        try {
            //接收到的签名
            String receivedSignature = signature;
            //接收到的消息体
            String receivedBody = body;

            // 重新计算签名
            String calculatedSignature = signature(key, receivedBody, algorithm);

            // 比较接收到的签名和重新计算的签名
            if (calculatedSignature.equals(receivedSignature)) {
                return true;
            }
        } catch (Exception e) {
            log.error("HAMCUtil verifySignature error", e);
        }
        return false;
    }

}
