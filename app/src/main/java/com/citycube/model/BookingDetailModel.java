package com.citycube.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingDetailModel {

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
        @SerializedName("driver_id")
        @Expose
        public String driverId;
        @SerializedName("picuplocation")
        @Expose
        public String picuplocation;
        @SerializedName("dropofflocation")
        @Expose
        public String dropofflocation;
        @SerializedName("picuplat")
        @Expose
        public String picuplat;
        @SerializedName("pickuplon")
        @Expose
        public String pickuplon;
        @SerializedName("droplat")
        @Expose
        public String droplat;
        @SerializedName("droplon")
        @Expose
        public String droplon;
        @SerializedName("booktype")
        @Expose
        public String booktype;
        @SerializedName("picklaterdate")
        @Expose
        public String picklaterdate;
        @SerializedName("picklatertime")
        @Expose
        public String picklatertime;
        @SerializedName("car_type_id")
        @Expose
        public String carTypeId;
        @SerializedName("timezone")
        @Expose
        public String timezone;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("req_datetime")
        @Expose
        public String reqDatetime;
        @SerializedName("estimate_charge_amount")
        @Expose
        public String estimateChargeAmount;
        @SerializedName("send_drivers")
        @Expose
        public String sendDrivers;
        @SerializedName("start_time")
        @Expose
        public String startTime;
        @SerializedName("end_time")
        @Expose
        public String endTime;
        @SerializedName("shareride_type")
        @Expose
        public Object sharerideType;
        @SerializedName("car_seats")
        @Expose
        public String carSeats;
        @SerializedName("passenger")
        @Expose
        public Object passenger;
        @SerializedName("booked_seats")
        @Expose
        public Object bookedSeats;
        @SerializedName("route_img")
        @Expose
        public Object routeImg;
        @SerializedName("wt_start_time")
        @Expose
        public Object wtStartTime;
        @SerializedName("wt_end_time")
        @Expose
        public Object wtEndTime;
        @SerializedName("accept_time")
        @Expose
        public Object acceptTime;
        @SerializedName("waiting_status")
        @Expose
        public String waitingStatus;
        @SerializedName("waiting_cnt")
        @Expose
        public Object waitingCnt;
        @SerializedName("reason_id")
        @Expose
        public Object reasonId;
        @SerializedName("card_id")
        @Expose
        public Object cardId;
        @SerializedName("apply_code")
        @Expose
        public Object applyCode;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("favorite_ride")
        @Expose
        public String favoriteRide;
        @SerializedName("user_rating_status")
        @Expose
        public String userRatingStatus;
        @SerializedName("tip_amount")
        @Expose
        public String tipAmount;

        @SerializedName("user_details")
        @Expose
        public UserDetails userDetails;

        @SerializedName("driver_details")
        @Expose
        public DriverDetails driverDetails;



        public class UserDetails {
            @SerializedName("id")
            @Expose
            public String id;

            @SerializedName("car_type_id")
            @Expose
            public String carTypeId;

            @SerializedName("type")
            @Expose
            public String type;

            @SerializedName("user_name")
            @Expose
            public String userName;

            @SerializedName("last_name")
            @Expose
            public String lastLame;

            @SerializedName("phone_code")
            @Expose
            public String phoneCode;

            @SerializedName("mobile")
            @Expose
            public String mobile;


            @SerializedName("email")
            @Expose
            public String email;

            @SerializedName("image")
            @Expose
            public String image;

            @SerializedName("user_image")
            @Expose
            public String userImage;




        }


        public class DriverDetails {
            @SerializedName("id")
            @Expose
            public String id;

            @SerializedName("car_type_id")
            @Expose
            public String carTypeId;

            @SerializedName("type")
            @Expose
            public String type;

            @SerializedName("user_name")
            @Expose
            public String userName;

            @SerializedName("last_name")
            @Expose
            public String lastLame;

            @SerializedName("phone_code")
            @Expose
            public String phoneCode;

            @SerializedName("mobile")
            @Expose
            public String mobile;


            @SerializedName("email")
            @Expose
            public String email;

            @SerializedName("driver_image")
            @Expose
            public String driverImage;

        }

    }

}

