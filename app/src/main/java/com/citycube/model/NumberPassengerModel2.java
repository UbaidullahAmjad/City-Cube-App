package com.citycube.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberPassengerModel2 {

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
        @SerializedName("passenger")
        @Expose
        public String passenger;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("status")
        @Expose
        public String status;

        @SerializedName("chk")
        @Expose
        public boolean chk =false;

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }

}

