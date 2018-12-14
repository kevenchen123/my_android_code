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
import rx.Observable;


public class DataService {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String PLATFORM = "android";

    private static DataService sInstance;

    private String mHost;
    private ApiService mApiService;
    private Context mContext;

    private DataService(Context context) {
        mContext = context;
    }

    public static DataService getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataService(context);
        }
        return sInstance;
    }

    public ApiService getApiService() {
        if (mApiService == null) {
            mApiService = new ApiService();
        }
        return mApiService;
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

    public Observable<Object> login(String data) {
        Log.d("TAG", "params : " + data);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("appPackage", "com.ecarobo.android");
        queryMap.put("versionNum", "0");
        String queryJson = buildJsonQueryParams(queryMap);
        return getApiService().getApiClient()
            .login(RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON), queryJson));
    }
}