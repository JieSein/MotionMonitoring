package com.json.motionmonitoring.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.json.motionmonitoring.model.Motion_Device;
import com.json.motionmonitoring.model.Motion_User;

import java.lang.reflect.Type;
import java.util.Date;

public class Utility {

    public static String handleValidateResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            return response;
        }
        return "";
    }

    public static Motion_User handleSelUserResponse(String response) {
        GsonBuilder builder = new GsonBuilder();
        if (!TextUtils.isEmpty(response)){
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Motion_User user = gson.fromJson(response, Motion_User.class);
            Log.d("RegisterAcitvity", user.getUser_name());
            Log.d("RegisterAcitvity", user.getPassword());
            return user;
        }
        return null;
    }

    public static Motion_Device handleSelDevResponse(String response) {
        if (!TextUtils.isEmpty(response)){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });

            Gson gson = builder.create();
            Motion_Device device = gson.fromJson(response, Motion_Device.class);
            Log.d("RegisterAcitvity", String.valueOf(device.getId()));
            Log.d("RegisterAcitvity", String.valueOf(device.getUser_id()));
            return device;
        }
        return null;
    }
}
