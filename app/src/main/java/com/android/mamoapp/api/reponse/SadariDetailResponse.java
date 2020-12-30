package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SadariDetailResponse extends BaseResponse{
    @SerializedName("data")
    public SadariDetail data;

    public static class SadariDetail {
        @SerializedName("data_sadari")
        public ArrayList<DataSadari> dataSadari;

        @SerializedName("data_sadari_detail")
        public ArrayList<DataSadariDetail> dataSadariDetail;

        public static class DataSadari {
            @SerializedName("ID_SADARI")
            public String idSadari;

            @SerializedName("NAME")
            public String name;

            @SerializedName("EMAIL")
            public String email;

            @SerializedName("PROFILEPIC_USER")
            public String profilePicUser;

            @SerializedName("PHONE")
            public String phone;

            @SerializedName("DATE_BIRTH")
            public String dateBirth;

            @SerializedName("GENDER")
            public String gender;

            @SerializedName("DATE_SADARI")
            public String dateSadari;

            @SerializedName("IS_CHECKED")
            public String isChecked;

            @SerializedName("IS_INDICATED")
            public String isIndicated;
        }

        public static class DataSadariDetail {
            @SerializedName("CONTENT_QUESTION")
            public String contentQuestion;

            @SerializedName("ANSWER")
            public String answer;
        }
    }
}
