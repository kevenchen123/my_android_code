package com.keven.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

/*
 * andrpoid 6.0 需要写运行时权限
 */
public class PermissionUtil {

    public static class PermissionListener {
        /**
         * 成功获取权限
         */
        protected void onGranted() {};
        /**
         * 获取权限失败
         */
        protected void onDenied() {};
    }

    public static void requestPermission(final Activity activity, String[] permissions, final PermissionListener listener, final String hint) {
        if (activity == null || permissions == null || listener == null) return;

        new RxPermissions(activity).request(permissions)
                .subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    listener.onGranted();
                } else {
                    listener.onDenied();
                    if (!TextUtils.isEmpty(hint)) {
                        showRequestPermissionDialog(activity, hint);
                    }
                }
            }
        });
    }

    private static void showRequestPermissionDialog(Context context, String hint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("权限请求");
        builder.setMessage(hint);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}