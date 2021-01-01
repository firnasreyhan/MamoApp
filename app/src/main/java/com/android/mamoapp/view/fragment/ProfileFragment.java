package com.android.mamoapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mamoapp.R;
import com.android.mamoapp.preference.AppPreference;
import com.android.mamoapp.view.activity.HistoryActivity;
import com.android.mamoapp.view.activity.SignInActivity;
import com.android.mamoapp.view.activity.UpdatePasswordUserActivity;
import com.android.mamoapp.view.activity.UpdateProfileUserActivity;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {
    private MaterialButton materialButtonLogout, materialButtonHistory, materialButtonProfileEdit, materialButtonPasswordEdit;
    private TextView textViewProfileName, textViewProfilePhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        materialButtonLogout = view.findViewById(R.id.materialButtonLogout);
        materialButtonHistory = view.findViewById(R.id.materialButtonHistory);
        materialButtonProfileEdit = view.findViewById(R.id.materialButtonProfileEdit);
        materialButtonPasswordEdit = view.findViewById(R.id.materialButtonPasswordEdit);
        textViewProfileName = view.findViewById(R.id.textViewProfileName);
        textViewProfilePhone = view.findViewById(R.id.textViewProfilePhone);

        textViewProfileName.setText(AppPreference.getUser(getContext()).name);
        textViewProfilePhone.setText(AppPreference.getUser(getContext()).phone);

        materialButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), HistoryActivity.class));
            }
        });

        materialButtonProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), UpdateProfileUserActivity.class));
            }
        });

        materialButtonPasswordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), UpdatePasswordUserActivity.class));
            }
        });

        materialButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreference.removeUser(v.getContext());
                Intent intent = new Intent(getContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        textViewProfileName.setText(AppPreference.getUser(getContext()).name);
        textViewProfilePhone.setText(AppPreference.getUser(getContext()).phone);
    }
}