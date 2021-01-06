package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.NewsAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.NewsResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewNewsList;
    private ShimmerFrameLayout shimmerFrameLayoutNewsList;
    private FrameLayout frameLayoutEmptySearch;
    private TextInputEditText textInputEditTextSearch;
    private TextInputLayout textInputLayoutSearch;
    private NewsAdapter newsAdapter;

    private boolean cekSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_news_list);

        apiInterface = ApiClient.getClient();
        recyclerViewNewsList = findViewById(R.id.recyclerViewNewsList);
        shimmerFrameLayoutNewsList = findViewById(R.id.shimmerFrameLayoutNewsList);
        frameLayoutEmptySearch = findViewById(R.id.frameLayoutEmptySearch);
        textInputEditTextSearch = findViewById(R.id.textInputEditTextSearch);
        textInputLayoutSearch = findViewById(R.id.textInputLayoutSearch);

        apiInterface.getNews(
                "",
                0
        ).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            setRecyclerViewNewsList(response.body().data);
                            showNotEmpty();
                        } else {
                            showEmpty();
                        }
                    } else {
                        showEmpty();
                    }
                } else {
                    showEmpty();
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                alertErrorServer();
                Log.e("getNews", t.getMessage());
            }
        });

        textInputEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchNews();
                    cekSearch = true;
                }
                return false;
            }
        });

        textInputLayoutSearch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputEditTextSearch.getText().clear();
                if (cekSearch) {
                    searchNews();
                    cekSearch = false;
                }
            }
        });
    }

    public void setRecyclerViewNewsList(ArrayList<NewsResponse.NewsModel> list) {
        newsAdapter = new NewsAdapter(list);
        recyclerViewNewsList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNewsList.setAdapter(newsAdapter);
    }

    public void searchNews() {
        shimmerFrameLayoutNewsList.startShimmer();
        shimmerFrameLayoutNewsList.setVisibility(View.VISIBLE);
        recyclerViewNewsList.setVisibility(View.GONE);

        apiInterface.getNews(
                textInputEditTextSearch.getText().toString(),
                0
        ).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            setRecyclerViewNewsList(response.body().data);
                            showNotEmpty();
                        } else {
                            showEmpty();
                        }
                    } else {
                        showEmpty();
                    }
                } else {
                    showEmpty();
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("getNews", t.getMessage());
            }
        });
    }

    public void showEmpty() {
        shimmerFrameLayoutNewsList.stopShimmer();
        shimmerFrameLayoutNewsList.setVisibility(View.GONE);
        frameLayoutEmptySearch.setVisibility(View.VISIBLE);
    }

    public void showNotEmpty() {
        shimmerFrameLayoutNewsList.stopShimmer();
        shimmerFrameLayoutNewsList.setVisibility(View.GONE);
        recyclerViewNewsList.setVisibility(View.VISIBLE);
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(NewsListActivity.this)
                .setTitle("Pesan")
                .setMessage("Terjadi kesalahan pada server, silahkan coba beberapa saat lagi")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayoutNewsList.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutNewsList.stopShimmer();
        super.onPause();
    }
}