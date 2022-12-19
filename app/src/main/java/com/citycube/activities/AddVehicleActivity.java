package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.adapters.VehicleAdapter;
import com.citycube.databinding.ActivityAddVehicleBinding;
import com.citycube.listener.onVanSelectedListener;
import com.citycube.map.BaseClass;
import com.citycube.map.DataParser;
import com.citycube.map.DataParser2;
import com.citycube.model.HandlerModel2;
import com.citycube.model.VehicleModel;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class AddVehicleActivity extends AppCompatActivity implements onVanSelectedListener {
    public String TAG = "AddVehicleActivity";
    ActivityAddVehicleBinding binding;
    VehicleAdapter adapter;
    ArrayList<VehicleModel> arrayList;
    String departLat = "", departLon = "", arrivalLat = "", arrivalLon = "", departureAddress = "", arrivalAddress = "", selectedVan = "", date = "", time = "", timeZome = "",distance="",addPercent="0";
    public static ArrayList<HandlerModel2.Result> handlerList;
    CityCubeUserInterface apiInterface;
    List<List<HashMap<String, String>>> routes=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vehicle);
        initViews();

    }

    private void initViews() {
        binding.vehicleToolbar.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.vehicleToolbar.tvTitle.setText(getString(R.string.select_vehicle));

        if (getIntent() != null) {
            departLat = getIntent().getStringExtra("departLat");
            departLon = getIntent().getStringExtra("departLon");
            arrivalLat = getIntent().getStringExtra("arrivalLat");
            arrivalLon = getIntent().getStringExtra("arrivalLon");
            departureAddress = getIntent().getStringExtra("departureAddress");
            arrivalAddress = getIntent().getStringExtra("arrivalAddress");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            timeZome = getIntent().getStringExtra("timeZone");
            addPercent = getIntent().getStringExtra("percent");
            getKm();
        }


        arrayList = new ArrayList<>();
        arrayList.add(new VehicleModel("1", getString(R.string.small), "6 M3", "800 Kgs", R.drawable.small_unselected, false));
        arrayList.add(new VehicleModel("2", getString(R.string.classic), "9 M3", "1200 Kgs", R.drawable.classic_unselected, false));
        arrayList.add(new VehicleModel("3", getString(R.string.large), "12-14 M3", "1400 Kgs", R.drawable.large_unselected, false));
        arrayList.add(new VehicleModel("4", getString(R.string.jumbo), "20 M3", "700 Kgs", R.drawable.jumbo, false));

        adapter = new VehicleAdapter(AddVehicleActivity.this, arrayList, AddVehicleActivity.this);
        binding.rvVehicle.setAdapter(adapter);


        binding.btnContinue.setOnClickListener(v -> {
            if (selectedVan.equals(""))
                App.showToast(AddVehicleActivity.this, getString(R.string.please_choose_vehicle), Toast.LENGTH_LONG);
            else startActivity(new Intent(AddVehicleActivity.this, HandlingServiceActivity.class)
                    .putExtra("selectedVan", selectedVan)
                    .putExtra("departLat", departLat)
                    .putExtra("departLon", departLon).putExtra("arrivalLat", arrivalLat)
                    .putExtra("arrivalLon", arrivalLon + "").putExtra("departureAddress", departureAddress)
                    .putExtra("arrivalAddress", arrivalAddress)
                    .putExtra("date", date).putExtra("time", time)
                    .putExtra("timeZone", timeZome)
                    .putExtra("distance",distance)
                    .putExtra("percent",addPercent)
            );

        });


    }

    @Override
    public void onVan(int position) {
        selectedVan = arrayList.get(position).getVehicleId();
        getHandler();
    }


    private void getHandler() {
        handlerList = new ArrayList<>();
        DataManager.getInstance().showProgressMessage(AddVehicleActivity.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("car_type_id",selectedVan);
        Log.e(TAG,"Handler Service Request "+map);
        Call<HandlerModel2> loginCall = apiInterface.getHandlerService(map);
        loginCall.enqueue(new Callback<HandlerModel2>() {
            @Override
            public void onResponse(Call<HandlerModel2> call, Response<HandlerModel2> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    HandlerModel2 data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Handler Service Response :"+responseString);
                    if(data.status.equals("1")){
                        handlerList.clear();
                        handlerList.addAll(data.result);

                    }
                    else if(data.status.equals("0")){
                        App.showToast(AddVehicleActivity.this,data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HandlerModel2> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void getKm(){
        String URL= BaseClass.get().getPolyLineUrl(AddVehicleActivity.this,new LatLng(Double.parseDouble(departLat),Double.parseDouble(departLon)),new LatLng(Double.parseDouble(arrivalLat),Double.parseDouble(arrivalLon)));
        ApiCallBuilder.build(AddVehicleActivity.this)
                .setUrl(URL).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    DataParser2 parser = new DataParser2();
                    distance  = parser.parse2(object);
                    Log.e("Distance===",distance);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failed(String error) {

            }
        });
    }


}