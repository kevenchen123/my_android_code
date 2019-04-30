package com.keven.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    private static String getItemOfXml(String xml, String tagName) {
        try {
            StringReader sr = new StringReader(xml);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList resultList = doc.getElementsByTagName(tagName);
            if (resultList != null && resultList.getLength() > 0) {
                Node resultEle = resultList.item(0);
                return resultEle.getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}