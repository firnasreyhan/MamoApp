package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResponse extends BaseResponse {
    @SerializedName("data")
    public LoginModel data;

    public static class LoginModel {
        @SerializedName("EMAIL")
        public String email;

        @SerializedName("NAME")
        public String name;

        @SerializedName("NAME_ROLE")
        public String nameRole;

        @SerializedName("PROFILEPIC_USER")
        public String profilpicUser;

        @SerializedName("PHONE")
        public String phone;

        @SerializedName("DATE_BIRTH")
        public String dateBirth;

        public String password;
    }
}
