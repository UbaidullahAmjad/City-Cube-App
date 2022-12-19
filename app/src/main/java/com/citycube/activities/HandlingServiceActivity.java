package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.adapters.HandlingServiceAdapter;

import com.citycube.databinding.ActivityHandlingServiceBinding;
import com.citycube.listener.onHandlerSelectedListener;
import com.citycube.listener.onVanSelectedListener;
import com.citycube.model.GetPriceModel;
import com.citycube.model.HandlerModel;
import com.citycube.model.HandlerModel2;
import com.citycube.model.SignupModel;
import com.citycube.model.VanListModel;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.retrofit.Constant;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkAvailablity;
import com.citycube.utility.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlingServiceActivity extends AppCompatActivity implements onHandlerSelectedListener {
    public String TAG = "HandlingServiceActivity";
    ActivityHandlingServiceBinding binding;
    HandlingServiceAdapter adapter;
    CityCubeUserInterface apiInterface;
    ArrayList<VanListModel.Result> vanArrayList;
    ArrayList<HandlerModel> arrayList;
    ArrayList<HandlerModel2.Result> handlerList;
    ArrayList<GetPriceModel.Result> priceList;
    String selectedVan = "", VanSize = "", selectedHandler = "", selectedHandler2 = "", preLongStrPhle, preLongStr, postLongStr, finalString, Progresss = "20 min ", vanPrice = "0.0", vanDistance = "0.0",distance="",addPercent="0";
    String departLat = "", departLon = "", arrivalLat = "", arrivalLon = "", departureAddress = "", arrivalAddress = "", date = "", time = "", timeZome = "",userId="",refreshedToken="";
    double totalPrice = 0.0,spinTotl=0.0;
    public static String Progresss1 = "20", perMinPrice = "0.50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_handling_service);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(HandlingServiceActivity.this, Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(HandlingServiceActivity.this).result.id;
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();

                if (NetworkAvailablity.checkNetworkStatus(HandlingServiceActivity.this)) getVanList();
                else
                    Toast.makeText(HandlingServiceActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        initViews();


    }

    private void getVanList() {
        vanArrayList = new ArrayList<>();
        DataManager.getInstance().showProgressMessage(HandlingServiceActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("picuplat", departLat);
        map.put("pickuplon", departLon);
        map.put("droplat", arrivalLat);
        map.put("droplon", arrivalLon);
        map.put("user_id", userId);
        Log.e(TAG, "Login Request " + map);
        Call<VanListModel> loginCall = apiInterface.getVanList(map);
        loginCall.enqueue(new Callback<VanListModel>() {
            @Override
            public void onResponse(Call<VanListModel> call, Response<VanListModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    VanListModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Login Response :" + responseString);
                    if (data.status.equals("1")) {
                        vanArrayList.clear();
                        vanArrayList.addAll(data.result);
                    } else if (data.status.equals("0")) {
                        App.showToast(HandlingServiceActivity.this, data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VanListModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void selectedHandler(String S, int pos) {
        handlerList = new ArrayList<>();
        DataManager.getInstance().showProgressMessage(HandlingServiceActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("km", distance);
        map.put("helper_status",S);
        Log.e(TAG, "helper Service Request " + map);
        Call<GetPriceModel> loginCall = apiInterface.getPrice(map);
        loginCall.enqueue(new Callback<GetPriceModel>() {
            @Override
            public void onResponse(Call<GetPriceModel> call, Response<GetPriceModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    GetPriceModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "helper Service Response :" + responseString);
                    if (data.status.equals("1")) {
                        priceList.clear();
                        priceList.addAll(data.result);
                        totalPrice = 0.0;
                        if (VanSize.equals("6m3"))
                            totalPrice = Double.parseDouble(priceList.get(0)._6m3) + Double.parseDouble(AddVehicleActivity.handlerList.get(pos).handlingPrice);
                        else if (VanSize.equals("9m3"))
                            totalPrice = Double.parseDouble(priceList.get(0)._9m3) + Double.parseDouble(AddVehicleActivity.handlerList.get(pos).handlingPrice);
                        else if (VanSize.equals("12_14m3"))
                            totalPrice = Double.parseDouble(priceList.get(0)._1214m3) + Double.parseDouble(AddVehicleActivity.handlerList.get(pos).handlingPrice);
                        else if (VanSize.equals("20m3"))
                            totalPrice = Double.parseDouble(priceList.get(0)._20m3) + Double.parseDouble(AddVehicleActivity.handlerList.get(pos).handlingPrice)
                                    ;
                        spinTotl =totalPrice;
                        allcalulation(pos,totalPrice);

                    } else if (data.status.equals("0")) {
                        App.showToast(HandlingServiceActivity.this, data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetPriceModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void initViews() {
        priceList = new ArrayList<>();

        if (getIntent() != null) {
            selectedVan = getIntent().getStringExtra("selectedVan");
            departLat = getIntent().getStringExtra("departLat");
            departLon = getIntent().getStringExtra("departLon");
            arrivalLat = getIntent().getStringExtra("arrivalLat");
            arrivalLon = getIntent().getStringExtra("arrivalLon");
            departureAddress = getIntent().getStringExtra("departureAddress");
            arrivalAddress = getIntent().getStringExtra("arrivalAddress");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            timeZome = getIntent().getStringExtra("timeZone");
            distance = getIntent().getStringExtra("distance");
            addPercent = getIntent().getStringExtra("percent");
            if (selectedVan.equals("1")) VanSize = "6m3";
            else if (selectedVan.equals("2")) VanSize = "9m3";
            else if (selectedVan.equals("3")) VanSize = "12_14m3";
            else if (selectedVan.equals("4")) VanSize = "20m3";

         //   selectedHandler("0",0);

        }

        // getHandler();

        String yourLongString = getString(R.string.text_for_loading3);


        preLongStrPhle = yourLongString.substring(0, 9);
        preLongStr = yourLongString.substring(9, 94);
        postLongStr = yourLongString.substring(94);


        arrayList = new ArrayList<>();
        arrayList.add(new HandlerModel("1", getString(R.string.transport_only), getString(R.string.sidewalk_to_sidewalk), R.drawable.transport_only, false));
        arrayList.add(new HandlerModel("2", getString(R.string.driver_handler), getString(R.string.max_30_kg_per_object), R.drawable.driver_handler, false));
        arrayList.add(new HandlerModel("3", getString(R.string.two_handlers), getString(R.string.option_not_available_with_selected_vehicle), R.drawable.handlers, false));


        binding.reservationToolbar.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.reservationToolbar.tvTitle.setText(R.string.handling_service);

        adapter = new HandlingServiceAdapter(this, arrayList, HandlingServiceActivity.this, selectedVan);

        binding.rvHandlingServices.setAdapter(adapter);

        binding.btnContinue.setOnClickListener(v -> {
            if (selectedHandler.equals("")) {
                App.showToast(HandlingServiceActivity.this, getString(R.string.please_select_handling_service), Toast.LENGTH_LONG);
            } else {
                startActivity(new Intent(HandlingServiceActivity.this, ContactInfo.class)
                        .putExtra("selectedVan", selectedVan)
                        .putExtra("selectedHandler", selectedHandler2)
                        .putExtra("departLat", departLat)
                        .putExtra("departLon", departLon).putExtra("arrivalLat", arrivalLat)
                        .putExtra("arrivalLon", arrivalLon + "")
                        .putExtra("departureAddress", departureAddress)
                        .putExtra("arrivalAddress", arrivalAddress)
                        .putExtra("date", date).putExtra("time", time)
                        .putExtra("timeZone", timeZome)
                        .putExtra("totalPrice", String.format("%.2f", totalPrice)));
            }

        });


        binding.seekProgressMin.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                //    states.setText("states: onSeeking");
                //    progress.setText("progress: " + seekParams.progress);
                //   progress_float.setText("progress_float: " + seekParams.progressFloat);
                //    from_user.setText("from_user: " + seekParams.fromUser);
                Log.e("progressOnSeeking", seekParams.progress + "");
                Progresss = seekParams.progress + " min ";
                Progresss1 = seekParams.progress + "";
                if (selectedHandler.equals("1")) {
                    perMinPrice = "0.50";
                    finalString = preLongStrPhle + Progresss + preLongStr + " " + perMinPrice + " " + postLongStr;
                    if (finalString != null && finalString.contains(perMinPrice)) {
                        Spannable spannable = new SpannableString(finalString);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.selected_color)), finalString.indexOf(perMinPrice), finalString.indexOf(perMinPrice) + perMinPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        binding.tvLoadingPrice.setText(spannable);
                    }

                    // binding.tvLoadingPrice.setText(finalString);
                } else if (selectedHandler.equals("2")) {
                    perMinPrice = "1.00";
                    finalString = preLongStrPhle + Progresss + preLongStr + " " + perMinPrice + " " + postLongStr;
                    if (finalString != null && finalString.contains(perMinPrice)) {
                        Spannable spannable = new SpannableString(finalString);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.selected_color)), finalString.indexOf(perMinPrice), finalString.indexOf(perMinPrice) + perMinPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        binding.tvLoadingPrice.setText(spannable);
                    }
                } else if (selectedHandler.equals("3")) {
                    perMinPrice = "1.50";
                    finalString = preLongStrPhle + Progresss + preLongStr + " " + perMinPrice + " " + postLongStr;
                    if (finalString != null && finalString.contains(perMinPrice)) {
                        Spannable spannable = new SpannableString(finalString);
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.selected_color)), finalString.indexOf(perMinPrice), finalString.indexOf(perMinPrice) + perMinPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        binding.tvLoadingPrice.setText(spannable);
                    }
                }

                Log.e("totalMinPrice", seekParams.progress * Double.parseDouble(perMinPrice) + "");
                for (int i = 0; i < vanArrayList.size(); i++) {
                    if (selectedVan.equals(vanArrayList.get(i).id)) {
                        vanPrice = vanArrayList.get(i).charge;
                        vanDistance = vanArrayList.get(i).distance;
                    }
                }
                if(addPercent.equals("20"))  {
                    double more = spinTotl +  (spinTotl*20/100 ) ;
                    totalPrice = more + /*Float.parseFloat(vanPrice) * Float.parseFloat(vanDistance) +*/ Float.parseFloat(perMinPrice) * Float.parseFloat(Progresss1);
                }
                else totalPrice =  spinTotl + /*Float.parseFloat(vanPrice) * Float.parseFloat(vanDistance) +*/ Float.parseFloat(perMinPrice) * Float.parseFloat(Progresss1);
                Log.e("TotalPrice===", totalPrice + "");
                binding.tvTotal.setText("Price" + "\n" + "€" + String.format("%.2f", totalPrice));

            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
                //    states.setText("states: onStart");
                //   progress.setText("progress: " + seekBar.getProgress());
                //  progress_float.setText("progress_float: " + seekBar.getProgressFloat());
                //   from_user.setText("from_user: true");
                Log.e("StartTrackingTouch", seekBar.getProgress() + "");

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                // states.setText("states: onStop");
                //  progress.setText("progress: " + seekBar.getProgress());
                //  progress_float.setText("progress_float: " + seekBar.getProgressFloat());
                //  from_user.setText("from_user: false");
                Log.e("onStopTrackingTouch", seekBar.getProgress() + "");
            }

        });


    }

    @Override
    public void onHandler(int position, String text) {
        selectedHandler = arrayList.get(position).getHandlerId();
        String helper="";
        if (selectedHandler.equals("1")) helper = "0";
        else if (selectedHandler.equals("2")) helper = "1";
        else if (selectedHandler.equals("3")) helper = "2";

        selectedHandler(helper, position);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //  getHandler();

    }


    public void allcalulation(int position,double totl) {
        if (selectedHandler.equals("1")) {
            perMinPrice = "0.50";
            selectedHandler2 = AddVehicleActivity.handlerList.get(position).id;

            finalString = preLongStrPhle + Progresss + preLongStr + " " + perMinPrice + " " + postLongStr;
            if (finalString != null && finalString.contains(perMinPrice)) {
                Spannable spannable = new SpannableString(finalString);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.selected_color)), finalString.indexOf(perMinPrice), finalString.indexOf(perMinPrice) + perMinPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvLoadingPrice.setText(spannable);
            }

            // binding.tvLoadingPrice.setText(finalString);
        } else if (selectedHandler.equals("2")) {
            perMinPrice = "1.00";
            selectedHandler2 = AddVehicleActivity.handlerList.get(position).id;

            finalString = preLongStrPhle + Progresss + preLongStr + " " + perMinPrice + " " + postLongStr;
            if (finalString != null && finalString.contains(perMinPrice)) {
                Spannable spannable = new SpannableString(finalString);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.selected_color)), finalString.indexOf(perMinPrice), finalString.indexOf(perMinPrice) + perMinPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvLoadingPrice.setText(spannable);
            }
        } else if (selectedHandler.equals("3")) {
            perMinPrice = "1.50";
            selectedHandler2 = AddVehicleActivity.handlerList.get(position).id;
            finalString = preLongStrPhle + Progresss + preLongStr + " " + perMinPrice + " " + postLongStr;
            if (finalString != null && finalString.contains(perMinPrice)) {
                Spannable spannable = new SpannableString(finalString);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.selected_color)), finalString.indexOf(perMinPrice), finalString.indexOf(perMinPrice) + perMinPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvLoadingPrice.setText(spannable);
            }
        }

        for (int i = 0; i < vanArrayList.size(); i++) {
            if (selectedVan.equals(vanArrayList.get(i).id)) {
                vanPrice = vanArrayList.get(i).charge;
                vanDistance = vanArrayList.get(i).distance;
            }
        }
        if(addPercent.equals("20"))  {
            double more = totl +  (totl*20/100 ) ;
            totalPrice =  more   /*+ Float.parseFloat(vanPrice) * Float.parseFloat(vanDistance) */+ Float.parseFloat(perMinPrice) * Float.parseFloat(Progresss1);
        }
       else   totalPrice = totl /*+ Float.parseFloat(vanPrice) * Float.parseFloat(vanDistance) */+ Float.parseFloat(perMinPrice) * Float.parseFloat(Progresss1);
        Log.e("TotalPrice===", totalPrice + "");
        binding.tvTotal.setText("Price" + "\n" + "€" + String.format("%.2f", totalPrice));
    }


}