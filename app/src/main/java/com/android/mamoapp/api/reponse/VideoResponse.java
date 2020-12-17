package com.android.mamoapp.api.reponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoResponse extends BaseResponse {
    @SerializedName("data")
    public ArrayList<VideoModel> data;

    public static class VideoModel {
        @SerializedName("ID_VIDEO")
        public String idVideo;

        @SerializedName("TITLE")
        public String title;

        @SerializedName("DESCRIPTION")
        public String description;

        @SerializedName("DATE_PUBLISHED")
        public String datePublisher;

        @SerializedName("URL_DEFAULT_THUMBNAIL")
        public String urlDefaultThumbnail;

        @SerializedName("URL_MEDIUM_THUMBNAIL")
        public String urlMediumThumbnail;

        @SerializedName("URL_HIGH_THUMBNAIL")
        public String urlHighThumbnail;

        @SerializedName("STATUS_PUBLISHED")
        public String statusPublished;
    }
}
