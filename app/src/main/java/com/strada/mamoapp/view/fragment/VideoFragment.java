package com.strada.mamoapp.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

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

import com.strada.mamoapp.R;
import com.strada.mamoapp.adapter.VideoAdapter;
import com.strada.mamoapp.api.ApiClient;
import com.strada.mamoapp.api.ApiInterface;
import com.strada.mamoapp.api.reponse.VideoResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment {
    private ApiInterface apiInterface;
    private VideoAdapter videoAdapter;
    private RecyclerView recyclerViewVideo;
    private SwipeRefreshLayout swipeRefreshLayoutVideo;
    private ShimmerFrameLayout shimmerFrameLayoutVideo;
    private FrameLayout frameLayoutEmptyVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        apiInterface = ApiClient.getClient();
        recyclerViewVideo = view.findViewById(R.id.recyclerViewVideo);
        swipeRefreshLayoutVideo = view.findViewById(R.id.swipeRefreshLayoutVideo);
        shimmerFrameLayoutVideo = view.findViewById(R.id.shimmerFrameLayoutVideo);
        frameLayoutEmptyVideo = view.findViewById(R.id.frameLayoutEmptyVideo);

        apiInterface.getVideo().enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            setRecyclerViewVideo(response.body().data);
                            showNotEmpty();
                        } else {
                            showEmpty();
                        }
                    } else {
                        showEmpty();
                    }
                } else {
                    showNotEmpty();
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                showNotEmpty();
                alertErrorServer();
                Log.e("video", t.getMessage());
            }
        });

        swipeRefreshLayoutVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayoutVideo.startShimmer();
                shimmerFrameLayoutVideo.setVisibility(View.VISIBLE);
                recyclerViewVideo.setVisibility(View.GONE);
                frameLayoutEmptyVideo.setVisibility(View.GONE);

                apiInterface.getVideo().enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status) {
                                if (!response.body().data.isEmpty()) {
                                    setRecyclerViewVideo(response.body().data);
                                    showNotEmpty();
                                } else {
                                    showEmpty();
                                }
                            } else {
                                showEmpty();
                            }
                        } else {
                            showNotEmpty();
                            alertErrorServer();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        showNotEmpty();
                        alertErrorServer();
                        Log.e("video", t.getMessage());
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayoutVideo.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return view;
    }

    public void setRecyclerViewVideo(ArrayList<VideoResponse.VideoModel> list) {
        videoAdapter = new VideoAdapter(list);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewVideo.setAdapter(videoAdapter);
    }

    public void showEmpty() {
        shimmerFrameLayoutVideo.stopShimmer();
        shimmerFrameLayoutVideo.setVisibility(View.GONE);
        frameLayoutEmptyVideo.setVisibility(View.VISIBLE);
    }

    public void showNotEmpty() {
        shimmerFrameLayoutVideo.stopShimmer();
        shimmerFrameLayoutVideo.setVisibility(View.GONE);
        recyclerViewVideo.setVisibility(View.VISIBLE);
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
        shimmerFrameLayoutVideo.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutVideo.stopShimmer();
        super.onPause();
    }
}