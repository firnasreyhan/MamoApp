package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.android.mamoapp.R;
import com.android.mamoapp.preference.AppPreference;
import com.google.android.material.button.MaterialButton;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private WebView webViewPrivacyPolicy;
    private MaterialButton materialButtonAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        webViewPrivacyPolicy = findViewById(R.id.webViewPrivacyPolicy);
        materialButtonAgree = findViewById(R.id.materialButtonAgree);

        webViewPrivacyPolicy.loadUrl("file:///android_asset/privacy_policy.html");

//        if (AppPreference.getPolicy(this)) {
//            materialButtonAgree.setVisibility(View.GONE);
//        } else {
//            materialButtonAgree.setVisibility(View.VISIBLE);
//        }

        materialButtonAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppPreference.savePolicy(v.getContext(), true);
                startActivity(new Intent(v.getContext(), TestVideoActivity.class));
                finish();
            }
        });
    }
}