package com.citycube.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.citycube.activities.SplashActivity;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SessionManager {
    public static String TAG = "SessionManager";
    public static final String PREF_NAME = "CubeCityUser";
    public static final String USER_INFO = "userinfo";


    public static final int MODE = Context.MODE_PRIVATE;


    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeBoolean(Context context, String key, Boolean value) {
        getEditor(context).putBoolean(key, value).commit();

    }

    public static Boolean readBoolean(Context context, String key,
                                      Boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void clear(final Context context, String user_id) {
        Logout(user_id,context);

     /*   getEditor(context).clear().commit();
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);*/
    }



    public static void Logout(String id, Context context) {
        CityCubeUserInterface apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_id",id);
        Log.e(TAG,"User Logout Request "+map);
        Call<Map<String, String>> loginCall = apiInterface.logout(map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"User Logout Response"+responseString);
                    if(data.get("status").equals("1")){
                        getEditor(context).clear().commit();
                        DataManager.updateResources(context,"en");
                        Intent intent = new Intent(context, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                    }
                    else if(data.get("status").equals("0")){
                        App.showToast(context,"data not available", Toast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public static void clearsession(final Context context) {
        getEditor(context).clear().commit();
        DataManager.updateResources(context,"en");
    }
}
