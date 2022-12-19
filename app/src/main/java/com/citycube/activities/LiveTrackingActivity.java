package com.citycube.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.citycube.R;
import com.citycube.databinding.ActivityLiveTrackingBinding;
import com.citycube.map.DrawPollyLine;
import com.citycube.model.BookingDetailModel;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    public String TAG = "LiveTrackingActivity";
    ActivityLiveTrackingBinding binding;
    CityCubeUserInterface apiInterface;
    private GoogleMap mMap;
    SupportMapFragment mapFragment ;
    int PERMISSION_ID = 44;
    Vibrator vibrator;
    private PolylineOptions lineOptions;
    private LatLng PickUpLatLng, DropOffLatLng, carLatLng, prelatLng, mapCenterLatLng;
    private MarkerOptions PicUpMarker;
    private MarkerOptions DropOffMarker;
    private MarkerOptions carMarker1;
    Marker carMarker;
    String request_id = "", driverStatus = "", DriverId = "", DriverName = "", DriverImage = "", mobile = "";


    /*BroadcastReceiver LocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("latitude") != null) {
                carLatLng = new LatLng(Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude")));
                if (prelatLng == null) {
                    //if (driverStatus.equals("Accept")) DrawPolyLine1();
                    //   else if(driverStatus.equals("Arrived"))  DrawPolyLine();
                    //   else if(driverStatus.equals("Start"))  DrawPolyLine1();
                    if (carMarker != null) carMarker.remove();
                    carMarker1.position(carLatLng);
                    carMarker = mMap.addMarker(carMarker1);
                    carMarker1.flat(true);
                    prelatLng = carLatLng;
                    if (PolyUtil.isLocationOnPath(carLatLng, polineLanLongLine, true, tolerance)) {
                        Log.e("chala on road===", "==true===");
                        //DrawPolyLine1();
                    } else Log.e("chala on road nahi hai=", "==false===");

                } else {
                    if (prelatLng != carLatLng) {
                        Log.e("locationChange====", carLatLng + "");
                        Location temp = new Location(LocationManager.GPS_PROVIDER);
                        temp.setLatitude(Double.parseDouble(intent.getStringExtra("latitude")));
                        temp.setLongitude(Double.parseDouble(intent.getStringExtra("longitude")));
                        //  MarkerAnimation.animateMarkerToGB(carMarker, carLatLng, new LatLngInterpolator.Spherical());
                        //  MarkerAnimation.move(mMap,carMarker,temp);
                        // MarkerAnimation.animateMarker(mMap,carMarker,polineLanLongLine,isMarkerRotating);
                        // float bearing = (float) bearingBetweenLocations(prelatLng, carLatLng);
                        // rotateMarker(carMarker, bearing);

                        moveVechile(carMarker, temp);
                        rotateMarker(carMarker, temp.getBearing(), start_rotation);

                        prelatLng = carLatLng;


                        if (driverStatus.equals("Start")) {
                            if (PolyUtil.isLocationOnPath(carLatLng, polineLanLongLine, true, tolerance)) {
                                Log.e("chala on road===", "==true===");

                            } else {
                                DrawPolyLine2();
                                Log.e("chala on road nahi hai=", "==false===");
                            }
                        }
                    }
                }
                animateCamera(carLatLng);
            }

            //  binding.tvAddress.setText(getAddress(TrackAct.this,Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude"))));
        }
    };*/


    BroadcastReceiver TripStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("status") != null) {
                if (intent.getStringExtra("status").equals("chat")) {
                    if (NetworkReceiver.isConnected()) {
                        //   request_id = intent.getStringExtra("request_id");
                        getChatCount();
                    } else
                        App.showToast(LiveTrackingActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                } else if (intent.getStringExtra("status").equals("Arrived") ||  intent.getStringExtra("status").equals("Loading")
                || intent.getStringExtra("status").equals("Finish")) {
                    request_id = intent.getStringExtra("request_id");
                   if(NetworkReceiver.isConnected()) getBookingDetail(request_id);
                   else App.showToast(LiveTrackingActivity.this,getString(R.string.network_failure),Toast.LENGTH_LONG);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_tracking);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        initViews();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);







    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> { finish(); });

        if (getIntent() != null)
            request_id = getIntent().getStringExtra("request_id");

        PicUpMarker = new MarkerOptions().title("Pick Up Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
        DropOffMarker = new MarkerOptions().title("Drop Off Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));

        binding.layoutforChat.setOnClickListener(v -> {
            startActivity(new Intent(LiveTrackingActivity.this, MsgChatAct.class)
                    .putExtra("UserId", DriverId)
                    .putExtra("UserName", DriverName)
                    .putExtra("UserImage", DriverImage)
                    .putExtra("request_id", request_id));
        });

        binding.layoutforCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            startActivity(intent);
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(NetworkReceiver.isConnected())  getBookingDetail(request_id);
        else App.showToast(LiveTrackingActivity.this,getString(R.string.network_failure), Toast.LENGTH_LONG);
    }


    public void getBookingDetail(String request_id) {
        Map<String, String> map = new HashMap<>();
        map.put("request_id", request_id);
        Log.e(TAG, "Request Accept or Cancel Request :" + map);
        Call<BookingDetailModel> call = apiInterface.bookingDetails(map);
        call.enqueue(new Callback<BookingDetailModel>() {
            @Override
            public void onResponse(Call<BookingDetailModel> call, Response<BookingDetailModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    BookingDetailModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Request Accept or Cancel Response :" + responseString);
                    if (data.status.equals("1")) {
                        DriverId = data.result.driverDetails.id;
                        DriverName = data.result.driverDetails.userName; //+ " " + data.result.userDetails.lastLame;
                        DriverImage = data.result.driverDetails.driverImage;
                        mobile = "+" + data.result.driverDetails.phoneCode + data.result.driverDetails.mobile;
                        binding.tvName.setText(DriverName);
                        prelatLng = null;
                        driverStatus = data.result.status;
                        Glide.with(LiveTrackingActivity.this)
                                .load(DriverImage)
                                .apply(new RequestOptions().placeholder(R.drawable.user_default))
                                .override(200, 200)
                                .into(binding.ivUserPropic);
                        PickUpLatLng = new LatLng(Double.parseDouble(data.result.picuplat), Double.parseDouble(data.result.pickuplon));
                        DropOffLatLng = new LatLng(Double.parseDouble(data.result.droplat), Double.parseDouble(data.result.droplon));
                        setCurrentLoc();
                        if (driverStatus.equals("Arrived")) {
                            Log.e("fhgddvnvdvn",driverStatus);
                            AlertDialogStatus(getString(R.string.driver_arrived_driver_location),driverStatus);

                        } else if (driverStatus.equals("Loading")) {
                            AlertDialogStatus(getString(R.string.loading_completed),driverStatus);
                        } else if (driverStatus.equals("Finish")) {
                            AlertDialogStatus(getString(R.string.unloading_completed),driverStatus);
                        }
                    } else if (data.status.equals("0")) {
                        App.showToast(LiveTrackingActivity.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

/*
    private void callService() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                startService(new Intent(LiveTrackingActivity.this, UpdateLocationService.class));
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
*/

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(17).build();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLoc();
            }
        }
    }

    private void setCurrentLoc() {
        DrawPolyLine();
    }

    private void DrawPolyLine() {
        DrawPollyLine.get(this).setOrigin(PickUpLatLng)
                .setDestination(DropOffLatLng).execute(new DrawPollyLine.onPolyLineResponse() {
            @Override
            public void Success(ArrayList<LatLng> latLngs) {
                mMap.clear();
                // polineLanLongLine.clear();
                // polineLanLongLine = latLngs;
                lineOptions = new PolylineOptions();
                lineOptions.addAll(latLngs);
                lineOptions.width(10);
                lineOptions.geodesic(true);
                lineOptions.color(R.color.black);
                AddDefaultMarker();
            }
        });
    }

    public void AddDefaultMarker() {
        if (mMap != null) {
            mMap.clear();
            if (lineOptions != null)
                mMap.addPolyline(lineOptions);
            if (PickUpLatLng != null) {
                PicUpMarker.position(PickUpLatLng);
                mMap.addMarker(PicUpMarker);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(PickUpLatLng)));
            }
            if (DropOffLatLng != null) {
                DropOffMarker.position(DropOffLatLng);
                mMap.addMarker(DropOffMarker);
            }



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatCount();
      //  callService();
        //  registerReceiver(LocationReceiver, new IntentFilter("data_update_location1"));
        registerReceiver(TripStatusReceiver, new IntentFilter("Job_Status_Action"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(LocationReceiver);
       // stopService(new Intent(LiveTrackingActivity.this, UpdateLocationService.class));
        unregisterReceiver(TripStatusReceiver);

    }


    private void getChatCount() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(LiveTrackingActivity.this).result.id);
        Log.e("MapMap", "CHAT COUNT REQUEST" + map);
        Call<Map<String, String>> chatCount = apiInterface.getChatCount(map);
        chatCount.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "CHAT COUNT RESPONSE" + dataResponse);
                        if (data.get("count").equals("0")) {
                            binding.tvCounter.setVisibility(View.GONE);
                        } else {
                            binding.tvCounter.setVisibility(View.VISIBLE);
                            binding.tvCounter.setText(data.get("count"));
                        }

                    } else if (data.get("status").equals("0")) {
                        binding.tvCounter.setVisibility(View.GONE);
                        // Toast.makeText(TrackingActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }


    public void AlertDialogStatus(String msg,String status){

            AlertDialog.Builder  builder1 = new AlertDialog.Builder(LiveTrackingActivity.this);
            builder1.setMessage(msg);
            builder1.setCancelable(false);


            builder1.setPositiveButton(
                    getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if (status.equals("Finish")) {
                                startActivity(new Intent(LiveTrackingActivity.this,RateDriverAct.class)
                                        .putExtra("provider_id",DriverId)
                                        .putExtra("providerName",DriverName)
                                        .putExtra("providerImage",DriverImage)
                                        .putExtra("requestId",request_id));
                                finish();
                            }
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }


