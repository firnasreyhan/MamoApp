package com.android.mamoapp.view.fragment;

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

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.HeaderNewsAdapter;
import com.android.mamoapp.adapter.NewsAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.NewsResponse;
import com.android.mamoapp.api.reponse.NewsTrendingResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewHeaderNews, recyclerViewNews;
    private ShimmerFrameLayout shimmerFrameLayoutHeaderNews, shimmerFrameLayoutNews;
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
        shimmerFrameLayoutHeaderNews = view.findViewById(R.id.shimmerFrameLayoutHeaderNews);
        shimmerFrameLayoutNews = view.findViewById(R.id.shimmerFrameLayoutNews);
        swipeRefreshLayoutHome = view.findViewById(R.id.swipeRefreshLayoutHome);

        recyclerViewHeaderNews.setHasFixedSize(true);
        recyclerViewNews.setHasFixedSize(true);

        apiInterface.getNewsTrending(
                5
        ).enqueue(new Callback<NewsTrendingResponse>() {
            @Override
            public void onResponse(Call<NewsTrendingResponse> call, Response<NewsTrendingResponse> response) {
                if (response.body().status) {
                    if (!response.body().data.isEmpty()) {
                        setRecyclerViewHeaderNews(response.body().data);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsTrendingResponse> call, Throwable t) {
                Log.e("getNewsTrending", t.getMessage());
            }
        });

        apiInterface.getNews(
                "",
                3
        ).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.body().status) {
                    if (!response.body().data.isEmpty()) {
                        setRecyclerViewNews(response.body().data);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("news", t.getMessage());
            }
        });

        swipeRefreshLayoutHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                headerNewsAdapter.clear();
                newsAdapter.clear();
                shimmerFrameLayoutHeaderNews.startShimmer();
                shimmerFrameLayoutNews.startShimmer();
                shimmerFrameLayoutHeaderNews.setVisibility(View.VISIBLE);
                shimmerFrameLayoutNews.setVisibility(View.VISIBLE);
                recyclerViewHeaderNews.setVisibility(View.GONE);
                recyclerViewNews.setVisibility(View.GONE);

                apiInterface.getNewsTrending(
                        5
                ).enqueue(new Callback<NewsTrendingResponse>() {
                    @Override
                    public void onResponse(Call<NewsTrendingResponse> call, Response<NewsTrendingResponse> response) {
                        if (response.body().status) {
                            if (!response.body().data.isEmpty()) {
                                headerNewsAdapter.addAll(response.body().data);
                                shimmerFrameLayoutHeaderNews.stopShimmer();
                                shimmerFrameLayoutHeaderNews.setVisibility(View.GONE);
                                recyclerViewHeaderNews.setVisibility(View.VISIBLE);
                            }
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
                        if (response.body().status) {
                            if (!response.body().data.isEmpty()) {
                                newsAdapter.addAll(response.body().data);
                                shimmerFrameLayoutNews.stopShimmer();
                                shimmerFrameLayoutNews.setVisibility(View.GONE);
                                recyclerViewNews.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
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