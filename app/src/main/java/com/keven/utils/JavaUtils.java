package com.keven.utils;

import java.math.BigDecimal;
import java.util.UUID;

public class JavaUtils {

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static byte[] intToByte(int v) {
        byte[] b = new byte[4];
        b[3] = (byte) (0xff & v);
        b[2] = (byte) ((0xff00 & v) >> 8);
        b[1] = (byte) ((0xff0000 & v) >> 16);
        b[0] = (byte) ((0xff000000 & v) >> 24);
        return b;
    }

    public static double div(double v1, double v2, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}