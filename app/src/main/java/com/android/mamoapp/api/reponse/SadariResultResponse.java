package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

public class SadariResultResponse extends BaseResponse {
    @SerializedName("data")
    public SadariResult data;

    public static class SadariResult {
        @SerializedName("DOCTOR_NAME")
        public String doctorName;

        @SerializedName("DOCTOR_EMAIL")
        public String doctorEmail;

        @SerializedName("IMG1_SADARI_RESULT")
        public String img1SadariResult;

        @SerializedName("IMG2_SADARI_RESULT")
        public String img2SadariResult;

        @SerializedName("CONTENT_SADARI_RESULT")
        public String contentSadariResult;

        @SerializedName("DATE_SADARI_RESULT")
        public String dateSadariResult;
    }
}
