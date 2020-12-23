package com.android.mamoapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mamoapp.R;
import com.android.mamoapp.preference.AppPreference;
import com.android.mamoapp.view.activity.SignInActivity;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {
    private MaterialButton materialButtonLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        materialButtonLogout = view.findViewById(R.id.materialButtonLogout);

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
}