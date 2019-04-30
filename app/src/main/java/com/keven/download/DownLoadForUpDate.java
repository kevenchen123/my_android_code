package com.keven.download;

import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 下载文件的工具
 */
public class DownLoadForUpDate {
    protected String d = (UUID.randomUUID() + ".apk");
    protected String e = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");

    public void downLoadFile(String downloadurl, ProgressResponseBody.ProgressResponseListener progressListener, final DownLoadCallBack callBack) {
        OkHttpClient clone = addProgressResponseListener(progressListener);
        Request request = new Request.Builder().url(downloadurl).build();
        clone.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    File file = new File(e);
                    if (file.mkdirs() || file.exists()) {
                        File file2 = new File(file, d);
                        InputStream bufferedInputStream = new ByteArrayInputStream(response.body().bytes());
                        OutputStream fileOutputStream = new FileOutputStream(file2);

                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = bufferedInputStream.read(bArr);
                            if (read != -1) {
                                fileOutputStream.write(bArr, 0, read);
                            } else {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                bufferedInputStream.close();
                                callBack.onsuccess(file2);
                                return;
                            }
                        }
                    }
                } catch (IOException e1) {
                    //e1.printStackTrace();
                    callBack.onfaile(e1.getMessage());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onfaile("网络异常");
            }
        });
    }

    public static OkHttpClient addProgressResponseListener(final ProgressResponseBody.ProgressResponseListener progressListener) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 拦截
                Response originalResponse = chain.proceed(chain.request());
                // 包装响应体并返回
                return originalResponse
                        .newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        };
        // 克隆
        OkHttpClient mOkHttpClient = (new OkHttpClient.Builder()).
                connectTimeout(15, TimeUnit.SECONDS).
                readTimeout(15, TimeUnit.SECONDS).build();
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        //添加拦截器
        builder.networkInterceptors().add(interceptor);
        OkHttpClient clone = builder.build();
        return clone;
    }

    public interface DownLoadCallBack {
        void onsuccess(File file);
        void onfaile(String errMsg);
    }
}