package com.citycube.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VanListModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public VanListModel() {
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("car_name")
        @Expose
        public String carName;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("vehicle_size")
        @Expose
        public String vehicleSize;
        @SerializedName("weight")
        @Expose
        public String weight;
        @SerializedName("vehicle_image")
        @Expose
        public String vehicleImage;
        @SerializedName("charge")
        @Expose
        public String charge;
        @SerializedName("per_min")
        @Expose
        public String perMin;
        @SerializedName("distance")
        @Expose
        public String distance;
        @SerializedName("miles")
        @Expose
        public String miles;



    }

}

