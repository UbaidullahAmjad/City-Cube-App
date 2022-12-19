package com.citycube.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.citycube.R;
import com.citycube.adapters.TaskStatusPagerAdapter;
import com.citycube.databinding.ActivityMyBookingsBinding;
import com.google.android.material.tabs.TabLayout;

public class MyBookingsAct extends AppCompatActivity {
    ActivityMyBookingsBinding binding;
    TaskStatusPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_bookings);

        binding.ivBack.setOnClickListener(v -> finish());

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.active));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.cancel));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.complete));


        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapterViewPager = new TaskStatusPagerAdapter(this, getSupportFragmentManager(), binding.tabLayout.getTabCount());

        binding.vpPager.setAdapter(adapterViewPager);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}