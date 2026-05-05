package com.exam.online.utils;

import java.security.MessageDigest;

public class PasswordUtil {

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    public static boolean verify(String input, String hashed) {
        return md5(input).equals(hashed);
    }
}