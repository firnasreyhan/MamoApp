package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsResponse extends BaseResponse {
    @SerializedName("data")
    public ArrayList<NewsModel> data;

    public static class NewsModel {
        @SerializedName("ID_NEWS")
        public String idNews;

        @SerializedName("NAME_CATEGORY")
        public String nameCategory;

        @SerializedName("TITLE_NEWS")
        public String titleNews;

        @SerializedName("NEWS_IMAGE")
        public String newsImage;

        @SerializedName("DATE_NEWS")
        public String dateNews;

        @SerializedName("EDITOR")
        public String editor;
    }
}
