package com.strada.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionResponse extends BaseResponse {
    @SerializedName("data")
    public ArrayList<QuestionModel> data;

    public static class QuestionModel implements Serializable {
        @SerializedName("ID_QUESTION")
        public String idQuestion;

        @SerializedName("CONTENT_QUESTION")
        public String contentQuestion;

        public boolean answer;
    }
}
