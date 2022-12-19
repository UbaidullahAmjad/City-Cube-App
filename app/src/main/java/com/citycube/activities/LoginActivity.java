package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.databinding.ActivityLoginBinding;
import com.citycube.model.SignupModel;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.retrofit.Constant;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkReceiver;
import com.citycube.utility.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.citycube.retrofit.Constant.emailPattern;

public class LoginActivity extends AppCompatActivity {
    public String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    CityCubeUserInterface apiInterface;
    String refreshedToken="",Check="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        initViews();
    }

    private void initViews() {

        if(getIntent()!=null){
            Check = getIntent().getStringExtra("Check");
            Log.e("Check=====",Check);
        }


        binding.tvDontHave.setOnClickListener(v -> { startActivity(new Intent(LoginActivity.this,SignupActivity.class)); });

        binding.btnSignIn.setOnClickListener(v -> { validation(); });

        binding.btnForgotPass.setOnClickListener(v -> { startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));  });

        binding.btnEnglish.setOnClickListener(v -> {
            DataManager.updateResources(LoginActivity.this,"en");
            SessionManager.writeString(LoginActivity.this, Constant.LANGUAGE,"en");
            resetartActivity();
        });

        binding.btnFranch.setOnClickListener(v -> {
            DataManager.updateResources(LoginActivity.this,"fr");
            SessionManager.writeString(LoginActivity.this,Constant.LANGUAGE,"fr");
            resetartActivity();
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===",refreshedToken);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else if (binding.etPass.getText().toString().equals("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            binding.etPass.setFocusable(true);
        }
        else {
            if (NetworkReceiver.isConnected()) {
                userLogin();
            } else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }


        }
    }




    public void userLogin(){
        DataManager.getInstance().showProgressMessage(LoginActivity.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",binding.etEmail.getText().toString());
        map.put("password",binding.etPass.getText().toString());
        map.put("type","USER");
        map.put("register_id",refreshedToken);
        Log.e(TAG,"Login Request "+map);
        Call<SignupModel> loginCall = apiInterface.userLogin(map);
        loginCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Login Response :"+responseString);
                    if(data.status.equals("1")){
                        SessionManager.writeString(LoginActivity.this, Constant.USER_INFO, responseString);
                        Toast.makeText(LoginActivity.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show();
                        if(Check.equals("Pay")){
                           Intent returnIntent = new Intent();
                           returnIntent.putExtra("chk",Check);
                           setResult(Activity.RESULT_OK,returnIntent);
                           finish();
                       }else {
                           startActivity(new Intent(LoginActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                           finish();
                       }


                    }
                    else if(data.status.equals("0")){
                        App.showToast(LoginActivity.this,data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void resetartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}