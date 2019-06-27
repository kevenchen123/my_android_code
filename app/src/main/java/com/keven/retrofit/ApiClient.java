package com.keven.retrofit;

import com.keven.retrofit.model.POIbean;
import com.keven.retrofit.model.UserResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ApiClient {
    @Headers({
                 "Content-Type: application/json", "Accept: application/json",
             })
    @POST("common/app/version/checkappversion")
    Observable<Object> login(@Body RequestBody body);

    /* github api */
    @GET("/users/{username}")
    Observable<UserResponse> getUser(@Path("username") String username);

    @GET("http://www.baidu.com")
    Observable<ResponseBody> getBaidu();

    @GET("https://restapi.amap.com/v3/place/text")
    Observable<POIbean> getAmapLocation(@QueryMap Map<String, String> options);
}