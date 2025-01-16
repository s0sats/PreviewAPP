package com.namoadigital.prj001.core.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BroadcastHelper {

    public static <WBR_CLASS extends WakefulBroadcastReceiver> void sendToWebServiceReceiver(
            Context context,
            Class<WBR_CLASS> receiverClass,
            NetworkReceiverCallback networkReceiver
    ) {
        Intent intent = new Intent(context, receiverClass);
        if (networkReceiver != null) {
            Bundle extras = networkReceiver.getExtras();
            if (extras != null) {
                intent.putExtras(extras);
            }
        }
        context.sendBroadcast(intent);
    }

    public interface NetworkReceiverCallback {
        Bundle getExtras();
    }

    public static <T> Bundle putApiRequest(Bundle bundle, T apiRequest, Class<T> clazz) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(apiRequest, clazz);
        bundle.putString(clazz.getName(), json);
        return bundle;
    }

    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        Gson gson = new Gson();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, type);
    }
}
