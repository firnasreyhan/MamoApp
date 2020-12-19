package com.android.mamoapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mamoapp.R;
import com.android.mamoapp.view.activity.TestVideoActivity;
import com.google.android.material.button.MaterialButton;

public class TestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        MaterialButton materialButtonTestVideo = view.findViewById(R.id.materialButtonTest);

        materialButtonTestVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), TestVideoActivity.class));
            }
        });

        return view;
    }
}