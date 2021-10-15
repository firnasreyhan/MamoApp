package com.strada.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.strada.mamoapp.R;
import com.strada.mamoapp.adapter.SadariAdapter;
import com.strada.mamoapp.api.ApiClient;
import com.strada.mamoapp.api.ApiInterface;
import com.strada.mamoapp.api.reponse.SadariListResponse;
import com.strada.mamoapp.preference.AppPreference;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

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
    private MaterialButton materialButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        apiInterface = ApiClient.getClient();
        recyclerViewNewsSadari = findViewById(R.id.recyclerViewSadari);
        shimmerFrameLayoutSadari = findViewById(R.id.shimmerFrameLayoutSadari);
        frameLayoutEmptySadari= findViewById(R.id.frameLayoutEmptySadari);
        swipeRefreshLayoutSadari= findViewById(R.id.swipeRefreshLayoutSadari);
        materialButtonLogout = findViewById(R.id.materialButtonLogout);

        getData();

        materialButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userKey = AppPreference.getUser(v.getContext()).email.replaceAll("[-+.^:,]","");
                FirebaseDatabase.getInstance().getReference("MamoApp").child("Token").child(userKey).removeValue();
                AppPreference.removeUser(v.getContext());
                Intent intent = new Intent(v.getContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        swipeRefreshLayoutSadari.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayoutSadari.startShimmer();
                shimmerFrameLayoutSadari.setVisibility(View.VISIBLE);
                recyclerViewNewsSadari.setVisibility(View.GONE);
                frameLayoutEmptySadari.setVisibility(View.GONE);

                apiInterface.getSadariList(
                        AppPreference.getUser(DoctorMainActivity.this).email
                ).enqueue(new Callback<SadariListResponse>() {
                    @Override
                    public void onResponse(Call<SadariListResponse> call, Response<SadariListResponse> response) {
                        if (response.body() != null) {
                            if (response.body().status) {
                                if (!response.body().data.isEmpty()) {
                                    setRecyclerViewNewsSadari(response.body().data);
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
                    public void onFailure(Call<SadariListResponse> call, Throwable t) {
                        showEmpty();
                        alertErrorServer();
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

    public void getData() {
        apiInterface.getSadariList(
                AppPreference.getUser(this).email
        ).enqueue(new Callback<SadariListResponse>() {
            @Override
            public void onResponse(Call<SadariListResponse> call, Response<SadariListResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            setRecyclerViewNewsSadari(response.body().data);
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
            public void onFailure(Call<SadariListResponse> call, Throwable t) {
                showEmpty();
                alertErrorServer();
                Log.e("getListSadari", t.getMessage());
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

    public void alertErrorServer() {
        new AlertDialog.Builder(DoctorMainActivity.this)
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
        shimmerFrameLayoutSadari.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutSadari.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shimmerFrameLayoutSadari.startShimmer();
        shimmerFrameLayoutSadari.setVisibility(View.VISIBLE);
        recyclerViewNewsSadari.setVisibility(View.GONE);
        frameLayoutEmptySadari.setVisibility(View.GONE);
        getData();
    }
}