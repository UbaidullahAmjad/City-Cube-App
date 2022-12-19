package com.citycube.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.citycube.R;
import com.citycube.databinding.ActivityHomeBinding;
import com.citycube.map.DrawPollyLine;
import com.citycube.retrofit.Constant;
import com.citycube.utility.App;
import com.citycube.utility.DataManager;
import com.citycube.utility.GPSTracker;
import com.citycube.utility.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {
    public String TAG ="HomeActivity";
    private GoogleMap mMap;
    ActivityHomeBinding binding;
    GPSTracker gpsTracker;
    SupportMapFragment mapFragment ;
    String strLat, strLng;
    public static final int LOCATION_REQUEST = 1000;
    int AUTOCOMPLETE_REQUEST_CODE_PICK_UP = 101, AUTOCOMPLETE_REQUEST_CODE_DESTINATION = 201;
    double departlat = 0.0, departlon = 0.0, arrivallat = 0.0, arrivallon = 0.0;
    String departureAddress = "", arrivalAddress = "";
    private LatLng DepartureLatLng, ArrivalLatLng;
    private PolylineOptions lineOptions;
    private MarkerOptions PicUpMarker, DropOffMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        gpsTracker = new GPSTracker(HomeActivity.this);
        initViews();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      //  getLocation();


    }


    private void initViews() {
        if (!Places.isInitialized()) {
            Places.initialize(HomeActivity.this, getString(R.string.place_api_key));
        }

        PicUpMarker = new MarkerOptions().title("Departure Address")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
        DropOffMarker = new MarkerOptions().title("Arrival Address")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));




        binding.dashboard.ivMenu.setOnClickListener(v -> { navmenu(); });

        binding.drawerLayout.rlHome.setOnClickListener(v -> { binding.drawer.closeDrawer(GravityCompat.START); });

        binding.drawerLayout.rlMyProfile.setOnClickListener(v -> { startActivity(new Intent(this,MyProfileActivity.class)); });

        binding.drawerLayout.rlPromotion.setOnClickListener(v -> { startActivity(new Intent(this,PromotionActivity.class)); });

        binding.drawerLayout.rlOrderHistory.setOnClickListener(v -> { startActivity(new Intent(this,MyBookingsAct.class)); });

        binding.drawerLayout.rlShare.setOnClickListener(v -> { startActivity(new Intent(this,ShareActivity.class)); });

        binding.drawerLayout.rlLogout.setOnClickListener(v -> { SessionManager.clear(HomeActivity.this,DataManager.getInstance().getUserData(HomeActivity.this).result.id); });


        binding.dashboard.rlDeparture.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(HomeActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_PICK_UP);

        });

        binding.dashboard.rlArrival.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(HomeActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_DESTINATION);

        });


        binding.dashboard.btnContinue.setOnClickListener(v -> { if (String.valueOf(departlat).equals("0.0"))
            App.showToast(HomeActivity.this, getString(R.string.please_enter_departure_location), Toast.LENGTH_SHORT);

        else if (String.valueOf(arrivallat).equals("0.0")) {
            App.showToast(HomeActivity.this, getString(R.string.please_enter_arrival_location), Toast.LENGTH_SHORT);
        } else {
            startActivity(new Intent(this, ReservationActivity.class).putExtra("departLat", departlat + "")
                    .putExtra("departLon", departlon + "").putExtra("arrivalLat", arrivallat + "")
                    .putExtra("arrivalLon", arrivallon + "").putExtra("departureAddress", departureAddress)
                    .putExtra("arrivalAddress", arrivalAddress));
        } });


    }

    public void navmenu() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START);
        } else {
            binding.drawer.openDrawer(GravityCompat.START);
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
      //  getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDataSet();

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            Log.e("Latittude====", gpsTracker.getLatitude() + "Latittude===="+gpsTracker.getLongitude() );
            strLat = Double.toString(gpsTracker.getLatitude());
            strLng = Double.toString(gpsTracker.getLongitude());
             setLocation();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.e(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
            Log.e(TAG, "Place: " + place.getAddress() + ", " + place.getLatLng());
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE_PICK_UP) {

                binding.dashboard.tvDeparture.setText(place.getAddress());
                departlat = place.getLatLng().latitude;
                departlon = place.getLatLng().longitude;
                Log.e("LatLong====", departlat + " ," + departlat);
                DepartureLatLng = new LatLng(departlat, departlon);
                departureAddress = place.getAddress();
            }


            if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DESTINATION) {
                // pickUpoLatlong = place.getLatLng();
                binding.dashboard.tvArrival.setText(place.getAddress());
                arrivallat = place.getLatLng().latitude;
                arrivallon = place.getLatLng().longitude;
                ArrivalLatLng = new LatLng(arrivallat, arrivallon);
                DrawPolyLine();
                arrivalAddress = place.getAddress();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gpsTracker = new GPSTracker(HomeActivity.this);

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude());
                    strLng = Double.toString(gpsTracker.getLongitude());
                    setLocation();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }


        }
    }

    private void setLocation() {
        gpsTracker = new GPSTracker(HomeActivity.this);
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        if (mMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
          /*  mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
            });*/
            mapFragment.getMapAsync(this);
        }
        PicUpMarker.position(sydney);
        mMap.addMarker(PicUpMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
    }

    private void DrawPolyLine() {
        DrawPollyLine.get(this).setOrigin(DepartureLatLng)
                .setDestination(ArrivalLatLng).execute(new DrawPollyLine.onPolyLineResponse() {
            @Override
            public void Success(ArrayList<LatLng> latLngs) {
                mMap.clear();
                lineOptions = new PolylineOptions();
                lineOptions.addAll(latLngs);
                lineOptions.width(10);
                lineOptions.color(R.color.black);
                lineOptions.startCap(new SquareCap());
                lineOptions.endCap(new SquareCap());
                lineOptions.jointType(ROUND);
                mMap.addPolyline(lineOptions);
                AddDefaultMarker();

            }
        });
    }


    public void AddDefaultMarker() {
        if (mMap != null) {
            mMap.clear();
            if (lineOptions != null)
                mMap.addPolyline(lineOptions);
            if (DepartureLatLng != null) {
                PicUpMarker.position(DepartureLatLng);
                mMap.addMarker(PicUpMarker);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(DepartureLatLng)));
            }
            if (ArrivalLatLng != null) {
                DropOffMarker.position(ArrivalLatLng);
                mMap.addMarker(DropOffMarker);
            }

        }
    }


    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(14).build();
    }


    public void userDataSet(){
        if(SessionManager.readString(HomeActivity.this, Constant.USER_INFO,"").equals("")){
            binding.drawerLayout.tvUserName.setText("Hi Guest");
            binding.drawerLayout.tvEmail.setText("guest@gmail.com");
            binding.drawerLayout.rlOrderHistory.setVisibility(View.GONE);
            binding.drawerLayout.rlPromotion.setVisibility(View.GONE);
            binding.drawerLayout.rlShare.setVisibility(View.GONE);
            binding.drawerLayout.rlLogout.setVisibility(View.GONE);
        }
        else {
            binding.drawerLayout.tvUserName.setText(DataManager.getInstance().getUserData(HomeActivity.this).result.userName);
            binding.drawerLayout.tvEmail.setText(DataManager.getInstance().getUserData(HomeActivity.this).result.email);
            binding.drawerLayout.rlOrderHistory.setVisibility(View.VISIBLE);
            binding.drawerLayout.rlPromotion.setVisibility(View.VISIBLE);
            binding.drawerLayout.rlShare.setVisibility(View.VISIBLE);
            binding.drawerLayout.rlLogout.setVisibility(View.VISIBLE);

        }
    }

}