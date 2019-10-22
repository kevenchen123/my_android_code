package com.keven.socket;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;


public class MessageJson {

    private String type;
    private String room;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -------------------------------------------------------------

    public static class Builder {
        JSONObject resultObject = new JSONObject();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder put(String key, String data) {
            try {
                if (data != null) {
                    resultObject.put(key, data);
                }
            } catch (JSONException e) {
                Log.w("MessageJson", e);
            }
            return this;
        }

        public Builder put(String key, long data) {
            try {
                resultObject.put(key, data);
            } catch (JSONException e) {
                Log.w("MessageJson", e);
            }
            return this;
        }

        public Builder put(String key, JSONObject data) {
            try {
                if (data != null) {
                    resultObject.put(key, data);
                }
            } catch (JSONException e) {
                Log.w("MessageJson", e);
            }
            return this;
        }

        public JSONObject build() {
            return resultObject;
        }
    }
}