package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

public class SadariResultDetailResponse extends BaseResponse {
    @SerializedName("data")
    public SadariResultDetail data;

    public static class SadariResultDetail {
        @SerializedName("ID_SADARI_RESULT")
        public String idSadariResult;
    }
}
