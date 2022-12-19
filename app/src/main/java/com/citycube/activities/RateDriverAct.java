package com.citycube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.citycube.R;
import com.citycube.databinding.ActivityRateDriverBinding;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkReceiver;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateDriverAct extends AppCompatActivity {
    ActivityRateDriverBinding binding;
    CityCubeUserInterface apiInterface;
    String requestId="",providerId="",providerName="",providerImage="";
    float rat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rate_driver);
        initViews();
    }

    private void initViews() {


        if(getIntent()!=null){
            providerId = getIntent().getStringExtra("provider_id");
            requestId = getIntent().getStringExtra("requestId");
            providerName = getIntent().getStringExtra("providerName");
            providerImage = getIntent().getStringExtra("providerImage");
            binding.tvName.setText(providerName);
            Glide.with(getApplicationContext())
                    .load(providerImage)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .override(80,80)
                    .into(binding.ivPic);
        }


        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rat =  rating;
            }
        });

        binding.btnSubmit1.setOnClickListener(v -> {
            if(NetworkReceiver.isConnected()) giveRate();
            else App.showToast(RateDriverAct.this,getString(R.string.network_failure),Toast.LENGTH_LONG);
        });
    }

    private void giveRate() {
        DataManager.getInstance().showProgressMessage(RateDriverAct.this, "Please wait...");
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(RateDriverAct.this).result.id);
        map.put("driver_id",providerId);
        map.put("request_id",requestId);
        map.put("rating", String.valueOf(rat));
       // map.put("date",DataManager.getInstance().currentDate());
        map.put("review",binding.edFeedback.getText().toString());
        Log.e("MapMap", "RATE REQUEST" + map);
        Call<Map<String, String>> payCall = apiInterface.addRate( map);
        payCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "RATE RESPONSE" + dataResponse);
                            Toast.makeText(RateDriverAct.this, getString(R.string.rate_successful), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RateDriverAct.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(RateDriverAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
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


}
