package com.android.mamoapp.api;

import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.api.reponse.SadariDetailResponse;
import com.android.mamoapp.api.reponse.SadariListResponse;
import com.android.mamoapp.api.reponse.LoginResponse;
import com.android.mamoapp.api.reponse.NewsDetailResponse;
import com.android.mamoapp.api.reponse.NewsResponse;
import com.android.mamoapp.api.reponse.NewsTrendingResponse;
import com.android.mamoapp.api.reponse.QuestionResponse;
import com.android.mamoapp.api.reponse.SadariResponse;
import com.android.mamoapp.api.reponse.SadariResultDetailResponse;
import com.android.mamoapp.api.reponse.SadariResultResponse;
import com.android.mamoapp.api.reponse.VideoDetailResponse;
import com.android.mamoapp.api.reponse.VideoResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("user/login")
    @FormUrlEncoded
    Call<LoginResponse> postLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("user/register")
    @FormUrlEncoded
    Call<BaseResponse> postRegister(
            @Field("email") String email,
            @Field("password") String password,
            @Field("idRole") String idRole,
            @Field("name") String name,
            @Field("dateBirth") String dateBirth,
            @Field("phone") String phone
    );

    @POST("sadari")
    @FormUrlEncoded
    Call<SadariResponse> postSadari(
            @Field("email") String email,
            @Field("dateSadari") String dateSadari
    );

    @POST("sadari/detail")
    @FormUrlEncoded
    Call<BaseResponse> postDetailSadari(
            @Field("idSadari") String idSadari,
            @Field("idQuestion") String idQuestion,
            @Field("answer") boolean answer
    );

    @POST("news/click")
    @FormUrlEncoded
    Call<BaseResponse> postViewNews(
            @Field("email") String email,
            @Field("idNews") String idNews
    );

    @POST("news/share")
    @FormUrlEncoded
    Call<BaseResponse> postShareNews(
            @Field("email") String email,
            @Field("idNews") String idNews
    );

    @POST("sadari/resultDetail")
    @FormUrlEncoded
    Call<SadariResultDetailResponse> postResultDetailSadari(
            @Field("idSadari") String idSadari,
            @Field("email") String email,
            @Field("contentResult") String contentResult,
            @Field("dateResult") String dateResult
    );

    @PUT("sadari/result")
    @FormUrlEncoded
    Call<BaseResponse> putResultSadari(
            @Field("idSadari") String idSadari,
            @Field("isIndicated") boolean isIndicated
    );

    @PUT("user/profile")
    @FormUrlEncoded
    Call<BaseResponse> putProfile(
            @Field("email") String email,
            @Field("name") String name,
            @Field("dateBirth") String dateBirth,
            @Field("phone") String phone
    );

    @PUT("user/password")
    @FormUrlEncoded
    Call<BaseResponse> putPassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("news")
    Call<NewsResponse> getNews(
            @Query("search") String search,
            @Query("limit") int limit
    );

    @GET("news/detail/{idNews}")
    Call<NewsDetailResponse> getNewsDetail(
            @Path("idNews") String idNews
    );

    @GET("news/trending")
    Call<NewsTrendingResponse> getNewsTrending(
            @Query("limit") int limit
    );

    @GET("video")
    Call<VideoResponse> getVideo();

    @GET("video/detail/{idVideo}")
    Call<VideoDetailResponse> getVideoDetail(
            @Path("idVideo") String idVideo
    );

    @GET("sadari/question")
    Call<QuestionResponse> getQuestion();

    @GET("sadari")
    Call<SadariListResponse> getSadariList(
            @Query("email") String email
    );

    @GET("sadari/detail/{idSadari}")
    Call<SadariDetailResponse> getSadariDetail(
            @Path("idSadari") String idSadari
    );

    @GET("sadari/resultDetail/{idSadari}")
    Call<SadariResultResponse> getSadariResult(
            @Path("idSadari") String idSadari
    );
}
