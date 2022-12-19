package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.adapters.OrderedItemAdapter;
import com.citycube.databinding.ActivityOrderHistoryBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.LinkedList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding binding;
    List<String> strings = new LinkedList<>();
    List<String> completed = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_history);

        setData();
        binding.ivBack.setOnClickListener(v ->
                {
                    finish();
                }
                );

        binding.rvOrderHistory.setHasFixedSize(true);
        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
        binding.rvOrderHistory.setAdapter(new OrderedItemAdapter(OrderHistoryActivity.this,strings));


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.in_progress));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.completed));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if(currentTabSelected==0)
                {

                    //Go for Today

                    binding.rvOrderHistory.setHasFixedSize(true);
                    binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                    binding.rvOrderHistory.setAdapter(new OrderedItemAdapter(OrderHistoryActivity.this,strings));
                }else if(currentTabSelected==1)
                {
                    //Go for Upcoming
                    binding.rvOrderHistory.setHasFixedSize(true);
                    binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                    binding.rvOrderHistory.setAdapter(new OrderedItemAdapter(OrderHistoryActivity.this,completed));

                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void setData() {

        strings.add("Scheduled Pickup");
        strings.add("Scheduled Pickup");
        strings.add("Scheduled Pickup");

        completed.add("Delivered");
        completed.add("Cancelled");
    }
}