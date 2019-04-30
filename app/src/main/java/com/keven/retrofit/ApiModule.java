package com.keven.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.keven.utils.JsonHelp;

import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohsen on 20/10/2016.
 */
@Module
public class ApiModule {
    private static final String CACHE_CONTROL = "Cache-Control";

    @Provides
    @Singleton
    public ApiClient provideMarvelApiService(Retrofit retrofit) {
        return retrofit.create(ApiClient.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(HttpUrl baseUrl, Converter.Factory converterFactory, CallAdapter.Factory callAdapterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public Converter.Factory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return JsonHelp.getGson();
    }

    @Provides
    @Singleton
    public CallAdapter.Factory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }


    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor,
                                            @Named("networkTimeoutInSeconds") int networkTimeoutInSeconds,
                                            @Named("isDebug") boolean isDebug,
                                            Cache cache,
                                            @Named("cacheInterceptor") Interceptor cacheInterceptor,
                                            @Named("offlineInterceptor") Interceptor offlineCacheInterceptor,
                                            @Named("mockInterceptor") Interceptor mockInterceptor) {

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(offlineCacheInterceptor)
                .cache(cache)
                .connectTimeout(networkTimeoutInSeconds, TimeUnit.SECONDS)
                .writeTimeout(networkTimeoutInSeconds, TimeUnit.SECONDS)
                .readTimeout(networkTimeoutInSeconds, TimeUnit.SECONDS);

        //show logs if app is in Debug mode
        if (isDebug) {
            okHttpClient.addInterceptor(loggingInterceptor);
            okHttpClient.addInterceptor(mockInterceptor);
        }
        return okHttpClient.build();
    }

    @Singleton
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    @Singleton
    public Cache provideCache(@Named("cacheDir") File cacheDir, @Named("cacheSize") long cacheSize) {
        Cache cache = null;
        try {
            cache = new Cache(new File(cacheDir.getPath(), "http-cache"), cacheSize);
            Log.e("TAG", "Cache="+cache.directory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    @Singleton
    @Provides
    @Named("cacheInterceptor")
    public Interceptor provideCacheInterceptor(@Named("cacheMaxAge") int maxAgeMin) {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(maxAgeMin, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

    @Singleton
    @Provides
    @Named("offlineInterceptor")
    public Interceptor provideOfflineCacheInterceptor(@Named("isConnect") boolean isConnect, @Named("cacheMaxStale") int maxStaleDay) {
        return chain -> {
            Request request = chain.request();
            Log.e("TAG", "isConnect="+isConnect);
            if (!isConnect) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(maxStaleDay, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            return chain.proceed(request);
        };
    }

    @Singleton
    @Provides
    @Named("mockInterceptor")
    public Interceptor provideMockInterceptor(Context context, @Named("mockFile") String mockFile) {
        return chain -> {
            // delay for test
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), MockUtils.getMockResponse(context, mockFile)))
                    .build();
        };
    }
}