package com.keven.retrofit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;


public class DataService {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static DataService sInstance;
    @Inject ApiClient mApiClient;


    public static DataService getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataService(context);
        }
        return sInstance;
    }

    private DataService(Context context) {
        DaggerApiComponent.builder()
                .constantModule(new ConstantModule(context))
                .build().inject(this);
    }

    private String buildJsonQueryParams(Map<String, String> params) {
        JSONObject resultObject = new JSONObject();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                try {
                    if (!TextUtils.isEmpty(key) && value != null) {
                        try {
                            JSONObject valueJsonObject = new JSONObject(value);
                            resultObject.put(key, valueJsonObject);
                        } catch (JSONException e) {
                            resultObject.put(key, value);
                        }
                    }
                } catch (Exception e) {
                    Log.w("tag", e);
                }
            }
        }
        Log.w("tag", "buildJsonQueryParams resultObject params: " + resultObject.toString());
        return resultObject.toString();
    }


    /* xijinfa api */
    public Observable<Object> login(String data) {
        Log.d("TAG", "params : " + data);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("appPackage", "com.ecarobo.android");
        queryMap.put("versionNum", "0");
        String queryJson = buildJsonQueryParams(queryMap);
        return mApiClient.login(RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON), queryJson));
    }

    /* github api */
    public Observable<UserResponse> getUser(String username) {
        return mApiClient.getUser(username);
    }

    public Observable<ResponseBody> getbaidu() {
        return mApiClient.getbaidu();
    }
}