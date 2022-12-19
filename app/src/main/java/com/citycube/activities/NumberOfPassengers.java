package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.citycube.R;
import com.citycube.adapters.PassengerAdapter;
import com.citycube.databinding.ActivityNumberOfPassengersBinding;

import java.util.LinkedList;
import java.util.List;

public class NumberOfPassengers extends AppCompatActivity {

    ActivityNumberOfPassengersBinding binding;

    List<String> list = new LinkedList<>();
    ArrayAdapter ad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_number_of_passengers);


        setData();

        binding.passengerToolbar.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.passengerToolbar.tvTitle.setText(R.string.numbers_of_passengers);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvPassengers.setLayoutManager(gridLayoutManager);
     //   binding.rvPassengers.setAdapter(new PassengerAdapter(this,list));

        binding.btnContinue.setOnClickListener(v ->
                {
                    startActivity(new Intent(NumberOfPassengers.this,ReservationActivity.class));
                }
                );


    }

    private void setData()
    {
        list.add("01");
        list.add("02");
        list.add("03");
        list.add("04");
        list.add("05");
        list.add("+");
    }


}