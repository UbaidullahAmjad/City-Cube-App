package com.citycube.utility;

import android.content.Context;
import android.util.Log;

import com.citycube.R;
import com.google.android.gms.maps.model.LatLng;


public class BaseClass {

    public static BaseClass get() {
        return new BaseClass();
    }


    public String getPolyLineUrl(Context context, LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + context.getResources().getString(R.string.place_api_key);
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("PathURL","====>"+url);
        return url;
    }

}
