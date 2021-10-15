package com.strada.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SadariListResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<SadariListModel> data;

    public static class SadariListModel {
        @SerializedName("ID_SADARI")
        public String idSadari;

        @SerializedName("NAME")
        public String name;

        @SerializedName("EMAIL")
        public String email;

        @SerializedName("DATE_SADARI")
        public String dateSadari;

        @SerializedName("IS_CHECKED")
        public String isChecked;

        @SerializedName("IS_INDICATED")
        public String isIndicated;
    }
}
