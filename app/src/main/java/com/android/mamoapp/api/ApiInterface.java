package com.android.mamoapp.api;

import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.api.reponse.LoginResponse;
import com.android.mamoapp.api.reponse.NewsDetailResponse;
import com.android.mamoapp.api.reponse.NewsResponse;
import com.android.mamoapp.api.reponse.VideoResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("user/login")
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("user/register")
    @FormUrlEncoded
    Call<BaseResponse> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("idRole") String idRole,
            @Field("name") String name,
            @Field("phone") String phone
    );

    @GET("news")
    Call<NewsResponse> news();

    @GET("news/detail/{idNews}")
    Call<NewsDetailResponse> newsDetail(@Path("idNews") String idNews);

    @GET("video")
    Call<VideoResponse> video();
}
