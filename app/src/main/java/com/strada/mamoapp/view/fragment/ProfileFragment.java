package com.strada.mamoapp.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.strada.mamoapp.R;
import com.strada.mamoapp.preference.AppPreference;
import com.strada.mamoapp.view.activity.HistoryActivity;
import com.strada.mamoapp.view.activity.PrivacyPolicyActivity;
import com.strada.mamoapp.view.activity.SignInActivity;
import com.strada.mamoapp.view.activity.UpdatePasswordUserActivity;
import com.strada.mamoapp.view.activity.UpdateProfileUserActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private ShapeableImageView shapeableImageViewAvatar;
    private MaterialButton materialButtonLogout, materialButtonHistory, materialButtonProfileEdit, materialButtonPasswordEdit, materialButtonPrivacyPolicy;
    private TextView textViewProfileName, textViewProfilePhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        shapeableImageViewAvatar = view.findViewById(R.id.shapeableImageViewAvatar);
        materialButtonLogout = view.findViewById(R.id.materialButtonLogout);
        materialButtonHistory = view.findViewById(R.id.materialButtonHistory);
        materialButtonProfileEdit = view.findViewById(R.id.materialButtonProfileEdit);
        materialButtonPasswordEdit = view.findViewById(R.id.materialButtonPasswordEdit);
        materialButtonPrivacyPolicy = view.findViewById(R.id.materialButtonPrivacyPolicy);
        textViewProfileName = view.findViewById(R.id.textViewProfileName);
        textViewProfilePhone = view.findViewById(R.id.textViewProfilePhone);

        textViewProfileName.setText(AppPreference.getUser(getContext()).name);
        textViewProfilePhone.setText(AppPreference.getUser(getContext()).phone);

        Glide.with(getContext())
                .load(AppPreference.getUser(getContext()).profilpicUser)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .placeholder(R.drawable.img_default_video)
                .into(shapeableImageViewAvatar);

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

        materialButtonPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), PrivacyPolicyActivity.class));
            }
        });

        materialButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userKey = AppPreference.getUser(v.getContext()).email.replaceAll("[-+.^:,]","");
                FirebaseDatabase.getInstance().getReference("MamoApp").child("Token").child(userKey).removeValue();
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
        Glide.with(getContext())
                .load(AppPreference.getUser(getContext()).profilpicUser)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .placeholder(R.drawable.img_icon)
                .into(shapeableImageViewAvatar);
        textViewProfileName.setText(AppPreference.getUser(getContext()).name);
        textViewProfilePhone.setText(AppPreference.getUser(getContext()).phone);

        Log.e("name", AppPreference.getUser(getContext()).name);
    }
}