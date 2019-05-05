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

    public static int getUint16(int i) {
        return i & 65535;
    }

    public static long getUint32(long j) {
        return j & -1;
    }

    public static short getUint8(short s) {
        return (short) (s & 255);
    }

    private static int reverse(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 += ((i >> i4) & 1) << ((i2 - 1) - i4);
        }
        return i3;
    }

    public static byte[] HexStr2bytes(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 1;
            int i4 = i3 + 1;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str.substring(i2, i3));
            stringBuilder.append(str.substring(i3, i4));
            String stringBuilder2 = stringBuilder.toString();
            byte[] bytes = stringBuilder2.getBytes();
            if ((bytes[0] < (byte) 48 || bytes[0] > (byte) 57) && ((bytes[0] < (byte) 97 || bytes[0] > (byte) 102) && (bytes[0] < (byte) 65 || bytes[0] > (byte) 70))) {
                return null;
            }
            if ((bytes[1] < (byte) 48 || bytes[1] > (byte) 57) && ((bytes[1] < (byte) 97 || bytes[1] > (byte) 102) && (bytes[1] < (byte) 65 || bytes[1] > (byte) 70))) {
                return null;
            }
            bArr[i] = (byte) (Integer.parseInt(stringBuilder2, 16) & 255);
        }
        return bArr;
    }

    public static String byte2HexStr(byte[] bArr, int i) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i2 = 0; i2 < i; i2++) {
            StringBuilder stringBuilder2;
            String str;
            String toHexString = Integer.toHexString(bArr[i2] & 255);
            if (toHexString.length() == 1) {
                stringBuilder2 = new StringBuilder();
                str = "0";
            } else {
                stringBuilder2 = new StringBuilder();
                str = "";
            }
            stringBuilder2.append(str);
            stringBuilder2.append(toHexString);
            stringBuilder.append(stringBuilder2.toString());
        }
        return stringBuilder.toString().toUpperCase().trim();
    }

    public static int getIntFromBytes(byte[] bArr, int i, int i2) {
        if (i2 > 4) {
            return 0;
        }
        try {
            byte[] bArr2 = new byte[4];
            System.arraycopy(bArr, i, bArr2, 0, i2);
            return bytesToInt(bArr2, 0);
        } catch (Exception unused) {
            return 0;
        }
    }

    public static byte[] intToBytes(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] intToBytes2(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] longToBytes(long j) {
        return new byte[]{(byte) ((int) ((j >> 24) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) (j & 255))};
    }

    public static byte[] longToBytes2(long j) {
        return new byte[]{(byte) ((int) ((j >> 24) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) (j & 255))};
    }

    public static int bytesToInt(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (((bArr[i] & 255) | ((bArr[i + 1] & 255) << 8)) | ((bArr[i + 2] & 255) << 16));
    }

    public static int bytesToInt2(byte[] bArr, int i) {
        return (bArr[i + 3] & 255) | ((((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8));
    }

    public static long getCRC32(byte[] bArr) {
        long[] jArr = new long[]{0, 1996959894, -301047508, -1727442502, 124634137, 1886057615, -379345611, -1637575261, 249268274, 2044508324, -522852066, -1747789432, 162941995, 2125561021, -407360249, -1866523247, 498536548, 1789927666, -205950648, -2067906082, 450548861, 1843258603, -187386543, -2083289657, 325883990, 1684777152, -43845254, -1973040660, 335633487, 1661365465, -99664541, -1928851979, 997073096, 1281953886, -715111964, -1570279054, 1006888145, 1258607687, -770865667, -1526024853, 901097722, 1119000684, -608450090, -1396901568, 853044451, 1172266101, -589951537, -1412350631, 651767980, 1373503546, -925412992, -1076862698, 565507253, 1454621731, -809855591, -1195530993, 671266974, 1594198024, -972236366, -1324619484, 795835527, 1483230225, -1050600021, -1234817731, 1994146192, 31158534, -1731059524, -271249366, 1907459465, 112637215, -1614814043, -390540237, 2013776290, 251722036, -1777751922, -519137256, 2137656763, 141376813, -1855689577, -429695999, 1802195444, 476864866, -2056965928, -228458418, 1812370925, 453092731, -2113342271, -183516073, 1706088902, 314042704, -1950435094, -54949764, 1658658271, 366619977, -1932296973, -69972891, 1303535960, 984961486, -1547960204, -725929758, 1256170817, 1037604311, -1529756563, -740887301, 1131014506, 879679996, -1385723834, -631195440, 1141124467, 855842277, -1442165665, -586318647, 1342533948, 654459306, -1106571248, -921952122, 1466479909, 544179635, -1184443383, -832445281, 1591671054, 702138776, -1328506846, -942167884, 1504918807, 783551873, -1212326853, -1061524307, -306674912, -1698712650, 62317068, 1957810842, -355121351, -1647151185, 81470997, 1943803523, -480048366, -1805370492, 225274430, 2053790376, -468791541, -1828061283, 167816743, 2097651377, -267414716, -2029476910, 503444072, 1762050814, -144550051, -2140837941, 426522225, 1852507879, -19653770, -1982649376, 282753626, 1742555852, -105259153, -1900089351, 397917763, 1622183637, -690576408, -1580100738, 953729732, 1340076626, -776247311, -1497606297, 1068828381, 1219638859, -670225446, -1358292148, 906185462, 1090812512, -547295293, -1469587627, 829329135, 1181335161, -882789492, -1134132454, 628085408, 1382605366, -871598187, -1156888829, 570562233, 1426400815, -977650754, -1296233688, 733239954, 1555261956, -1026031705, -1244606671, 752459403, 1541320221, -1687895376, -328994266, 1969922972, 40735498, -1677130071, -351390145, 1913087877, 83908371, -1782625662, -491226604, 2075208622, 213261112, -1831694693, -438977011, 2094854071, 198958881, -2032938284, -237706686, 1759359992, 534414190, -2118248755, -155638181, 1873836001, 414664567, -2012718362, -15766928, 1711684554, 285281116, -1889165569, -127750551, 1634467795, 376229701, -1609899400, -686959890, 1308918612, 956543938, -1486412191, -799009033, 1231636301, 1047427035, -1362007478, -640263460, 1088359270, 936918000, -1447252397, -558129467, 1202900863, 817233897, -1111625188, -893730166, 1404277552, 615818150, -1160759803, -841546093, 1423857449, 601450431, -1285129682, -1000256840, 1567103746, 711928724, -1274298825, -1022587231, 1510334235, 755167117};
        long j = 0;
        for (byte b : bArr) {
            j = getUint32(getUint32((j << 8) | ((long) getUint8((short) b))) ^ getUint32(jArr[(short) (getUint8((short) ((int) (j >> 24))) & 255)]));
        }
        return j & 4294967295L;
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