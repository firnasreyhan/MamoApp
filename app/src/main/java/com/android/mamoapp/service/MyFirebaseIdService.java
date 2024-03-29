package com.android.mamoapp.service;

import com.android.mamoapp.preference.AppPreference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String userKey = AppPreference.getUser(getApplicationContext()).email;
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(userKey != null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        String userKey = AppPreference.getUser(getApplicationContext()).email;
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("MamoApp").child("Token").child(userKey).setValue(token);
    }
}