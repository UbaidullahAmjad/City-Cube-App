package com.citycube.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentModel {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("payment_method")
        @Expose
        public String paymentMethod;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("request_id")
        @Expose
        public String requestId;
        @SerializedName("provider_id")
        @Expose
        public String providerId;
        @SerializedName("date")
        @Expose
        public String date;



    }

}
