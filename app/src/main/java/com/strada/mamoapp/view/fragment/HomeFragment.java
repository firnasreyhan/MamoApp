package com.strada.mamoapp.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strada.mamoapp.R;
import com.strada.mamoapp.adapter.HeaderNewsAdapter;
import com.strada.mamoapp.adapter.NewsAdapter;
import com.strada.mamoapp.api.ApiClient;
import com.strada.mamoapp.api.ApiInterface;
import com.strada.mamoapp.api.reponse.NewsResponse;
import com.strada.mamoapp.api.reponse.NewsTrendingResponse;
import com.strada.mamoapp.view.activity.NewsListActivity;
import com.strada.mamoapp.view.activity.VideoDetailActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewHeaderNews, recyclerViewNews;
    private ShimmerFrameLayout shimmerFrameLayoutHeaderNews, shimmerFrameLayoutNews;
    private LinearLayout linearLayoutNews;
    private FrameLayout frameLayoutEmptyNews;
    private TextView textViewNewsList;
    private SwipeRefreshLayout swipeRefreshLayoutHome;
    private HeaderNewsAdapter headerNewsAdapter;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        apiInterface = ApiClient.getClient();
        recyclerViewHeaderNews = view.findViewById(R.id.recyclerViewHeaderNews);
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews);
        textViewNewsList = view.findViewById(R.id.textViewNewsList);
        shimmerFrameLayoutHeaderNews = view.findViewById(R.id.shimmerFrameLayoutHeaderNews);
        shimmerFrameLayoutNews = view.findViewById(R.id.shimmerFrameLayoutNews);
        swipeRefreshLayoutHome = view.findViewById(R.id.swipeRefreshLayoutHome);
        linearLayoutNews = view.findViewById(R.id.linearLayoutNews);
        frameLayoutEmptyNews = view.findViewById(R.id.frameLayoutEmptyNews);

        recyclerViewHeaderNews.setHasFixedSize(true);
        recyclerViewNews.setHasFixedSize(true);

        apiInterface.getNewsTrending(
                5
        ).enqueue(new Callback<NewsTrendingResponse>() {
            @Override
            public void onResponse(Call<NewsTrendingResponse> call, Response<NewsTrendingResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            setRecyclerViewHeaderNews(response.body().data);
                        } else {
                            linearLayoutNews.setVisibility(View.GONE);
                            frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                        }
                    } else {
                        linearLayoutNews.setVisibility(View.GONE);
                        frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                    }
                } else {
                    linearLayoutNews.setVisibility(View.GONE);
                    frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<NewsTrendingResponse> call, Throwable t) {
                linearLayoutNews.setVisibility(View.GONE);
                frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                alertErrorServer();
                Log.e("getNewsTrending", t.getMessage());
            }
        });

        apiInterface.getNews(
                "",
                3
        ).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            setRecyclerViewNews(response.body().data);
                        } else {
                            linearLayoutNews.setVisibility(View.GONE);
                            frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                        }
                    } else {
                        linearLayoutNews.setVisibility(View.GONE);
                        frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                    }
                } else {
                    linearLayoutNews.setVisibility(View.GONE);
                    frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                linearLayoutNews.setVisibility(View.GONE);
                frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                alertErrorServer();
                Log.e("news", t.getMessage());
            }
        });

        swipeRefreshLayoutHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayoutHeaderNews.startShimmer();
                shimmerFrameLayoutNews.startShimmer();
                shimmerFrameLayoutHeaderNews.setVisibility(View.VISIBLE);
                shimmerFrameLayoutNews.setVisibility(View.VISIBLE);
                linearLayoutNews.setVisibility(View.VISIBLE);
                recyclerViewHeaderNews.setVisibility(View.GONE);
                recyclerViewNews.setVisibility(View.GONE);
                frameLayoutEmptyNews.setVisibility(View.GONE);

                apiInterface.getNewsTrending(
                        5
                ).enqueue(new Callback<NewsTrendingResponse>() {
                    @Override
                    public void onResponse(Call<NewsTrendingResponse> call, Response<NewsTrendingResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status) {
                                if (!response.body().data.isEmpty()) {
                                    setRecyclerViewHeaderNews(response.body().data);
                                    shimmerFrameLayoutHeaderNews.stopShimmer();
                                    shimmerFrameLayoutHeaderNews.setVisibility(View.GONE);
                                    recyclerViewHeaderNews.setVisibility(View.VISIBLE);
                                } else {
                                    linearLayoutNews.setVisibility(View.GONE);
                                    frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                                }
                            } else {
                                linearLayoutNews.setVisibility(View.GONE);
                                frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                            }
                        } else {
                            linearLayoutNews.setVisibility(View.GONE);
                            frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                            alertErrorServer();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsTrendingResponse> call, Throwable t) {
                        Log.e("getNewsTrending", t.getMessage());
                    }
                });

                apiInterface.getNews(
                        "",
                        3).enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status) {
                                if (!response.body().data.isEmpty()) {
                                    setRecyclerViewNews(response.body().data);
                                    shimmerFrameLayoutNews.stopShimmer();
                                    shimmerFrameLayoutNews.setVisibility(View.GONE);
                                    recyclerViewNews.setVisibility(View.VISIBLE);
                                } else {
                                    linearLayoutNews.setVisibility(View.GONE);
                                    frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                                }
                            } else {
                                linearLayoutNews.setVisibility(View.GONE);
                                frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                            }
                        } else {
                            linearLayoutNews.setVisibility(View.GONE);
                            frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                            alertErrorServer();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        linearLayoutNews.setVisibility(View.GONE);
                        frameLayoutEmptyNews.setVisibility(View.VISIBLE);
                        alertErrorServer();
                        Log.e("news", t.getMessage());
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayoutHome.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        textViewNewsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), NewsListActivity.class));
            }
        });

        return view;
    }

    public void setRecyclerViewHeaderNews(ArrayList<NewsTrendingResponse.NewsTrendingModel> list) {
        headerNewsAdapter = new HeaderNewsAdapter(list);
        recyclerViewHeaderNews.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewHeaderNews.setAdapter(headerNewsAdapter);
        shimmerFrameLayoutHeaderNews.stopShimmer();
        shimmerFrameLayoutHeaderNews.setVisibility(View.GONE);
        recyclerViewHeaderNews.setVisibility(View.VISIBLE);
    }

    public void setRecyclerViewNews(ArrayList<NewsResponse.NewsModel> list) {
        newsAdapter = new NewsAdapter(list);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNews.setAdapter(newsAdapter);
        shimmerFrameLayoutNews.stopShimmer();
        shimmerFrameLayoutNews.setVisibility(View.GONE);
        recyclerViewNews.setVisibility(View.VISIBLE);
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(getContext())
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
        shimmerFrameLayoutHeaderNews.startShimmer();
        shimmerFrameLayoutNews.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutHeaderNews.stopShimmer();
        shimmerFrameLayoutNews.stopShimmer();
        super.onPause();
    }
}