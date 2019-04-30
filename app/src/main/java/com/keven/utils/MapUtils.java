package com.keven.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/*
 * 地图工具
 */
public class MapUtils {

    private static final double PI = Math.PI * 3000 / 180.0;
    public static final String GD_MODE_WALK = "4";
    public static final String GD_MODE_DRIVE = "2";
    public static final String BD_MODE_WALK = "walking";
    public static final String BD_MODE_DRIVE = "driving";
    public static final String TX_MODE_WALK = "walk";
    public static final String TX_MODE_DRIVE = "drive";


    public static double[] bdTogcj(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    public static double[] gcjTobd(double gcj_lon, double gcj_lat) {
        double[] bd_lat_lon = new double[2];
        double x = gcj_lon, y = gcj_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    public static void openGaoDeMapByDrive(Context context, double slon, double slat, String sname, double dlon, double dlat, String dname) {
        try {
            StringBuilder loc = new StringBuilder();
            loc.append("androidamap://route?sourceApplication=mango");
            loc.append("&slat=");
            loc.append(slat);
            loc.append("&slon=");
            loc.append(slon);
            loc.append("&sname=");
            loc.append(sname);
            loc.append("&dlat=");
            loc.append(dlat);
            loc.append("&dlon=");
            loc.append(dlon);
            loc.append("&dname=");
            loc.append(dname);
            loc.append("&dev=0");
            loc.append("&t=");
            loc.append(GD_MODE_DRIVE);
            Intent intent = Intent.getIntent(loc.toString());
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "请先安装高德地图", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    public static void openBaiduMapByDrive(Context context, double slon, double slat, String sname, double dlon, double dlat, String dname) {
        try {
            double[] sbd09cc = gcjTobd(slon, slat);
            double[] dbd09cc = gcjTobd(dlon, dlat);
            StringBuilder loc = new StringBuilder();
            loc.append("baidumap://map/direction?origin=latlng:");
            loc.append(sbd09cc[1]);
            loc.append(",");
            loc.append(sbd09cc[0]);
            loc.append("|name:");
            loc.append(sname);
            loc.append("&destination=latlng:");
            loc.append(dbd09cc[1]);
            loc.append(",");
            loc.append(dbd09cc[0]);
            loc.append("|name:");
            loc.append(dname);
            loc.append("&mode=");
            loc.append(BD_MODE_DRIVE);
            //    loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = new Intent();
            Log.e("kowen", "导航字符串" + loc.toString());
            intent.setData(Uri.parse(loc.toString()));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "请先安装百度地图", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}