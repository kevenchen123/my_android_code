package com.keven.nanohttpd;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NetInfoUtil {

    public static String getIP(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {
        return (i & 0xFF ) + "." +
            ((i >> 8 ) & 0xFF) + "." +
            ((i >> 16 ) & 0xFF) + "." +
            ( i >> 24 & 0xFF) ;
    }


    public static void copyResourceFile(Context context, int rid, String targetFile) {
        try {
            InputStream fin = context.getResources().openRawResource(rid);
            FileOutputStream fos = new FileOutputStream(targetFile);

            int     length;
            byte[] buffer = new byte[1024*32];
            while( (length = fin.read(buffer)) != -1){
                fos.write(buffer,0,length);
            }
            fin.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}