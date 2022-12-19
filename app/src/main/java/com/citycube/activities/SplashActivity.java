package com.citycube.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.citycube.MainActivity;
import com.citycube.R;
import com.citycube.utility.DataManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (DataManager.getInstance().getUserData(getApplicationContext()) != null &&
                        DataManager.getInstance().getUserData(getApplicationContext()).result != null &&
                        !DataManager.getInstance().getUserData(getApplicationContext())
                                .result.id.equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }



            }
        },3000);

    }
}