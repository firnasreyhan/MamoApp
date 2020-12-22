package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsTrendingResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<NewsTrendingModel> data;

    public static class NewsTrendingModel {
        @SerializedName("ID_NEWS")
        public String idNews;

        @SerializedName("TITLE_NEWS")
        public String titleNews;

        @SerializedName("TOTAL_NEWS_VIEW")
        public String totalNewsView;

        @SerializedName("TOTAL_NEWS_SHARE")
        public String totalNewsShare;

        @SerializedName("TRENDING")
        public String trending;
    }
}
