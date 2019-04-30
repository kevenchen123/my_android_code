package com.keven.retrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiClient {
    /* xijinfa api */
    @Headers({
                 "Content-Type: application/json", "Accept: application/json",
             })
    @POST("common/app/version/checkappversion")
    Observable<Object> login(@Body RequestBody body);

    /* github api */
    @GET("/users/{username}")
    Observable<UserResponse> getUser(@Path("username") String username);

    @GET("http://www.baidu.com")
    Observable<ResponseBody> getbaidu();
}