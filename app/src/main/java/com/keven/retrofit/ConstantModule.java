package com.keven.retrofit;

import android.content.Context;

import com.keven.BuildConfig;
import com.keven.R;
import com.keven.utils.NetworkUtils;

import java.io.File;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

/**
 * Created by Mohsen on 20/10/2016.
 */

@Module
public class ConstantModule {
    private Context context;

    public ConstantModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    @Named("isDebug")
    boolean provideIsDebug() {
        return BuildConfig.DEBUG;
    }

    @Provides
    @Singleton
    @Named("networkTimeoutInSeconds")
    int provideNetworkTimeoutInSeconds() {
        return 60;
    }

    @Provides
    @Singleton
    HttpUrl provideEndpoint(Context context) {
        return HttpUrl.parse(context.getString(R.string.endpoint));
    }

    @Provides
    @Singleton
    @Named("cacheSize")
    long provideCacheSize() {
        return 10 * 1024 * 1024; // 10 MB
    }

    @Provides
    @Singleton
    @Named("cacheMaxAge")
    int provideCacheMaxAgeMinutes() {
        return 2; // 2 min;
    }

    @Provides
    @Singleton
    @Named("cacheMaxStale")
    int provideCacheMaxStaleDays() {
        return 7; // 7 day;
    }

    @Provides
    @Singleton
    @Named("cacheDir")
    File provideCacheDir(Context context) {
        return context.getCacheDir();
    }

    @Provides
    @Named("isConnect")
    boolean provideIsConnect(Context context) {
        return NetworkUtils.isConnected(context);
    }

    @Provides
    @Named("mockFile")
    String provideMockFile() {
        return "userResponse.txt";
    }
}