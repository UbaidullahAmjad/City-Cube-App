package com.citycube.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPriceModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("start_km")
        @Expose
        public String startKm;
        @SerializedName("end_km")
        @Expose
        public String endKm;
        @SerializedName("6m3")
        @Expose
        public String _6m3;
        @SerializedName("9m3")
        @Expose
        public String _9m3;
        @SerializedName("12_14m3")
        @Expose
        public String _1214m3;
        @SerializedName("20m3")
        @Expose
        public String _20m3;
        @SerializedName("helper_status")
        @Expose
        public String helperStatus;
        @SerializedName("date_time")
        @Expose
        public String dateTime;



    }

}

