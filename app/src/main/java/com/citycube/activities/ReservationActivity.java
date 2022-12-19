package com.citycube.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.databinding.ActivityReservationBinding;
import com.citycube.map.BaseClass;
import com.citycube.map.DataParser2;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class ReservationActivity extends AppCompatActivity {
    ActivityReservationBinding binding;
    public static String date="",time="",addPercent="0";
    String departLat="",departLon="",arrivalLat="",arrivalLon="",departureAddress="",arrivalAddress="",distance="";
    CityCubeUserInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_reservation);
        initViews();

    }

    private void initViews() {
        if(getIntent()!=null){
            departLat = getIntent().getStringExtra("departLat");
            departLon = getIntent().getStringExtra("departLon");
            arrivalLat = getIntent().getStringExtra("arrivalLat");
            arrivalLon = getIntent().getStringExtra("arrivalLon");
            departureAddress = getIntent().getStringExtra("departureAddress");
            arrivalAddress = getIntent().getStringExtra("arrivalAddress");
            getKm();
        }

        binding.reservationToolbar.tvTitle.setText(R.string.reversation);
        binding.reservationToolbar.ivBack.setOnClickListener(v -> { finish(); });

        binding.tvDate.setOnClickListener(v -> { DataManager.DatePicker(ReservationActivity.this,binding.tvDate::setText);});
        binding.tvTime.setOnClickListener(v -> {
            if(!date.equals(""))DataManager.TimePicker(ReservationActivity.this,binding.tvTime::setText);
            else Toast.makeText(this, getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
        });


        binding.btnContinue.setOnClickListener(v -> {

            if(date.equals("")){
                App.showToast(ReservationActivity.this,getString(R.string.please_select_date), Toast.LENGTH_LONG);
            }

            else if(time.equals("")){
                App.showToast(ReservationActivity.this,getString(R.string.please_select_time), Toast.LENGTH_LONG);
            }
            else {
                startActivity(new Intent(ReservationActivity.this,AddVehicleActivity.class)
                        .putExtra("departLat", departLat)
                        .putExtra("departLon", departLon).putExtra("arrivalLat", arrivalLat)
                        .putExtra("arrivalLon", arrivalLon + "").putExtra("departureAddress", departureAddress)
                        .putExtra("arrivalAddress", arrivalAddress)
                        .putExtra("date",date).putExtra("time",time)
                        .putExtra("timeZone", TimeZone.getDefault().getID() +"")
                        .putExtra("percent",addPercent));


            }


            });
    }


    public void getKm(){
        String URL= BaseClass.get().getPolyLineUrl(ReservationActivity.this,new LatLng(Double.parseDouble(departLat),Double.parseDouble(departLon)),new LatLng(Double.parseDouble(arrivalLat),Double.parseDouble(arrivalLon)));
        ApiCallBuilder.build(ReservationActivity.this)
                .setUrl(URL).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    DataParser2 parser = new DataParser2();
                    distance  = parser.parse2(object);
                    Log.e("Distance===",distance);
                    if(Double.parseDouble(distance)>=500) AlertTiming();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failed(String error) {

            }
        });
    }

    private void AlertTiming() {
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ReservationActivity.this);
        builder1.setMessage(getResources().getString(R.string.note5));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                       // finish();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}