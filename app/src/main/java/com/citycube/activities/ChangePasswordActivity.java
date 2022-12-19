package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.citycube.R;
import com.citycube.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_password);

        binding.reservationToolbar.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.reservationToolbar.tvTitle.setText(R.string.change_password);

    }
}