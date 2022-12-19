package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.citycube.R;
import com.citycube.databinding.ActivityShareBinding;

public class ShareActivity extends AppCompatActivity {

    ActivityShareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_share);

        binding.reservationToolbar.ivBack.setOnClickListener(v ->
                {
                    finish();
                }
        );

        binding.reservationToolbar.tvTitle.setText(R.string.share);

    }
}