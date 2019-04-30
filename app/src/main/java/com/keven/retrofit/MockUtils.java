package com.keven.retrofit;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class MockUtils {

    public static String getMockResponse(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            JSONObject JsonObject = new JSONObject(convertStreamToString(inputStream));
            return JsonObject.getJSONObject("Body").toString();
        } catch (Exception e) {
            Log.e("TAG", "okhttp mockInterceptor  Exception=" + e);
            return "";
        }
    }

    private static String convertStreamToString(InputStream inputStream) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            inputStream.close();
        }
        String text = writer.toString();
        return text;
    }
}