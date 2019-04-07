package com.json.motionmonitoring.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.json.motionmonitoring.model.Device;
import com.json.motionmonitoring.model.User;

import java.lang.reflect.Type;
import java.util.Date;

public class Utility {

    public static String handleValidateResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            return response;
        }
        return "";
    }

    public static User handleSelUserResponse(String response) {
        GsonBuilder builder = new GsonBuilder();
        if (!TextUtils.isEmpty(response)){
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            User user = gson.fromJson(response, User.class);
            Log.d("RegisterAcitvity", user.getUser_name());
            Log.d("RegisterAcitvity", user.getPassword());
            return user;
        }
        return null;
    }

    public static Device handleSelDevResponse(String response) {
        if (!TextUtils.isEmpty(response)){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });

            Gson gson = builder.create();
            Device device = gson.fromJson(response, Device.class);
            Log.d("RegisterAcitvity", String.valueOf(device.getId()));
            Log.d("RegisterAcitvity", String.valueOf(device.getUser_id()));
            return device;
        }
        return null;
    }
}
