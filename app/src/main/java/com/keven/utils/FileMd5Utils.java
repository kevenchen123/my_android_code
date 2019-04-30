package com.keven.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 获得文件的md5值
 */
public class FileMd5Utils {

    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int length;
            byte[] buffer = new byte[10240];
            while ((length = in.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            while (value.length() < 32) {
                value = "0" + value;
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        return value;
    }

    public static String getMd5ByFile(String filename) {
        return getMd5ByFile(new File(filename));
    }
}