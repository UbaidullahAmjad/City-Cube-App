package com.citycube.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupModel {

    @SerializedName("result")
    @Expose
    public Result result;
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
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("wallet")
        @Expose
        public String wallet;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("user_name")
        @Expose
        public String userName;

        @SerializedName("phone_code")
        @Expose
        public String phoneCode;



    }

}
