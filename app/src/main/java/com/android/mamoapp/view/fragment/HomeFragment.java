package com.android.mamoapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.HeaderNewsAdapter;
import com.android.mamoapp.adapter.NewsAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.NewsDetailResponse;
import com.android.mamoapp.api.reponse.NewsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewHeaderNews, recyclerViewNews;
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

        apiInterface.news().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.body().status) {
                    if (!response.body().data.isEmpty()) {
                        setRecyclerViewHeaderNews(response.body().data);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("news", t.getMessage());
            }
        });

        apiInterface.news().enqueue(new Callback<NewsResponse>() {
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

        return view;
    }

    public void setRecyclerViewHeaderNews(ArrayList<NewsResponse.NewsModel> list) {
        headerNewsAdapter = new HeaderNewsAdapter(list);
        recyclerViewHeaderNews.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewHeaderNews.setAdapter(headerNewsAdapter);
    }

    public void setRecyclerViewNews(ArrayList<NewsResponse.NewsModel> list) {
        newsAdapter = new NewsAdapter(list);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNews.setAdapter(newsAdapter);
    }
}