package com.keven.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
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