package com.citycube.retrofit;




import com.citycube.model.BookingDetailModel;
import com.citycube.model.BookingModel;
import com.citycube.model.GetPriceModel;
import com.citycube.model.HandlerModel2;
import com.citycube.model.NumberPassengerModel;
import com.citycube.model.NumberPassengerModel2;
import com.citycube.model.PaymentModel;
import com.citycube.model.SignupModel;
import com.citycube.model.VanListModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CityCubeUserInterface {



  /*  @Multipart
    @POST("signup")
    Call<SignupModel> signupUser(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody country_code,
            @Part("password") RequestBody password,
            @Part("register_id") RequestBody register_id,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part file);*/






    @FormUrlEncoded
    @POST("signup")
    Call<SignupModel> signupUser(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("login")
    Call<SignupModel> userLogin (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("forgot_password")
    Call<Map<String, String>> forgotPass (@FieldMap Map<String, String> params);




    @Multipart
    @POST("update_profile")
    Call<SignupModel> profileUpdate(
            @Part("user_name") RequestBody username,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("phone_code") RequestBody country_code,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("change_password")
    Call<Map<String, String>> changePassword(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("logout")
    Call<Map<String, String>> logout(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("car_type_list")
    Call<VanListModel> getVanList(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_handling")
    Call<HandlerModel2> getHandlerService(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("booking_request")
    Call<Map<String, String>>bookingRequest(@FieldMap Map<String, String> params);


    /*@FormUrlEncoded
    @POST("get_current_booking_user")
    Call<BookingModel> getBookingStatus(@FieldMap Map<String, String> params);*/


    @FormUrlEncoded
    @POST("get_running_booking_user")
    Call<BookingModel> getBookingStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_booking_details")
    Call<BookingDetailModel> bookingDetails(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("chat_notification")
    Call<Map<String, String>> sendPushNotification(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_chat_count")
    Call<Map<String, String>> getChatCount(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("reset_chat_count")
    Call<Map<String, String>> resetChatCount(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("add_user_rating_review")
    Call<Map<String, String>> addRate(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_price_list")
    Call<GetPriceModel> getPrice(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("stripe_payment")
    Call<PaymentModel> payment(@FieldMap Map<String, String> params);



    @GET("passenger_list")
    Call<NumberPassengerModel2> getPassengerList();




// https://city-cube.fr/webservice/car_list

    // https://city-cube.fr/webservice/get_handling?car_type_id=1

}
