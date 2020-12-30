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
import com.android.mamoapp.adapter.SadariAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariListResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorMainActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewNewsSadari;
    private ShimmerFrameLayout shimmerFrameLayoutSadari;
    private FrameLayout frameLayoutEmptySadari;
    private SwipeRefreshLayout swipeRefreshLayoutSadari;
    private SadariAdapter sadariAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        apiInterface = ApiClient.getClient();
        recyclerViewNewsSadari = findViewById(R.id.recyclerViewSadari);
        shimmerFrameLayoutSadari = findViewById(R.id.shimmerFrameLayoutSadari);
        frameLayoutEmptySadari= findViewById(R.id.frameLayoutEmptySadari);
        swipeRefreshLayoutSadari= findViewById(R.id.swipeRefreshLayoutSadari);

        apiInterface.getSadariList().enqueue(new Callback<SadariListResponse>() {
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
                            setRecyclerViewNewsSadari(list);
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

        swipeRefreshLayoutSadari.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayoutSadari.startShimmer();
                shimmerFrameLayoutSadari.setVisibility(View.VISIBLE);
                recyclerViewNewsSadari.setVisibility(View.GONE);
                frameLayoutEmptySadari.setVisibility(View.GONE);

                apiInterface.getSadariList().enqueue(new Callback<SadariListResponse>() {
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
                                    setRecyclerViewNewsSadari(list);
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
                        swipeRefreshLayoutSadari.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    public void setRecyclerViewNewsSadari(ArrayList<SadariListResponse.SadariListModel> list) {
        sadariAdapter = new SadariAdapter(list);
        recyclerViewNewsSadari.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNewsSadari.setAdapter(sadariAdapter);
    }

    public void showEmpty() {
        shimmerFrameLayoutSadari.stopShimmer();
        shimmerFrameLayoutSadari.setVisibility(View.GONE);
        frameLayoutEmptySadari.setVisibility(View.VISIBLE);
    }

    public void showNotEmpty() {
        shimmerFrameLayoutSadari.stopShimmer();
        shimmerFrameLayoutSadari.setVisibility(View.GONE);
        recyclerViewNewsSadari.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayoutSadari.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutSadari.stopShimmer();
        super.onPause();
    }
}