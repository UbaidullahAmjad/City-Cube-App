package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.citycube.R;
import com.citycube.databinding.ActivityFeedbackBinding;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feedback);

        binding.tvCancel.setOnClickListener(v ->
                {
                    finish();
                }
                );

        binding.btnContinue.setOnClickListener(v ->
                {
                    startActivity(new Intent(FeedbackActivity.this,HomeActivity.class));
                    finish();
                }
                );

    }
}