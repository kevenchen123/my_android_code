package com.keven.retrofit;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiClient {

    @Headers({
                 "Content-Type: application/json", "Accept: application/json",
             })
    @POST("common/app/version/checkappversion")
    Observable<Object> login(@Body RequestBody body);
}