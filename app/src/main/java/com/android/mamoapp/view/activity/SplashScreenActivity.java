package com.android.mamoapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.android.mamoapp.R;
import com.android.mamoapp.preference.AppPreference;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTheme(R.style.ThemeSplashScreenMamoApp);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        int loadingTime = 3000;
        new Handler().postDelayed(() -> {
            if (AppPreference.getUser(getApplicationContext()) != null) {
                if (AppPreference.getUser(SplashScreenActivity.this).nameRole.equalsIgnoreCase("user")) {
                    Uri uri = getIntent().getData();
                    if (uri != null) {
                        String[] spPath= uri.getPath().split("/");
                        String idNews = spPath[spPath.length-1];
                        Intent intent = new Intent(SplashScreenActivity.this, UserMainActivity.class);
                        intent.putExtra("ID_NEWS", idNews);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, UserMainActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, DoctorMainActivity.class));
                }
            } else {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
            }
            finish();
        }, loadingTime);
    }
}