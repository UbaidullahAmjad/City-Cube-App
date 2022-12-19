package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.adapters.PassengerAdapter;
import com.citycube.databinding.ActivityContactInfoBinding;
import com.citycube.listener.onVanSelectedListener;
import com.citycube.model.GetPriceModel;
import com.citycube.model.NumberPassengerModel;
import com.citycube.model.NumberPassengerModel2;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkAvailablity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactInfo extends AppCompatActivity implements onVanSelectedListener {
    public String TAG = "ContactInfo";
    ActivityContactInfoBinding binding;
 //   ArrayList<NumberPassengerModel> arrayList;
    ArrayList<NumberPassengerModel2.Result> arrayList;
    String pos = "",numberOfPassengers="";
    String departLat = "", departLon = "", arrivalLat = "", arrivalLon = "", departureAddress = "", arrivalAddress = "", date = "", time = "", timeZome = ""
            ,selectedVan="",selectedHandler="",totalPrice="",departName="",departNumber="",arrivalName="",arrivalNumber="";

    CityCubeUserInterface apiInterface;
    PassengerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_info);
        initViews();


    }

    private void initViews() {
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

        }


        arrayList = new ArrayList<>();
       // arrayList.add(new NumberPassengerModel("1", getString(R.string.no_passenger), false));
      //  arrayList.add(new NumberPassengerModel("2", getString(R.string.one_passenger), false));
      //  arrayList.add(new NumberPassengerModel("3", getString(R.string.two_passenger), false));


        //binding.rvPassengers.setAdapter(new PassengerAdapter(this, arrayList, ContactInfo.this));
        adapter = new PassengerAdapter(this, arrayList, ContactInfo.this);
        binding.rvPassengers.setAdapter(adapter);


        binding.reservationToolbar.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.reservationToolbar.tvTitle.setText(R.string.parcel_receiver_info);

        binding.btnContinue.setOnClickListener(v -> { validation(); });

        binding.radioDepartureMe.setOnClickListener(v -> {
            binding.etDepartureName.setVisibility(View.GONE);
            binding.llDepartNumber.setVisibility(View.GONE);
        });

        binding.radioDepartureOther.setOnClickListener(v -> {
            binding.etDepartureName.setVisibility(View.VISIBLE);
            binding.llDepartNumber.setVisibility(View.VISIBLE);
        });

        binding.radioArrivalMe.setOnClickListener(v -> {
            binding.etArrivalName.setVisibility(View.GONE);
            binding.llArrivalNumber.setVisibility(View.GONE);
        });

        binding.radioArrivalOther.setOnClickListener(v -> {
            binding.etArrivalName.setVisibility(View.VISIBLE);
            binding.llArrivalNumber.setVisibility(View.VISIBLE);
        });

        if(NetworkAvailablity.checkNetworkStatus(ContactInfo.this)) getAllPassenList();
        else Toast.makeText(ContactInfo.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }

    private void validation() {
        if (binding.etName.getText().toString().equals("")) {
            binding.etName.setError(getString(R.string.please_enter_your_name));
            binding.etName.setFocusable(true);
        } else if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.please_enter_mobile_number));
            binding.etMobile.setFocusable(true);
        } else if (pos.equals("")) {
            App.showToast(ContactInfo.this, getString(R.string.select_passenger), Toast.LENGTH_LONG);
        } else if (pos.equals("0")) {
            if (binding.etDepartureName.getText().toString().equals("")) {
                binding.etDepartureName.setError(getString(R.string.please_enter_name));
                binding.etDepartureName.setFocusable(true);
            } else if (binding.etDepartureNumber.getText().toString().equals("")) {
                binding.etDepartureNumber.setError(getString(R.string.please_enter_starting_contact));
                binding.etDepartureNumber.setFocusable(true);
            } else if (binding.etArrivalName.getText().toString().equals("")) {
                binding.etArrivalName.setError(getString(R.string.please_enter_name));
                binding.etArrivalName.setFocusable(true);
            } else if (binding.etArrivalNumber.getText().toString().equals("")) {
                binding.etArrivalNumber.setError(getString(R.string.please_enter_arrival_contact));
                binding.etArrivalNumber.setFocusable(true);
            } else {
                if(binding.radioDepartureMe.isChecked()){
                     departName = binding.etName.getText().toString();
                     departNumber = binding.ccp.getSelectedCountryCode() + binding.etMobile.getText().toString();
                }else {
                    departName = binding.etDepartureName.getText().toString();
                    departNumber = binding.departureCcp.getSelectedCountryCode() + binding.etDepartureNumber.getText().toString();
                }

                if(binding.radioArrivalMe.isChecked()){
                    arrivalName = binding.etName.getText().toString();
                    arrivalNumber = binding.ccp.getSelectedCountryCode() + binding.etMobile.getText().toString();
                }else {
                    arrivalName = binding.etArrivalName.getText().toString();
                    arrivalNumber = binding.arrivalCcp.getSelectedCountryCode() + binding.etArrivalNumber.getText().toString();
                }


                startActivity(new Intent(ContactInfo.this, FurtherInformationAct.class)
                        .putExtra("selectedVan", selectedVan)
                        .putExtra("selectedHandler", selectedHandler)
                        .putExtra("departLat", departLat)
                        .putExtra("departLon", departLon)
                        .putExtra("arrivalLat", arrivalLat)
                        .putExtra("arrivalLon", arrivalLon + "")
                        .putExtra("departureAddress", departureAddress)
                        .putExtra("arrivalAddress", arrivalAddress)
                        .putExtra("date", date).putExtra("time", time)
                        .putExtra("timeZone", timeZome)
                        .putExtra("totalPrice",totalPrice)
                        .putExtra("first_name", binding.etName.getText().toString())
                        .putExtra("last_name", "")
                        .putExtra("email", binding.etEmail.getText().toString())
                        .putExtra("mobile", binding.ccp.getSelectedCountryCode() +binding.etMobile.getText().toString())
                        .putExtra("departure_contact_name", departName /*binding.etDepartureName.getText().toString()*/)
                        .putExtra("departure_contact_number",departNumber/*binding.departureCcp.getSelectedCountryCode() + binding.etDepartureNumber.getText().toString()*/)
                        .putExtra("arrival_contact_name",arrivalName /*binding.etArrivalName.getText().toString()*/)
                        .putExtra("arrival_contact_number",arrivalNumber /*binding.arrivalCcp.getSelectedCountryCode() + binding.etArrivalNumber.getText().toString()*/)
                        .putExtra("passengers", numberOfPassengers)
                );

            }
        } else {
            startActivity(new Intent(ContactInfo.this, FurtherInformationAct.class)
                    .putExtra("selectedVan", selectedVan)
                    .putExtra("selectedHandler", selectedHandler)
                    .putExtra("departLat", departLat)
                    .putExtra("departLon", departLon)
                    .putExtra("arrivalLat", arrivalLat)
                    .putExtra("arrivalLon", arrivalLon + "")
                    .putExtra("departureAddress", departureAddress)
                    .putExtra("arrivalAddress", arrivalAddress)
                    .putExtra("date", date).putExtra("time", time)
                    .putExtra("timeZone", timeZome)
                    .putExtra("totalPrice",totalPrice)
                    .putExtra("first_name", binding.etName.getText().toString())
                    .putExtra("last_name", "")
                    .putExtra("email", binding.etEmail.getText().toString())
                    .putExtra("mobile", binding.ccp.getSelectedCountryCode() + binding.etMobile.getText().toString())
                    .putExtra("departure_contact_name", "")
                    .putExtra("departure_contact_number","")
                    .putExtra("arrival_contact_name", "")
                    .putExtra("arrival_contact_number", "")
                    .putExtra("passengers", numberOfPassengers)
            );
        }
    }

    @Override
    public void onVan(int position) {
        pos = String.valueOf(position);
      //  numberOfPassengers = arrayList.get(position).getPassengerNumber();

        if(arrayList.get(position).passenger.equals("No")) {
            numberOfPassengers = getString(R.string.no_passenger);
            totalPrice = String.valueOf(Double.parseDouble(totalPrice) + Double.parseDouble(arrayList.get(position).price))  ;
        }

        else if(arrayList.get(position).passenger.equals("1")) {
            numberOfPassengers = getString(R.string.one_passenger);
            totalPrice = String.valueOf(Double.parseDouble(totalPrice) + Double.parseDouble(arrayList.get(position).price))  ;
        }

        else if(arrayList.get(position).passenger.equals("2")){
            numberOfPassengers = getString(R.string.two_passenger);
            totalPrice = String.valueOf(Double.parseDouble(totalPrice) + Double.parseDouble(arrayList.get(position).price))  ;
        }

        if (position == 0) binding.llNopassenger.setVisibility(View.VISIBLE);
        else binding.llNopassenger.setVisibility(View.GONE);
    }


    private void getAllPassenList() {
        DataManager.getInstance().showProgressMessage(ContactInfo.this, getString(R.string.please_wait));
        Call<NumberPassengerModel2> loginCall = apiInterface.getPassengerList();
        loginCall.enqueue(new Callback<NumberPassengerModel2>() {
            @Override
            public void onResponse(Call<NumberPassengerModel2> call, Response<NumberPassengerModel2> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    NumberPassengerModel2 data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "passenger List Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        App.showToast(ContactInfo.this, data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<NumberPassengerModel2> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}