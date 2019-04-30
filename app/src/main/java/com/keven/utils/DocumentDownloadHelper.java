package com.keven.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件下载助手
 * Created by Administrator on 2016/10/9.
 */
public class DocumentDownloadHelper {

    public String downloadRoot;
    public static final long State_Exist = -10086;
    public static final long State_No_InList = -12450;
    public static final String ACTION_COMPLETE = "ACTION_COMPLETE";

    private Context mContext;
    private static DocumentDownloadHelper mHelper;
    private Handler mHandler;
    private IntentFilter finishFilter;
    private List<Long> registerIdList;

    private DocumentDownloadHelper() {
        super();
    }

    public static DocumentDownloadHelper getHelper() {
        return mHelper;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mHelper = new DocumentDownloadHelper();
        mHelper.mContext = context;
        mHelper.mHandler = new Handler();
        mHelper.finishFilter = new IntentFilter();
        mHelper.finishFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mHelper.registerIdList = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mHelper.downloadRoot = Environment.getExternalStorageDirectory() + "/Documents/DownloadDoc/";
        } else {
            mHelper.downloadRoot = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/DownloadDoc/";
        }
    }

    /**
     * 开始下载
     *
     * @param uriStr
     * @return
     */
    public long[] startDownload(String uriStr) {
        String fileName = uriStr.substring(uriStr.lastIndexOf(File.separator));
        Log.e("wawawa", fileName);
        Log.e("wawawa", uriStr);
        long[] result = new long[2];
        if (!isComplete(fileName)) {
            long[] isInListLongs = isInList(mContext, fileName);
            final long isInList = isInListLongs[0];
            long isInListState = isInListLongs[1];
            if (isInList == State_No_InList) {
                long id = download(uriStr, fileName);
                result[0] = id;
                result[1] = DownloadManager.STATUS_RUNNING;
                //正在下载
                return result;
            } else {
                result = isInListLongs;
                if (isInListState == DownloadManager.STATUS_SUCCESSFUL) {
                    if (isComplete(fileName)) {
                        //完成
                        mContext.sendBroadcast(new Intent(ACTION_COMPLETE).putExtra("path", downloadRoot + fileName).putExtra("url", uriStr));
                        return result;
                    } else {
                        File file = new File(Environment.getExternalStorageDirectory() + downloadRoot + fileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadManager.remove(isInList);
                        //重新下载
                        long id = download(uriStr, fileName);
                        result[0] = id;
                        result[1] = DownloadManager.STATUS_RUNNING;
                        //正在下载
                        return result;
                    }
                } else if (isInListState == DownloadManager.STATUS_PENDING || isInListState == DownloadManager.STATUS_RUNNING) {
                    //正在下载
//                    registerCallBack(isInList, fileName);
                    return result;
                } else {
                    //正在暂停
//                    registerCallBack(isInList, fileName);
                    return result;
                }
            }
        } else {
            result[0] = State_Exist;
            mContext.sendBroadcast(new Intent(ACTION_COMPLETE).putExtra("path", downloadRoot + fileName).putExtra("url", uriStr));
            //完成
//            Toast.makeText(mContext, R.string.apk_already_finish, Toast.LENGTH_SHORT).show();
//            installApk(fileName);
            return result;
        }
    }

    public long download(String uriStr, final String fileName) {
        Uri uri = Uri.parse(uriStr);
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(downloadRoot, fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (fileName.endsWith(".doc")) {
            request.setMimeType("application/msword");
        } else if (fileName.endsWith(".pdf")) {
            request.setMimeType("application/pdf");
        } else if (fileName.endsWith(".docx")) {
            request.setMimeType("docx,application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }
        final long id = downloadManager.enqueue(request);
        registerCallBack(id, fileName);
        return id;
    }

    public void registerCallBack(final long id, final String fileName) {
        if (!registerIdList.contains(id)) {
            mContext.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (id == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
//                        Toast.makeText(mContext, R.string.apk_already_finish, Toast.LENGTH_SHORT).show();
//                        installApk(fileName);
                    }
                }
            }, finishFilter);
            registerIdList.add(id);
        }
    }

    private static long[] isInList(Context context, String fileName) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor c = downloadManager.query(query);
        long[] result = new long[2];
        if (c.moveToFirst()) {
            do {
                String cFileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                if (cFileName == null) {
                    continue;
                } else if (fileName.equals(new File(cFileName).getName())) {
                    result[0] = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID));
                    result[1] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    return result;
                }
            } while (c.moveToNext());
        }
        result[0] = State_No_InList;
        return result;
    }

    private boolean isComplete(String fileName) {
        File targetFile = new File(downloadRoot + fileName);
        return targetFile.exists();
    }
}