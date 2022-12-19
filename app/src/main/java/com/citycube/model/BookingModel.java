package com.citycube.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
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
        @SerializedName("loding_time")
        @Expose
        public String lodingTime;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("departure_contact_name")
        @Expose
        public String departureContactName;
        @SerializedName("arrival_contact_name")
        @Expose
        public String arrivalContactName;
        @SerializedName("departure_contact_number")
        @Expose
        public String departureContactNumber;
        @SerializedName("arrival_contact_number")
        @Expose
        public String arrivalContactNumber;
        @SerializedName("passengers")
        @Expose
        public String passengers;
        @SerializedName("fare")
        @Expose
        public String fare;
        @SerializedName("delete_time")
        @Expose
        public String deleteTime;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("transport")
        @Expose
        public String transport;
        @SerializedName("van_size")
        @Expose
        public String vanSize;
        @SerializedName("driver_details")
        @Expose
        public DriverDetails driverDetails;

        public class DriverDetails {

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
            @SerializedName("user_name")
            @Expose
            public String userName;


        }

    }

}
