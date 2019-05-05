package com.keven.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

import java.util.List;

public class WifiHelper {
    private static final String TAG = "WifiHelper";
    private List<ScanResult> listResult;
    private ScanResult mScanResult;
    private StringBuffer mStringBuffer = new StringBuffer();
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiInfo mWifiInfo;
    WifiLock mWifiLock;
    private WifiManager mWifiManager;

    public WifiHelper(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public static boolean tryDisableWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                return wifiManager.setWifiEnabled(false);
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean tryEnableWifi(Context context) {
        String str;
        StringBuilder stringBuilder;
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                return true;
            }
            try {
                boolean wifiEnabled = wifiManager.setWifiEnabled(true);
                if (wifiEnabled) {
                    int i = 10;
                    while (i > 0) {
                        i--;
                        if (wifiManager.isWifiEnabled()) {
                            wifiEnabled = true;
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
                return wifiEnabled;
            } catch (Exception e3) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("WifiHelper ");
                stringBuilder.append(e3.toString());
                Log.e(str, stringBuilder.toString());
                return false;
            }
        } catch (Exception e32) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("WifiHelper ");
            stringBuilder.append(e32.toString());
            Log.e(str, stringBuilder.toString());
            return false;
        }
    }

    public static boolean checkWifiConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
                if (allNetworkInfo != null) {
                    for (NetworkInfo typeName : allNetworkInfo) {
                        if (typeName.getTypeName().equalsIgnoreCase("WIFI")) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static String checkWifiConnected(Context context, String str, String str2) {
        String str3 = "null";
        if (!(context == null || str == null)) {
            try {
                if (str.length() > 0) {
                    str = str.toLowerCase();
                    if (checkWifiConnected(context)) {
                        str = str.toLowerCase();
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        if (3 == wifiManager.getWifiState()) {
                            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                            if (connectionInfo != null) {
                                try {
                                    if (connectionInfo.getSSID() != null) {
                                        String toLowerCase = connectionInfo.getSSID().toLowerCase();
                                        String intToIp = intToIp(connectionInfo.getIpAddress());
                                        if (toLowerCase.contains(str) && intToIp.contains(str2)) {
                                            toLowerCase = "";
                                        }
                                        str3 = toLowerCase;
                                        return str3;
                                    }
                                } catch (Exception unused) {
                                }
                            }
                            str3 = "null";
                            return str3;
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ex=");
                stringBuilder.append(e.toString());
                return stringBuilder.toString();
            }
        }
        return str3;
    }

    private static String intToIp(int i) {
        String str = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i & 255);
            stringBuilder.append(".");
            stringBuilder.append((i >> 8) & 255);
            stringBuilder.append(".");
            stringBuilder.append((i >> 16) & 255);
            stringBuilder.append(".");
            stringBuilder.append((i >> 24) & 255);
            str = stringBuilder.toString();
            return str;
        } catch (Exception unused) {
            return str;
        }
    }

    public static ScanResult getScanResultWithName(Context context, String str) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return null;
            }
            List scanResults = wifiManager.getScanResults();
            if (scanResults == null) {
                return null;
            }
            int size = scanResults.size();
            int i = 0;
            while (i < size) {
                try {
                    if (((ScanResult) scanResults.get(i)).SSID.toLowerCase().contains(str)) {
                        return (ScanResult) scanResults.get(i);
                    }
                    i++;
                } catch (Exception unused) {
                }
            }
            return null;
        } catch (Exception unused2) {
            return null;
        }
    }

    public static WifiConfiguration getWifiConfiguration(WifiManager wifiManager, String str) {
        try {
            for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()) {
                try {
                    String str2 = wifiConfiguration.SSID;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("\"");
                    stringBuilder.append(str);
                    stringBuilder.append("\"");
                    if (str2.equalsIgnoreCase(stringBuilder.toString()) || wifiConfiguration.SSID.equalsIgnoreCase(str)) {
                        return wifiConfiguration;
                    }
                } catch (Exception unused) {
                }
            }
            return null;
        } catch (Exception unused2) {
            return null;
        }
    }

    public static WifiConfiguration createWifiConfig(Context context, String str, String str2, int i) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return null;
            }
            WifiConfiguration wifiConfiguration = getWifiConfiguration(wifiManager, str);
            if (wifiConfiguration != null) {
                return wifiConfiguration;
            }
            wifiConfiguration = new WifiConfiguration();
            try {
                wifiConfiguration.networkId = -1;
                try {
                    wifiConfiguration.allowedAuthAlgorithms.clear();
                    wifiConfiguration.allowedGroupCiphers.clear();
                    wifiConfiguration.allowedKeyManagement.clear();
                    wifiConfiguration.allowedPairwiseCiphers.clear();
                    wifiConfiguration.allowedProtocols.clear();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("\"");
                    stringBuilder.append(str);
                    stringBuilder.append("\"");
                    wifiConfiguration.SSID = stringBuilder.toString();
                    if (i == 1) {
                        wifiConfiguration.wepKeys[0] = "";
                        wifiConfiguration.allowedKeyManagement.set(0);
                        wifiConfiguration.wepTxKeyIndex = 0;
                    }
                    if (i == 2) {
                        wifiConfiguration.hiddenSSID = true;
                        String[] strArr = wifiConfiguration.wepKeys;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("\"");
                        stringBuilder2.append(str2);
                        stringBuilder2.append("\"");
                        strArr[0] = stringBuilder2.toString();
                        wifiConfiguration.allowedAuthAlgorithms.set(1);
                        wifiConfiguration.allowedGroupCiphers.set(3);
                        wifiConfiguration.allowedGroupCiphers.set(2);
                        wifiConfiguration.allowedGroupCiphers.set(0);
                        wifiConfiguration.allowedGroupCiphers.set(1);
                        wifiConfiguration.allowedKeyManagement.set(0);
                        wifiConfiguration.wepTxKeyIndex = 0;
                    }
                    if (i != 3) {
                        return wifiConfiguration;
                    }
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("\"");
                    stringBuilder3.append(str2);
                    stringBuilder3.append("\"");
                    wifiConfiguration.preSharedKey = stringBuilder3.toString();
                    wifiConfiguration.hiddenSSID = true;
                    wifiConfiguration.allowedAuthAlgorithms.set(0);
                    wifiConfiguration.allowedGroupCiphers.set(2);
                    wifiConfiguration.allowedKeyManagement.set(1);
                    wifiConfiguration.allowedPairwiseCiphers.set(1);
                    wifiConfiguration.allowedGroupCiphers.set(3);
                    wifiConfiguration.allowedPairwiseCiphers.set(2);
                    wifiConfiguration.status = 2;
                    return wifiConfiguration;
                } catch (Exception unused) {
                    return null;
                }
            } catch (Exception unused2) {
                return wifiConfiguration;
            }
        } catch (Exception unused3) {
            return null;
        }
    }

    public static boolean disconnectNetwork(Context context) {
        if (context != null) {
            try {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    try {
                        wifiManager.disconnect();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean addNetwork(Context context, WifiConfiguration wifiConfiguration) {
        if (context == null || wifiConfiguration == null) {
            return false;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null || wifiConfiguration == null) {
                return false;
            }
            try {
                return wifiManager.enableNetwork(wifiManager.addNetwork(wifiConfiguration), true);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return false;
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean enableWifiConfiguration(Context context, WifiConfiguration wifiConfiguration) {
        if (context == null) {
            return false;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                return wifiManager.enableNetwork(wifiConfiguration.networkId, true);
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public void openNetCard() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeNetCard() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public void checkNetCardState() {
        if (this.mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Log.i(TAG, "网卡正在关闭");
        } else if (this.mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            Log.i(TAG, "网卡已经关闭");
        } else if (this.mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Log.i(TAG, "网卡正在打开");
        } else if (this.mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            Log.i(TAG, "网卡已经打开");
        } else {
            Log.i(TAG, "没有获取到状态-_-");
        }
    }

    public int getWifiState() {
        WifiManager wifiManager = this.mWifiManager;
        return wifiManager != null ? wifiManager.getWifiState() : 0;
    }

    public void scan() {
        this.mWifiManager.startScan();
        this.listResult = this.mWifiManager.getScanResults();
        if (this.listResult != null) {
            Log.i(TAG, "当前区域存在无线网络，请查看扫描结果");
        } else {
            Log.i(TAG, "当前区域没有无线网络");
        }
    }

    public String getScanResult() {
        if (this.mStringBuffer != null) {
            this.mStringBuffer = new StringBuffer();
        }
        scan();
        this.listResult = this.mWifiManager.getScanResults();
        if (this.listResult != null) {
            int i = 0;
            while (i < this.listResult.size()) {
                this.mScanResult = (ScanResult) this.listResult.get(i);
                StringBuffer stringBuffer = this.mStringBuffer;
                stringBuffer.append("NO.");
                i++;
                stringBuffer.append(i);
                stringBuffer.append(" :");
                stringBuffer.append(this.mScanResult.SSID);
                stringBuffer.append("->");
                stringBuffer.append(this.mScanResult.BSSID);
                stringBuffer.append("->");
                stringBuffer.append(this.mScanResult.capabilities);
                stringBuffer.append("->");
                stringBuffer.append(this.mScanResult.frequency);
                stringBuffer.append("->");
                stringBuffer.append(this.mScanResult.level);
                stringBuffer.append("->");
                stringBuffer.append(this.mScanResult.describeContents());
                stringBuffer.append("\n\n");
                this.mStringBuffer = stringBuffer;
            }
        }
        Log.i(TAG, this.mStringBuffer.toString());
        return this.mStringBuffer.toString();
    }

    public List<ScanResult> getScanResultList() {
        scan();
        this.listResult = this.mWifiManager.getScanResults();
        return this.listResult;
    }

    public void connect() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            Log.d("wifiInfo", wifiInfo.toString());
            Log.d("wifiInfo", this.mWifiInfo.getSSID());
        }
    }

    public void disconnectWifi() {
        try {
            this.mWifiManager.disableNetwork(getNetworkId());
            this.mWifiManager.disconnect();
            this.mWifiInfo = null;
        } catch (Exception unused) {
        }
    }

    public void checkNetWorkState() {
        if (this.mWifiInfo != null) {
            Log.i(TAG, "网络正常工作");
        } else {
            Log.i(TAG, "网络已断开");
        }
    }

    public int getNetworkId() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? 0 : wifiInfo.getNetworkId();
    }

    public int getIPAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? 0 : wifiInfo.getIpAddress();
    }

    public void acquireWifiLock() {
        this.mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.acquire();
        }
    }

    public void creatWifiLock() {
        this.mWifiLock = this.mWifiManager.createWifiLock("Test");
    }

    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }

    public void connectConfiguration(String str) {
        this.mWifiConfiguration = this.mWifiManager.getConfiguredNetworks();
        if (this.mWifiConfiguration != null) {
            int i = 0;
            while (i < this.mWifiConfiguration.size()) {
                try {
                    WifiConfiguration wifiConfiguration = (WifiConfiguration) this.mWifiConfiguration.get(i);
                    if (wifiConfiguration != null) {
                        String str2 = wifiConfiguration.SSID;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("\"");
                        stringBuilder.append(str);
                        stringBuilder.append("\"");
                        if (str2.equals(stringBuilder.toString())) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } catch (Exception unused) {
                }
                i++;
            }
            i = -1;
            if (i != -1 && i < this.mWifiConfiguration.size()) {
                try {
                    this.mWifiManager.enableNetwork(((WifiConfiguration) this.mWifiConfiguration.get(i)).networkId, true);
                } catch (Exception unused2) {
                }
            }
        }
    }

    public String getMacAddress() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.getMacAddress();
    }

    public String getSSID() {
        String str = "";
        try {
            return (this.mWifiManager == null || this.mWifiManager.getConnectionInfo() == null) ? str : this.mWifiManager.getConnectionInfo().getSSID();
        } catch (Exception unused) {
            return str;
        }
    }

    public String getBSSID() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.getBSSID();
    }

    public String getWifiInfo() {
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.toString();
    }
}
