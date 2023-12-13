package com.shiro.demo.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shiro.demo.bean.MemberInfo;
import com.shiro.demo.jwt.AbstractJwtTokenPayload;
import com.shiro.demo.jwt.CsrfJwtTokenPayload;
import com.shiro.demo.jwt.JwtTokenPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Assert;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * JwtToken工具类
 * Author TianYang
 * Date 2023/11/15 11:04
 */
@Slf4j
public class JwtUtil {


    private static final String PRIVATE_KEY_FILE_RSA = "/usr/local/rsa/private-key.pem";
    private static final String PUBLIC_KEY_FILE_RSA = "/usr/local/rsa/public-key.pem";

    private static RSAPrivateKey rsaPrivateKey;

    private static RSAPublicKey rsaPublicKey;

    static {
        //静态初始化中完成签名证书读取，降低IO操作
        try {
            rsaPrivateKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(PRIVATE_KEY_FILE_RSA, "RSA");
            rsaPublicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(PUBLIC_KEY_FILE_RSA, "RSA");
        } catch (IOException e) {
            log.error("JwtUtil Read RSAPrivateKey or RSAPublicKey fail", e);
        }

    }

    /**
     * 创建JWT Token
     *
     * @param jwtTokenPayload JWT载体
     * @return java.lang.String
     * @Author TianYang
     * @Date 2023/11/15 14:17
     */
    public static String createJwtToken(AbstractJwtTokenPayload jwtTokenPayload) {
        Assert.notNull(jwtTokenPayload, "JwtUtil Jwt Token Payload can not be null");
        Assert.notNull(rsaPublicKey, "JwtUtil rsaPublicKey cant not be null");
        Assert.notNull(rsaPrivateKey, "JwtUtil rsaPrivateKey cant not be null");

        //使用ThreadLocalRandom 生成UUID，提升性能
        UUID jwtId = new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong());

        Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
        String jwtToken = JWT.create()
                .withPayload(JSON.toJSONString(jwtTokenPayload))
                .withJWTId(jwtId.toString().replaceAll("-", ""))
                .sign(algorithm);
        return jwtToken;
    }

    /**
     * JwtToken解析
     *
     * @param token
     * @return com.shiro.demo.jwt.JwtTokenPayload
     * @Author TianYang
     * @Date 2023/11/15 14:16
     */
    public static <T extends AbstractJwtTokenPayload> T decodeJwtToken(String token,Class<T> type) {
        Assert.notNull(token, "JwtUtil token can not be null");
        Assert.notNull(rsaPublicKey, "JwtUtil rsaPublicKey cant not be null");

        DecodedJWT decodedJWT = JWT.decode(token);
        DecodedJWT jwt = JWT.require(Algorithm.RSA256(rsaPublicKey))
                .build()
                .verify(decodedJWT);
        String payloadJson = new String(Base64.getDecoder().decode(jwt.getPayload().getBytes()));
        return JSON.parseObject(payloadJson, type);
    }
    /**
     * 获取Token失效时间
     *
     * @param days
     * @return java.lang.Long
     * @Author TianYang
     * @Date 2023/11/15 14:49
     */
    public static Long getExpirationTimeMillis(int days) {
        long expirationTimeMillis = System.currentTimeMillis() +  days * 86400*1000;
        return expirationTimeMillis;
    }

    /**
     * 获取Token失效时间
     *
     * @param seconds
     * @return java.lang.Long
     * @Author TianYang
     * @Date 2023/11/24 15:06
     */
    public static Long getExpirationTimeMillisOfSeconds(int seconds) {
        long expirationTimeMillis = System.currentTimeMillis() + 60 * seconds * 1000;
        return expirationTimeMillis;
    }


    /**
     * 判断Token是否失效
     *
     * @param expirationTimeMillis
     * @return boolean
     * @Author TianYang
     * @Date 2023/11/15 14:56
     */
    public static boolean isExpired(long expirationTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis >= expirationTimeMillis;
    }

    public static void main(String[] args) {
        Map<String, Object> subjectMap = new HashMap<>();
        subjectMap.put("memberId", "666");
        long expirationTimeMillis = System.currentTimeMillis() + 3600 * 1000;
        log.info("expirationTimeMillis:{}", expirationTimeMillis);
        JwtTokenPayload jwtTokenPayload = new JwtTokenPayload();
        MemberInfo memberInfo = new MemberInfo();
        jwtTokenPayload.setSub("666");
        memberInfo.setName("XiaoLi");
        jwtTokenPayload.setMemberInfo(memberInfo);
        try {
            String jwtToken = createJwtToken(jwtTokenPayload);
            log.info("jwtToken:{}", jwtToken);
            JwtTokenPayload jwtTokenPayload1 = decodeJwtToken(jwtToken,JwtTokenPayload.class);
            log.info("decodeJwtToken:{}", jwtTokenPayload1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
