package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.mamoapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int loadingTime = 3000;
        new Handler().postDelayed(() -> {
//            if (AppPreference.getUser(getApplicationContext()) != null) {
//                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//            } else {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
//            }
            finish();
        }, loadingTime);
    }
}