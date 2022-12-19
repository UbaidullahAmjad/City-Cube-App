package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.databinding.ActivitySignupBinding;
import com.citycube.model.SignupModel;
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

public class SignupActivity extends AppCompatActivity {
    public String TAG = "SignupActivity";
    ActivitySignupBinding binding;
    CityCubeUserInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        initViews();

   

    }

    private void initViews() {
        binding.tvAlreayHave.setOnClickListener(v -> { startActivity(new Intent(SignupActivity.this,LoginActivity.class)); });
   
         binding.btnSignUp.setOnClickListener(v -> {validation();});
   
    }

    private void validation() {
        if (binding.etUsername.getText().toString().equals("")) {
            binding.etUsername.setError(getString(R.string.please_enter_username));
            binding.etUsername.setFocusable(true);
        } else if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        } else if (binding.etPhoneNumber.getText().toString().equals("")) {
            binding.etPhoneNumber.setError(getString(R.string.please_enter_mobile_number));
            binding.etPhoneNumber.setFocusable(true);
        } else if (binding.etPassword.getText().toString().equals("")) {
            binding.etPassword.setError(getString(R.string.please_enter_pass));
            binding.etPassword.setFocusable(true);
        }
        else if (binding.etConfirmPass.getText().toString().equals("")) {
            binding.etConfirmPass.setError(getString(R.string.please_enter_confirm_pass));
            binding.etConfirmPass.setFocusable(true);
        }
        else if (!binding.etConfirmPass.getText().toString().equals(binding.etPassword.getText().toString())) {
            binding.etConfirmPass.setError(getString(R.string.password_does_not_matched));
            binding.etConfirmPass.setFocusable(true);
        }

        else {
            if (NetworkReceiver.isConnected()) {
                 registerUser();
              
            } else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        }
    }

      public void registerUser(){
        DataManager.getInstance().showProgressMessage(SignupActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_name", binding.etUsername.getText().toString());
        map.put("email", binding.etEmail.getText().toString());
        map.put("mobile", binding.etPhoneNumber.getText().toString());
        map.put("phone_code", binding.ccp.getSelectedCountryCode());
        map.put("password", binding.etPassword.getText().toString());
        map.put("register_id", /*FirebaseInstanceId.getInstance().getToken()*/"");
        map.put("type", "USER");
        Log.e(TAG, "Register Request " + map);
        Call<SignupModel> loginCall = apiInterface.signupUser(map);
        loginCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Register Response :" + responseString);
                    if (data.status.equals("1")) {
                        //  SessionManager.writeString(Register.this, Constant.USER_INFO, responseString);
                        Toast.makeText(SignupActivity.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT); //Toast.makeText(LoginAct.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (data.status.equals("0")) {
                        App.showToast(SignupActivity.this, data.message, Toast.LENGTH_SHORT);
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
}