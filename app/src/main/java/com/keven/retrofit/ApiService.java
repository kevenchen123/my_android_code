package com.keven.retrofit;

import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiService  {

    private Retrofit mRetrofitWithGsonAndRxJava;
    private String mHost;

    public ApiService() {
        mHost = "http://10.0.6.31:9100/";
    }

    public ApiService(String host) {
        mHost = host;
    }


    public Retrofit getRetrofitWithGsonAndRxJava() {
        if (mRetrofitWithGsonAndRxJava == null) {
            rebuildClient();
        }
        return mRetrofitWithGsonAndRxJava;
    }

    public ApiClient getApiClient() {
        Retrofit retrofit = getRetrofitWithGsonAndRxJava();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        return apiClient;
    }

    public void setHost(String newHost) {
        if (mHost == null || !newHost.contentEquals(mHost)) {
            mHost = newHost;
            rebuildClient();
        }
    }

    private void rebuildClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging).build();

        mRetrofitWithGsonAndRxJava = new Retrofit.Builder().addCallAdapterFactory(
            RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            .baseUrl(mHost)
            .client(httpClient)
            .build();
    }
}