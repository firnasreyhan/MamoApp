package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
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
import com.android.mamoapp.api.reponse.NewsDetailResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private TextView textViewCategoryNews, textViewDateNews, textViewTitleNews, textViewEditorNews, textViewContentNews;
    private ImageView imageViewImageNews;
    private WebView webViewContentNews;

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
        imageViewImageNews = findViewById(R.id.imageViewImageNews);
        webViewContentNews = findViewById(R.id.webViewContentNews);

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
        textViewDateNews.setText(model.dateNews);
        textViewEditorNews.setText(model.editor);
        textViewTitleNews.setText(model.titleNews);

        String html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body width=\"100%\" height=\"auto\">" + model.contentNews + "</body></html>";
        webViewContentNews.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        webViewContentNews.setWebViewClient(new WebViewClient());

        //textViewContentNews.setText(Html.fromHtml(Html.fromHtml(model.contentNews).toString()));

        Glide.with(this)
                .load("https://ilham.kristomoyo.com/images/news/" + model.newsImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
//                .placeholder(R.drawable.mqdefault)
                .into(imageViewImageNews);
    }
}