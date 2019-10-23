package com.keven.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    public static final String TAG = "NetworkUtils";

    /*
    STATE_EMERGENCY_ONLY = 2;
    STATE_IN_SERVICE = 0;
    STATE_OUT_OF_SERVICE = 1;
    STATE_POWER_OFF = 3;
   */
    private volatile int mCurrentServiceState = ServiceState.STATE_POWER_OFF;

    private Context mContext = null;
    private TelephonyManager mTelephonyManager = null;
    private ConnectivityManager mConnMgr = null;
    private ConnectivityBroadcastReceiver mReceiver = null;

    private ConnectivityListener mListener = null;
    private IntentFilter mIntentFilter = null;

    private PhoneStateListener mPhoneStateListener = null;
    private Looper mLooper = null;

    public enum TYPES {
        disconnect,
        other,
        wifi,
        mobile
    }

    public interface ConnectivityListener {
        void onStateChanged(final TYPES state);
    }

    public static NetworkUtils build(final Context context, final ConnectivityListener listener) {
        NetworkUtils conn = new NetworkUtils(context);
        conn.register(listener);
        return conn;
    }

    private NetworkUtils(final Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        mReceiver = new ConnectivityBroadcastReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public void register(final ConnectivityListener listener) {
        if (mListener == null) {
            if (mTelephonyManager != null) {
                mCurrentServiceState = ServiceState.STATE_IN_SERVICE;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mLooper = Looper.myLooper();

                        mPhoneStateListener = new PhoneStateListener() {
                            @Override
                            public void onServiceStateChanged(final ServiceState serviceState) {
                                LogUtils.d(TAG, "CurrentServiceState: " + mCurrentServiceState + "    getServiceState: " + serviceState.getState());
                                if (mCurrentServiceState != serviceState.getState()) {
                                    mCurrentServiceState = serviceState.getState();
                                    onPhoneStateChanged(mCurrentServiceState);
                                }
                            }
                            @Override
                            public void onDataConnectionStateChanged(final int state, final int networkType) {
                                LogUtils.d(TAG, "onDataConnectionStateChanged: " + state + " , " + networkType);
                                mCurrentServiceState = (state == TelephonyManager.DATA_DISCONNECTED)
                                        ? ServiceState.STATE_OUT_OF_SERVICE
                                        : ServiceState.STATE_IN_SERVICE;
                            }
                        };
                        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SERVICE_STATE);

                        Looper.loop();
                    }
                }).start();
            }
            if (mConnMgr != null) {
                mContext.registerReceiver(mReceiver, mIntentFilter);
            }
        }
        mListener = listener;
    }

    public void unregister() {
        if (mListener != null) {
            if (mTelephonyManager != null) {
                if (mLooper != null) mLooper.quit();
                mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
                mCurrentServiceState = ServiceState.STATE_POWER_OFF;
            }
            if (mConnMgr != null) {
                mContext.unregisterReceiver(mReceiver);
            }
        }
        mListener = null;
    }

    private void onPhoneStateChanged(final int serviceState) {
        if (mListener != null) {
            mListener.onStateChanged(getActiveNetworkType(null, mConnMgr));
        }
    }

    /**
     * Connectivity change broadcast receiver. This gets the network connectivity updates.
     * In case we don't get the active connectivity when we first acquire the network,
     * this receiver will notify us when it is connected, so to unblock the waiting thread
     * which is sending the message.
     */
    public class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(TAG, "ConnectivityBroadcastReceiver: " + intent.getAction());
            if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                return;
            }
            if (mListener != null) {
                mListener.onStateChanged(getActiveNetworkType(context, mConnMgr));
            }
        }
    }

    //--------------------------------------------------------------

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo wifiNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isConnected();
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo wifiNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiNetworkInfo.isConnected();
    }

    public static TYPES getActiveNetworkType(Context context, ConnectivityManager mConnMgr) {
        ConnectivityManager connMgr = mConnMgr != null ? mConnMgr : ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connMgr != null) {
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return TYPES.mobile;
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_WIMAX) {
                    return TYPES.wifi;
                } else {
                    return TYPES.other;
                }
            } else {
                return TYPES.disconnect;
            }
        } else {
            return TYPES.disconnect;
        }
    }

    //--------------------------------------------------------------

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

    /**
     * Check if there is fast connectivity
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

}