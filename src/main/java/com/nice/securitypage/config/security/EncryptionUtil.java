package com.nice.securitypage.config.security;


import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {

    // SHA-256 알고리즘을 이용하여 입력된 문자열을 암호화하는 메서드
    public static String encryptSHA256(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(md5.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 알고리즘이 없는 경우 예외를 발생시킴
            throw new RuntimeException(e);
        }
    }

    // MD5 알고리즘을 이용하여 입력된 문자열을 암호화하는 메서드
    public static String encryptMD5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}