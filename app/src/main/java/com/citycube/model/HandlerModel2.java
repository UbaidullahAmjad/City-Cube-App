package com.citycube.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HandlerModel2 {

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
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("handler_msg")
        @Expose
        public String handlerMsg;
        @SerializedName("car_type_id")
        @Expose
        public String carTypeId;
        @SerializedName("chk")
        @Expose
        public boolean chk;
        @SerializedName("handling_price")
        @Expose
        public String handlingPrice;

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }

}


