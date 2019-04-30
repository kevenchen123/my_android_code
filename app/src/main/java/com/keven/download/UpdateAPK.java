package com.keven.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.keven.utils.FileMd5Utils;

import java.io.File;

/**
 * APP更新
 */
public class UpdateAPK {

    private File downfile;
    private long fileContentLength;

    public void update(Context context, String downloadURL, String md5) {
        //文件已下载点击重新安装
        if (checkFileAndOpen(context, downfile, fileContentLength, md5, true)) {
            return;
        }

        DownLoadForUpDate download = new DownLoadForUpDate();
        download.downLoadFile(downloadURL,
            new ProgressResponseBody.ProgressResponseListener() {
                @Override
                public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                    if (fileContentLength == 0) {
                        fileContentLength = contentLength;
                    }
                    //进度条更新
                    Log.d("onResponseProgress", "bytesRead=" + bytesRead + ",contentLength=" + contentLength + ",done=" + done);
                }
            },
            new DownLoadForUpDate.DownLoadCallBack() {
                @Override
                public void onsuccess(File file) {
                    downfile = file;
                    //启动新界面
                    checkFileAndOpen(context, downfile, fileContentLength, md5, true);
                }
                @Override
                public void onfaile(String errMsg) {
                    //下载失败
                }
            });
    }

    /***
     * 检测文件是否完整并返回信息
     */
    public static boolean checkFileAndOpen(Context context, File file, long fileContentLength, String md5, boolean install) {
        if (file == null || fileContentLength == 0) {
            return false;
        }
        if (!md5.equals(FileMd5Utils.getMd5ByFile(file))) {
            return false;
        }
        if (install) {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 24) {
                Uri apkUri = FileProvider.getUriForFile(context, "com.keven.fileprovider", file);
                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent1.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent1.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            context.startActivity(intent1);
        }
        return true;
    }
}