package com.strada.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsDetailResponse extends BaseResponse{
    @SerializedName("data")
    public NewsDetailModel data;

    public static class NewsDetailModel {
        @SerializedName("ID_NEWS")
        public String idNews;

        @SerializedName("NAME_CATEGORY")
        public String nameCategory;

        @SerializedName("TITLE_NEWS")
        public String titleNews;

        @SerializedName("CONTENT_NEWS")
        public String contentNews;

        @SerializedName("VIEWS_COUNT")
        public String viewsCount;

        @SerializedName("SHARES_COUNT")
        public String sharesCount;

        @SerializedName("DATE_NEWS")
        public String dateNews;

        @SerializedName("NEWS_IMAGE")
        public String newsImage;

        @SerializedName("EDITOR")
        public String editor;

        @SerializedName("VERIFICATOR")
        public String verificator;
    }
}
