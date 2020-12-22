package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.api.reponse.NewsDetailResponse;
import com.android.mamoapp.preference.AppPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private TextView textViewCategoryNews, textViewDateNews, textViewTitleNews, textViewEditorNews, textViewContentNews, textViewViewCountNews, textViewShareCountNews;
    private ImageView imageViewImageNews;
    private WebView webViewContentNews;
    private ShimmerFrameLayout shimmerFrameLayoutDetailNews;
    private NestedScrollView nestedScrollViewDetailNews;

    private String idNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        idNews = getIntent().getStringExtra("ID_NEWS");

        apiInterface = ApiClient.getClient();
        textViewCategoryNews = findViewById(R.id.textViewCategoryNews);
        textViewDateNews = findViewById(R.id.textViewDateNews);
        textViewTitleNews = findViewById(R.id.textViewTitleNews);
        textViewEditorNews = findViewById(R.id.textViewEditorNews);
        textViewContentNews = findViewById(R.id.textViewContentNews);
        textViewViewCountNews = findViewById(R.id.textViewViewCountNews);
        textViewShareCountNews = findViewById(R.id.textViewShareCountNews);
        imageViewImageNews = findViewById(R.id.imageViewImageNews);
        webViewContentNews = findViewById(R.id.webViewContentNews);
        nestedScrollViewDetailNews = findViewById(R.id.nestedScrollViewDetailNews);
        shimmerFrameLayoutDetailNews = findViewById(R.id.shimmerFrameLayoutDetailNews);

//        webViewContentNews.setInitialScale(1);
//        webViewContentNews.getSettings().setLoadWithOverviewMode(true);
//        webViewContentNews.getSettings().setUseWideViewPort(true);
        webViewContentNews.getSettings().setJavaScriptEnabled(true);
        webViewContentNews.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webViewContentNews.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webViewContentNews.getSettings().setPluginState(WebSettings.PluginState.ON);
        webViewContentNews.getSettings().setSupportMultipleWindows(true);
        webViewContentNews.setWebChromeClient(new WebChromeClient());
        webViewContentNews.setHorizontalScrollBarEnabled(false);
        webViewContentNews.getSettings().setLoadWithOverviewMode(true);
        webViewContentNews.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webViewContentNews.getSettings().setBuiltInZoomControls(false);

        apiInterface.postViewNews(
                AppPreference.getUser(this).email,
                idNews
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.e("postViewNews", response.body().message);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("postViewNews", t.getMessage());
            }
        });

        apiInterface.getNewsDetail(
                idNews
        ).enqueue(new Callback<NewsDetailResponse>() {
            @Override
            public void onResponse(Call<NewsDetailResponse> call, Response<NewsDetailResponse> response) {
                if (response.body().status) {
                    setData(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<NewsDetailResponse> call, Throwable t) {
                Log.e("getNewsDetail", t.getMessage());
            }
        });
    }

    public void setData(NewsDetailResponse.NewsDetailModel model) {
        textViewCategoryNews.setText(model.nameCategory);

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date = oldFormat.parse(model.dateNews);
            textViewDateNews.setText(newFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        textViewEditorNews.setText(model.editor);
        textViewTitleNews.setText(model.titleNews);
        textViewViewCountNews.setText(model.viewsCount);
        textViewShareCountNews.setText(model.sharesCount);

        String html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body width=\"100%\" height=\"auto\">" + model.contentNews + "</body></html>";
        webViewContentNews.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        webViewContentNews.setWebViewClient(new WebViewClient());

        //textViewContentNews.setText(Html.fromHtml(Html.fromHtml(model.contentNews).toString()));

        Glide.with(this)
                .load("https://ilham.kristomoyo.com/images/news/" + model.newsImage)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .placeholder(R.drawable.img_default_video)
                .into(imageViewImageNews);

        shimmerFrameLayoutDetailNews.stopShimmer();
        shimmerFrameLayoutDetailNews.setVisibility(View.GONE);
        nestedScrollViewDetailNews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayoutDetailNews.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutDetailNews.stopShimmer();
        super.onPause();
    }
}