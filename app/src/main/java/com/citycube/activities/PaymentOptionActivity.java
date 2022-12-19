package com.citycube.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.citycube.R;
                                     import com.citycube.activities.HandlingServiceActivity;
                                     import com.citycube.activities.HomeActivity;
                                     import com.citycube.databinding.ActivityPaymentOptionBinding;
import com.citycube.model.HandlerModel2;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.retrofit.Constant;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkAvailablity;
import com.citycube.utility.SessionManager;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentOptionActivity extends AppCompatActivity {
     public String TAG = "PaymentOptionActivity";
    ActivityPaymentOptionBinding binding;
    CityCubeUserInterface apiInterface;
    int  LAUNCH_SECOND_ACTIVITY = 1;
    String departLat = "", departLon = "", arrivalLat = "", arrivalLon = "", departureAddress = "", arrivalAddress = "", date = "", time = "", timeZome = ""
            ,selectedVan="",selectedHandler="",totalPrice="",first_name="",last_name="",email="",mobile="",departure_contact_name="",departure_contact_number="",arrival_contact_name=""
            ,arrival_contact_number="",passengers="",transport="",payType="Card", chk="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_option);

        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            selectedVan = getIntent().getStringExtra("selectedVan");
            selectedHandler = getIntent().getStringExtra("selectedHandler");
            departLat = getIntent().getStringExtra("departLat");
            departLon = getIntent().getStringExtra("departLon");
            arrivalLat = getIntent().getStringExtra("arrivalLat");
            arrivalLon = getIntent().getStringExtra("arrivalLon");
            departureAddress = getIntent().getStringExtra("departureAddress");
            arrivalAddress = getIntent().getStringExtra("arrivalAddress");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            timeZome = getIntent().getStringExtra("timeZone");
            totalPrice = getIntent().getStringExtra("totalPrice");
            first_name = getIntent().getStringExtra("first_name");
            last_name = getIntent().getStringExtra("last_name");
            email = getIntent().getStringExtra("email");
            mobile = getIntent().getStringExtra("mobile");
            departure_contact_name = getIntent().getStringExtra("departure_contact_name");
            departure_contact_number = getIntent().getStringExtra("departure_contact_number");
            arrival_contact_name = getIntent().getStringExtra("arrival_contact_name");
            arrival_contact_number = getIntent().getStringExtra("arrival_contact_number");
            passengers = getIntent().getStringExtra("passengers");
            transport = getIntent().getStringExtra("transport");

        }

        binding.tvTotal1.setText("€"+ totalPrice);
        binding.tvDeparture.setText(departureAddress);
        binding.tvArrival.setText(arrivalAddress);
        binding.tvDate.setText(date);
        binding.tvTime.setText(time);
        binding.tvMinutes.setText(HandlingServiceActivity.Progresss1 + " min" + "(" + " included over " + HandlingServiceActivity.perMinPrice + " €/min "+")");


        if(selectedVan.equals("1")){
            binding.ivVehicle.setImageDrawable(getResources().getDrawable(R.drawable.small_unselected));
            binding.tvVehicle.setText("Small : 6m3");
        }

       else if(selectedVan.equals("2")){
            binding.ivVehicle.setImageDrawable(getResources().getDrawable(R.drawable.classic_unselected));
            binding.tvVehicle.setText("Classic : 9m3");
        }
       else if(selectedVan.equals("3")){
            binding.ivVehicle.setImageDrawable(getResources().getDrawable(R.drawable.large_unselected));
            binding.tvVehicle.setText("Large : 12m3");
        }
      else if(selectedVan.equals("4")){
            binding.ivVehicle.setImageDrawable(getResources().getDrawable(R.drawable.jumbo));
            binding.tvVehicle.setText("Jumbo : 20m3");
        }




      binding.rdGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                       switch(checkedId) {
                                                           case R.id.rdCard:
                                                               payType = "Card" ;
                                                               binding.tvPay.setText(getString(R.string.pay));
                                                               break;
                                                           case R.id.rdCash:
                                                               payType = "Cash"  ;
                                                               binding.tvPay.setText(getString(R.string.book_now));

                                                               break;
                                                       }
                                                   }
                                               }
      );




        binding.reservationToolbar.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.reservationToolbar.tvTitle.setText(R.string.order_detail);

        binding.btnPay.setOnClickListener(v -> {
            if(payType.equals("")) Toast.makeText(PaymentOptionActivity.this, getString(R.string.please_select_payment_method), Toast.LENGTH_SHORT).show();
          else {
                if(!SessionManager.readString(PaymentOptionActivity.this, Constant.USER_INFO,"").equals("")) {
                   if(NetworkAvailablity.checkNetworkStatus(PaymentOptionActivity.this)) sendBookingRequest();
                   else Toast.makeText(PaymentOptionActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                }else LogInAlert(); // sendBookingRequest();
            }

        }
        );

    }
    public void sendBookingRequest(){
        DataManager.getInstance().showProgressMessage(PaymentOptionActivity.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("car_type_id",selectedVan);
      //  map.put("current_time",time);
        map.put("pick_lat",departLat);
        map.put("pick_lon",departLon);
        map.put("drop_lat",arrivalLat);
        map.put("drop_lon",arrivalLon);
        map.put("picuplocation",departureAddress);
        map.put("dropofflocation",arrivalAddress);
        map.put("picklaterdate",date);
        map.put("picklatertime",time);
        map.put("timezone",timeZome);
        map.put("user_id",DataManager.getInstance().getUserData(PaymentOptionActivity.this).result.id);
        map.put("handling_id",selectedHandler);
        map.put("loding_time", HandlingServiceActivity.Progresss1+"");
        map.put("first_name",first_name);
        map.put("last_name",last_name);
        map.put("email",email);
        map.put("mobile",mobile);
        map.put("departure_contact_name",departure_contact_name);
        map.put("departure_contact_number",departure_contact_number);
        map.put("arrival_contact_name",arrival_contact_name);
        map.put("arrival_contact_number",arrival_contact_number);
        map.put("passengers",passengers);
        map.put("fare",totalPrice);
        map.put("transport",transport);
        map.put("van_size",binding.tvVehicle.getText().toString());
        map.put("pay_type",payType);
        Log.e(TAG,"Send Booking Service Request "+map);
        Call<Map<String, String>> loginCall = apiInterface.bookingRequest(map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Send Booking Service Response :"+responseString);
                    if(data.get("status").equals("1")){

                        if(payType.equals("Cash")) {

                            App.showToast(PaymentOptionActivity.this, getString(R.string.request_send_successfully), Toast.LENGTH_LONG);
                            startActivity(new Intent(PaymentOptionActivity.this, HomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }

                        else  if(payType.equals("Card")) {
                            startActivity(new Intent(PaymentOptionActivity.this, PayAct.class)
                            .putExtra("request_id",data.get("request_id"))
                            .putExtra("amount",totalPrice+""));

                        }


                    }
                    else if(data.get("status").equals("0")){
                        if(chk.equals("Pay")){
                         SessionManager.clearsession(PaymentOptionActivity.this);
                        }
                        App.showToast(PaymentOptionActivity.this,data.get("message"), Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



    public void LogInAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(PaymentOptionActivity.this);
        builder1.setMessage(getResources().getString(R.string.please_login));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent i = new Intent(PaymentOptionActivity.this, LoginActivity.class);
                        i.putExtra("Check","Pay");
                        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);


                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        // getActivity().startActivity(new Intent(getActivity(), HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //getActivity().finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                chk = data.getStringExtra("chk");
                if(NetworkAvailablity.checkNetworkStatus(PaymentOptionActivity.this)) sendBookingRequest();
                else Toast.makeText(PaymentOptionActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}


