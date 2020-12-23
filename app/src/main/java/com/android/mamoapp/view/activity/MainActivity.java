package com.android.mamoapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.mamoapp.R;
import com.android.mamoapp.view.fragment.HomeFragment;
import com.android.mamoapp.view.fragment.ProfileFragment;
import com.android.mamoapp.view.fragment.TestFragment;
import com.android.mamoapp.view.fragment.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private Fragment fragmentActive, fragmentHome, fragmentVideo, fragmentTest, fragmentProfile;
    private boolean doubleBackToExit;

    private String idNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idNews = getIntent().getStringExtra("ID_NEWS");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        fragmentHome = new HomeFragment();
        fragmentVideo = new VideoFragment();
        fragmentTest = new TestFragment();
        fragmentProfile = new ProfileFragment();
        fragmentActive = fragmentHome;

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager.beginTransaction().add(R.id.frameLayoutFragment, fragmentProfile, "Profil").hide(fragmentProfile).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayoutFragment, fragmentTest, "Diagnosis").hide(fragmentTest).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayoutFragment, fragmentVideo, "Video").hide(fragmentVideo).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayoutFragment, fragmentHome, "Home").commit();

        if (idNews != null) {
            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtra("ID_NEWS", idNews);
            startActivity(intent);
        }

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentHome).commit();
//                    if (fragmentActive == fragmentHome) {
//                        ((HomeFragment) fragmentHome).scrollUp();
//                    }
                    fragmentActive = fragmentHome;
                    doubleBackToExit = true;
                    return true;
                case R.id.menu_video:
                    fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentVideo).commit();
//                    if (fragmentActive == fragmentVideo) {
//                        ((VideoFragment) fragmentVideo).scrollUp();
//                    }
                    fragmentActive = fragmentVideo;
                    doubleBackToExit = false;
                    return true;
                case R.id.menu_test:
                    //((VideoFragment) fragmentVideo).pauseVideo();
                    fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentTest).commit();
//                    if (fragmentActive == fragmentTest) {
//                        ((TestFragment) fragmentTest).scrollUp();
//                    }
                    fragmentActive = fragmentTest;
                    doubleBackToExit = false;
                    return true;
                case R.id.menu_profile:
                    fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentProfile).commit();
                    fragmentActive = fragmentTest;
                    doubleBackToExit = false;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExit = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar.", Toast.LENGTH_SHORT).show();
    }
}