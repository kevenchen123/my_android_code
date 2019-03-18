package com.keven.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import com.keven.retrofit.UserResponse;
import com.keven.retrofit.UserResponse.Contact.Phone;

/**
 * json字符串与对象相互转换
 */
public class JsonHelp {

    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        return getGson().fromJson(jsonStr, objClass);
    }

    public static <T> T json2Bean(String jsonStr) {
        return getGson().fromJson(jsonStr, new TypeToken<T>() { }.getType());
    }

    public static <T> T json2Bean(String jsonStr, Type objClass) {
        return getGson().fromJson(jsonStr, objClass);
    }

    public static String toJson(Object obj) {
        String reString = getGson().toJson(obj);
        return reString;
    }


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // change serialization for specific types
        JsonDeserializer<UserResponse> deserializer1 = new MyJsonDeserializer<UserResponse>(UserResponse.class) {
            @Override
            public UserResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return super.deserialize(json, typeOfT, context);
            }
            @Override
            public boolean custom(String jsonKey, JsonElement je, UserResponse response) {
                if (jsonKey.equals("name")) {
                    if (je.isJsonPrimitive()) {
                        response.name = je.getAsString() + "@";
                    }
                    return true;
                } else {
                    return false;
                }
            }
        };
        gsonBuilder.registerTypeAdapter(UserResponse.class, deserializer1);

        JsonDeserializer<Phone> deserializer2 = new JsonDeserializer<Phone>() {
            @Override
            public Phone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                Phone phone = new Phone();
                phone.number = new ArrayList<>();
                for (JsonElement je : jsonObject.get("number").getAsJsonArray()) {
                    phone.number.add("$" + String.valueOf(je.getAsLong()));
                }
                return phone;
            }
        };
        gsonBuilder.registerTypeAdapter(Phone.class, deserializer2);

        Gson customGson = gsonBuilder.create();
        return customGson;
    }


    public static class MyJsonDeserializer<T> implements JsonDeserializer<T> {
        Class<T> typeParameterClass;

        public MyJsonDeserializer(Class<T> typeParameterClass) {
            this.typeParameterClass = typeParameterClass;
        }

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                JsonObject jsonObject = json.getAsJsonObject();
                T response = typeParameterClass.newInstance();

                for (Map.Entry<String, JsonElement> mapset : jsonObject.entrySet()) {
                    JsonElement je = mapset.getValue();
                    if (custom(mapset.getKey(), je, response)) continue;

                    Field field = typeParameterClass.getField(mapset.getKey());
                    Log.e("TAG", "deserialize : " + mapset.getKey() + "     " + je.getClass() + "     " + field.getType());

                    if (je.isJsonPrimitive()) {
                        if (je.getAsJsonPrimitive().isString()) {
                            field.set(response, je.getAsString());
                        }
                        if (je.getAsJsonPrimitive().isNumber()) {
                            Object object = null;
                            if (field.getType() == String.class) object = String.valueOf(je.getAsNumber());
                            if (field.getType() == int.class) object = je.getAsInt();
                            field.set(response, object);
                        }
                    }
                    if (je.isJsonObject()) {
                        field.set(response, JsonHelp.json2Bean(je.toString(), field.getType()));
                    }
                    if (je.isJsonArray()) {
                        ParameterizedType pt = (ParameterizedType) field.getGenericType();
                        field.set(response, JsonHelp.json2Bean(je.toString(), TypeToken.getParameterized(List.class, pt.getActualTypeArguments()[0]).getType()));
                    }
                }
                return response;
            } catch (Exception e) {
                Log.e("TAG", "" + e);
                return null;
            }
        }

        public boolean custom(String jsonKey, JsonElement je, T response) {
            return false;
        }
    }
}