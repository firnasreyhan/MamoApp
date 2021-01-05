package com.android.mamoapp.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.mamoapp.api.reponse.LoginResponse;
import com.google.gson.Gson;

public class AppPreference {
    static final String PREF = "PREF";
    static final String USER_PREF = "USER_PREF";
    static final String TOKEM_PREF = "TOKEN_PREF";

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

    public static void saveToken(Context context, String token){
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putString(TOKEM_PREF, token).apply();
    }

    public static String getToken(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(TOKEM_PREF)){
            return pref.getString(TOKEM_PREF, "");
        }

        return null;
    }

    public static void removeToken(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        if(pref.contains(TOKEM_PREF)){
            pref.edit().remove(TOKEM_PREF).apply();
        }
    }
}
