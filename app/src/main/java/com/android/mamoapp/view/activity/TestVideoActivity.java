package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.mamoapp.R;
import com.google.android.material.button.MaterialButton;

public class TestVideoActivity extends AppCompatActivity {
    private MaterialButton materialButtonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_test_video);

        materialButtonTest = findViewById(R.id.materialButtonTest);

        materialButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), TestActivity.class));
                finish();
            }
        });
    }
}