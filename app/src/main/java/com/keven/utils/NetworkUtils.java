package com.keven.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {

    public static final int NETTYPE_CMNET = 3;
    public static final int NETTYPE_CMWAP = 2;
    public static final int NETTYPE_WIFI = 1;

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        int i = 0;
        if (activeNetworkInfo == null) {
            return 0;
        }
        int type = activeNetworkInfo.getType();
        if (type == 0) {
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if (extraInfo != null && extraInfo.equals("")) {
                i = extraInfo.toLowerCase().equals("cmnet") ? NETTYPE_CMNET : NETTYPE_CMWAP;
            }
        } else if (type == NETTYPE_WIFI) {
            i = NETTYPE_WIFI;
        }
        return i;
    }

    /**
     * 测试是否能上网
     */
    public static boolean isConnectOutWeb(String hostName) {
        try {
            DNSResolver dnsRes = new DNSResolver(hostName);
            Thread t = new Thread(dnsRes);
            t.start();
            t.join(1000);
            InetAddress inetAddr = dnsRes.get();
            return inetAddr != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static class DNSResolver implements Runnable {
        private String domain;
        private InetAddress inetAddr;

        public DNSResolver(String domain) {
            this.domain = domain;
        }

        public void run() {
            try {
                InetAddress addr = InetAddress.getByName(domain);
                set(addr);
            } catch (UnknownHostException e) {
            }
        }

        public synchronized void set(InetAddress inetAddr) {
            this.inetAddr = inetAddr;
        }

        public synchronized InetAddress get() {
            return inetAddr;
        }
    }
}