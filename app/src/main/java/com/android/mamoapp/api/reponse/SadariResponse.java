package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

public class SadariResponse extends BaseResponse {
    @SerializedName("data")
    public SadariModel data;

    public static class SadariModel {
        @SerializedName("ID_SADARI")
        public String idSadari;
    }
}
