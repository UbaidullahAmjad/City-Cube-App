package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.citycube.R;
import com.citycube.databinding.ActivitySocialLoginBinding;

public class SocialLoginActivity extends AppCompatActivity {

    ActivitySocialLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_social_login);

        binding.btnGetStarted.setOnClickListener(v ->
                {
                    /*startActivity(new Intent(SocialLoginActivity.this,LoginActivity.class));
                    finish();*/

                    startActivity(new Intent(SocialLoginActivity.this,HomeActivity.class));
                    finish();
                }
        );

    }
}