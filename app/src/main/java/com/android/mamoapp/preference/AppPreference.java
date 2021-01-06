package com.android.mamoapp.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.mamoapp.api.reponse.LoginResponse;
import com.google.gson.Gson;

public class AppPreference {
    static final String PREF = "PREF";
    static final String USER_PREF = "USER_PREF";
    static final String POLICY_PREF = "POLICY_PREF";

    public static void saveUser(Context context, LoginResponse.LoginModel user){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putString(USER_PREF, new Gson().toJson(user)).apply();
    }

    public static LoginResponse.LoginModel getUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(USER_PREF)){
            Gson gson = new Gson();

            return gson.fromJson(pref.getString(USER_PREF, ""), LoginResponse.LoginModel.class);
        }

        return null;
    }

    public static void removeUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(USER_PREF)){
            pref.edit().remove(USER_PREF).apply();
        }
    }

    public static void savePolicy(Context context, boolean status){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putBoolean(POLICY_PREF, status).apply();
    }

    public static boolean getPolicy(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(POLICY_PREF)){
            return pref.getBoolean(POLICY_PREF, false);
        }

        return false;
    }

    public static void removePolicy(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(POLICY_PREF)){
            pref.edit().remove(POLICY_PREF).apply();
        }
    }
}
