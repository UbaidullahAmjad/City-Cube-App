package com.citycube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.citycube.R;
import com.citycube.databinding.ActivityFurtherInfoBinding;
import com.citycube.utility.App;

public class FurtherInformationAct extends AppCompatActivity {
    ActivityFurtherInfoBinding binding;
    String departLat = "", departLon = "", arrivalLat = "", arrivalLon = "", departureAddress = "", arrivalAddress = "", date = "", time = "", timeZome = ""
            ,selectedVan="",selectedHandler="",totalPrice="",first_name="",last_name="",email="",mobile="",departure_contact_name="",departure_contact_number="",arrival_contact_name=""
            ,arrival_contact_number="",passengers="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_further_info);
        initViews();
    }

    private void initViews() {
        binding.reservationToolbar.tvTitle.setText(R.string.further_information);
        binding.reservationToolbar.ivBack.setOnClickListener(v -> { finish(); });

        if (getIntent() != null) {
            selectedVan = getIntent().getStringExtra("selectedVan");
            selectedHandler = getIntent().getStringExtra("selectedHandler");
            departLat = getIntent().getStringExtra("departLat");
            departLon = getIntent().getStringExtra("departLon");
            arrivalLat = getIntent().getStringExtra("arrivalLat");
            arrivalLon = getIntent().getStringExtra("arrivalLon");
            departureAddress = getIntent().getStringExtra("departureAddress");
            arrivalAddress = getIntent().getStringExtra("arrivalAddress");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            timeZome = getIntent().getStringExtra("timeZone");
            totalPrice = getIntent().getStringExtra("totalPrice");
            first_name = getIntent().getStringExtra("first_name");
            last_name = getIntent().getStringExtra("last_name");
            email = getIntent().getStringExtra("email");
            mobile = getIntent().getStringExtra("mobile");
            departure_contact_name = getIntent().getStringExtra("departure_contact_name");
            departure_contact_number = getIntent().getStringExtra("departure_contact_number");
            arrival_contact_name = getIntent().getStringExtra("arrival_contact_name");
            arrival_contact_number = getIntent().getStringExtra("arrival_contact_number");
            passengers = getIntent().getStringExtra("passengers");

        }


        binding.btnContinue.setOnClickListener(v -> {

            if(binding.etDelivery.getText().toString().equals(""))
                App.showToast(FurtherInformationAct.this,getString(R.string.please_enter_what_you_want_to_transport), Toast.LENGTH_LONG);
            else
            startActivity(new Intent(FurtherInformationAct.this, PaymentOptionActivity.class)
                    .putExtra("selectedVan", selectedVan)
                    .putExtra("selectedHandler", selectedHandler)
                    .putExtra("departLat", departLat)
                    .putExtra("departLon", departLon)
                    .putExtra("arrivalLat", arrivalLat)
                    .putExtra("arrivalLon", arrivalLon)
                    .putExtra("departureAddress", departureAddress)
                    .putExtra("arrivalAddress", arrivalAddress)
                    .putExtra("date", date).putExtra("time", time)
                    .putExtra("timeZone", timeZome)
                    .putExtra("totalPrice",totalPrice)
                    .putExtra("first_name", first_name)
                    .putExtra("last_name", last_name)
                    .putExtra("email", email)
                    .putExtra("mobile", mobile)
                    .putExtra("departure_contact_name", departure_contact_name)
                    .putExtra("departure_contact_number",departure_contact_number)
                    .putExtra("arrival_contact_name", arrival_contact_name)
                    .putExtra("arrival_contact_number", arrival_contact_number)
                    .putExtra("passengers", passengers)
                    .putExtra("transport", binding.etDelivery.getText().toString())

            );

        });

    }
}
