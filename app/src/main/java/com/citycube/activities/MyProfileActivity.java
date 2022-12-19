package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.citycube.R;
import com.citycube.databinding.ActivityMyProfileBinding;
import com.citycube.retrofit.Constant;
import com.citycube.utility.DataManager;
import com.citycube.utility.SessionManager;

public class MyProfileActivity extends AppCompatActivity {
    ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_profile);
        initViews();


    }

    private void initViews() {

        binding.rlFeedbackRegarding.setOnClickListener(v -> { startActivity(new Intent(MyProfileActivity.this,FeedbackActivity.class)); });

        binding.rlEditProfile.setOnClickListener(v -> { startActivity(new Intent(MyProfileActivity.this,EditProfileActivity.class)); });

        binding.rlOrderHistory.setOnClickListener(v -> { startActivity(new Intent(MyProfileActivity.this,OrderHistoryActivity.class)); });

        binding.ivBack.setOnClickListener(v -> { finish(); });


    }

    public void setUserInfo(){
        if(SessionManager.readString(MyProfileActivity.this, Constant.USER_INFO,"").equals("")){
                binding.tvName.setText("Hi Guest");
                binding.tvEmail.setText("guest@gmail.com");
            Glide.with(MyProfileActivity.this)
                    .load(R.drawable.user_default)
                    .override(80,80)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .into(binding.ivProfile);
            binding.layoutMain.setVisibility(View.GONE);

            }
            else {
            binding.tvName.setText(DataManager.getInstance().getUserData(MyProfileActivity.this).result.userName);
            binding.tvEmail.setText(DataManager.getInstance().getUserData(MyProfileActivity.this).result.email);
            Glide.with(MyProfileActivity.this)
                    .load(DataManager.getInstance().getUserData(MyProfileActivity.this).result.image)
                    .override(80,80)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .into(binding.ivProfile);
                binding.layoutMain.setVisibility(View.VISIBLE);


            }
        }






    @Override
    protected void onResume() {
        super.onResume();
        setUserInfo();
    }
}