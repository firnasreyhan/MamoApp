package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.HistoryAdapter;
import com.android.mamoapp.adapter.SadariAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariListResponse;
import com.android.mamoapp.preference.AppPreference;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private SwipeRefreshLayout swipeRefreshLayoutHistory;
    private ShimmerFrameLayout shimmerFrameLayoutHistory;
    private FrameLayout frameLayoutEmptyHistory;
    private RecyclerView recyclerViewHistory;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        apiInterface = ApiClient.getClient();
        swipeRefreshLayoutHistory = findViewById(R.id.swipeRefreshLayoutHistory);
        shimmerFrameLayoutHistory = findViewById(R.id.shimmerFrameLayoutHistory);
        frameLayoutEmptyHistory = findViewById(R.id.frameLayoutEmptyHistory);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);

        apiInterface.getSadariList(
                AppPreference.getUser(this).email
        ).enqueue(new Callback<SadariListResponse>() {
            @Override
            public void onResponse(Call<SadariListResponse> call, Response<SadariListResponse> response) {
                if (response.body().status) {
                    if (!response.body().data.isEmpty()) {
                        ArrayList<SadariListResponse.SadariListModel> list = new ArrayList<>();
                        for (SadariListResponse.SadariListModel model: response.body().data) {
                            if (model.isIndicated.equalsIgnoreCase("t")) {
                                list.add(model);
                            }
                        }
                        if (!list.isEmpty()) {
                            setRecyclerViewHistory(list);
                            showNotEmpty();
                        } else {
                            showEmpty();
                        }
                    } else {
                        showEmpty();
                    }
                } else {
                    showEmpty();
                }
            }

            @Override
            public void onFailure(Call<SadariListResponse> call, Throwable t) {
                Log.e("getListSadari", t.getMessage());
            }
        });

        swipeRefreshLayoutHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayoutHistory.startShimmer();
                shimmerFrameLayoutHistory.setVisibility(View.VISIBLE);
                recyclerViewHistory.setVisibility(View.GONE);
                frameLayoutEmptyHistory.setVisibility(View.GONE);

                apiInterface.getSadariList(
                        AppPreference.getUser(HistoryActivity.this).email
                ).enqueue(new Callback<SadariListResponse>() {
                    @Override
                    public void onResponse(Call<SadariListResponse> call, Response<SadariListResponse> response) {
                        if (response.body().status) {
                            if (!response.body().data.isEmpty()) {
                                ArrayList<SadariListResponse.SadariListModel> list = new ArrayList<>();
                                for (SadariListResponse.SadariListModel model: response.body().data) {
                                    if (model.isIndicated.equalsIgnoreCase("t")) {
                                        list.add(model);
                                    }
                                }
                                if (!list.isEmpty()) {
                                    setRecyclerViewHistory(list);
                                    showNotEmpty();
                                } else {
                                    showEmpty();
                                }
                            } else {
                                showEmpty();
                            }
                        } else {
                            showEmpty();
                        }
                    }

                    @Override
                    public void onFailure(Call<SadariListResponse> call, Throwable t) {
                        Log.e("getListSadari", t.getMessage());
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayoutHistory.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    public void setRecyclerViewHistory(ArrayList<SadariListResponse.SadariListModel> list) {
        historyAdapter = new HistoryAdapter(list);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(historyAdapter);
    }

    public void showEmpty() {
        shimmerFrameLayoutHistory.stopShimmer();
        shimmerFrameLayoutHistory.setVisibility(View.GONE);
        frameLayoutEmptyHistory.setVisibility(View.VISIBLE);
    }

    public void showNotEmpty() {
        shimmerFrameLayoutHistory.stopShimmer();
        shimmerFrameLayoutHistory.setVisibility(View.GONE);
        recyclerViewHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayoutHistory.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutHistory.stopShimmer();
        super.onPause();
    }
}