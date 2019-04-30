package com.keven.utils;

import android.util.Log;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
    /**
     * md5编码
     */
    public static String MD5Encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    //--------------------------------------------------------------

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_MD5 = "HmacMD5";
    private static final String ENCODING = "UTF-8";

    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) {
        return encrypt(HMAC_SHA1, encryptText, encryptKey);
    }

    public static String HmacMD5Encrypt(String encryptText, String encryptKey) {
        return encrypt(HMAC_MD5, encryptText, encryptKey);
    }

    private static String encrypt(String algorithm, String encryptText, String encryptKey) {
        try {
            byte[] data = encryptKey.getBytes(ENCODING);
            SecretKey secretKey = new SecretKeySpec(data, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            byte[] text = encryptText.getBytes(ENCODING);
            byte[] rawHmac = mac.doFinal(text);
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(byteToHexString(b));
            }
            return sb.toString();
        } catch (Exception e) {
            Log.e("TAG", e.toString());
            return null;
        }
    }

    private static String byteToHexString(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0f];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    public static String bytesToHex(byte[] resultBytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < resultBytes.length; i++) {
            String hex = Integer.toHexString(0xFF & resultBytes[i]);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }
        return builder.toString();
    }

}