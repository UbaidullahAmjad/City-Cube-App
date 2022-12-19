package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.databinding.ActivityForgotPasswordBinding;
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

import static com.citycube.retrofit.Constant.emailPattern;

public class ForgotPasswordActivity extends AppCompatActivity implements NetworkReceiver.ConnectivityReceiverListener {
    public static String TAG = "ForgotPasswordActivity";
    ActivityForgotPasswordBinding binding;
    CityCubeUserInterface apiInterface;
    NetworkReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        initView();
    }

    private void initView() {
        receiver = new NetworkReceiver();
        App.getInstance().setConnectivityListener(ForgotPasswordActivity.this);

        binding.ivBack.setOnClickListener(v -> {finish();});

        binding.btnSend.setOnClickListener(v -> { validation(); });
    }

    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else {
            if(NetworkReceiver.isConnected()) forGotPass(); else App.showToast(ForgotPasswordActivity.this,getString(R.string.network_failure), Toast.LENGTH_SHORT);
        }
    }


    private void forGotPass() {
        DataManager.getInstance().showProgressMessage(ForgotPasswordActivity.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",binding.etEmail.getText().toString());
        Log.e(TAG,"ForgotPass Request :"+map.toString());
        Call<Map<String,String>> signupCall = apiInterface.forgotPass(map);
        signupCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"ForgotPass Response :"+responseString);
                    if(data.get("status").equals("1")){
                        App.showToast(ForgotPasswordActivity.this,getString(R.string.link_send_successfully_your_email),Toast.LENGTH_SHORT);
                        finish(); }
                    else if(data.get("status").equals("0")){
                        App.showToast(ForgotPasswordActivity.this,data.get("result"),Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, App.intentFilter);
        if (NetworkReceiver.isConnected()) {
            App.showSnack(this, findViewById(R.id.forgotPass), true);
        } else {
            App.showSnack(this, findViewById(R.id.forgotPass), false);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("isConnected", "isConnected=$isConnected");
        App.showSnack(this, findViewById(R.id.forgotPass), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.forgotPass), true);
    }
}