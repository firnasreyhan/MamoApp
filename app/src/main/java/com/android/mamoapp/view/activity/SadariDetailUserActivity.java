package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.SadariDetailAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariDetailResponse;
import com.android.mamoapp.api.reponse.SadariResultResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SadariDetailUserActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private RecyclerView recyclerViewSadariDetail;
    private ImageView imageViewResponse1, imageViewResponse2;
    private TextView textViewSadariIsIndicated, textViewSadariSurveyDate, textViewSadariIsChecked, textViewSadariDoctorName, textViewSadariCheckDate, textViewSadariResponseText;
    private LinearLayout linearLayoutIsIndicated;
    private SadariDetailAdapter sadariDetailAdapter;

    private String idSadari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sadari_detail_user);

        idSadari = getIntent().getStringExtra("ID_SADARI");

        apiInterface = ApiClient.getClient();
        recyclerViewSadariDetail = findViewById(R.id.recyclerViewSadariDetail);
        imageViewResponse1 = findViewById(R.id.imageViewResponse1);
        imageViewResponse2 = findViewById(R.id.imageViewResponse2);
        textViewSadariIsIndicated = findViewById(R.id.textViewSadariIsIndicated);
        textViewSadariSurveyDate = findViewById(R.id.textViewSadariSurveyDate);
        textViewSadariIsChecked = findViewById(R.id.textViewSadariIsChecked);
        textViewSadariDoctorName = findViewById(R.id.textViewSadariDoctorName);
        textViewSadariCheckDate = findViewById(R.id.textViewSadariCheckDate);
        textViewSadariResponseText = findViewById(R.id.textViewSadariResponseText);
        linearLayoutIsIndicated = findViewById(R.id.linearLayoutIsIndicated);

        apiInterface.getSadariDetail(
                idSadari
        ).enqueue(new Callback<SadariDetailResponse>() {
            @Override
            public void onResponse(Call<SadariDetailResponse> call, Response<SadariDetailResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.dataSadari.isEmpty()) {
                            if (response.body().data.dataSadari.get(0).isIndicated.equalsIgnoreCase("t")) {
                                textViewSadariIsIndicated.setText("Terindikasi Kangker");
                                linearLayoutIsIndicated.setVisibility(View.VISIBLE);
                                setSadariResult(idSadari, response.body().data.dataSadari.get(0).isChecked);
                            } else {
                                textViewSadariIsIndicated.setText("Tidak Terindikasi Kangker");
                            }

                            String nmyFormat = "dd MMMM yyyy"; //In which you need put here
                            SimpleDateFormat nsdf = new SimpleDateFormat(nmyFormat, new Locale("id", "ID"));
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                            try {
                                Date date = format.parse(response.body().data.dataSadari.get(0).dateSadari);
                                textViewSadariSurveyDate.setText(nsdf.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!response.body().data.dataSadariDetail.isEmpty()) {
                            setRecyclerViewSadariDetail(response.body().data.dataSadariDetail);
                        }
                    }
                } else {
                    alertErrorServer();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SadariDetailResponse> call, Throwable t) {
                alertErrorServer();
                finish();
                Log.e("getSadariDetail", t.getMessage());
            }
        });
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(SadariDetailUserActivity.this)
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

    public void setRecyclerViewSadariDetail(ArrayList<SadariDetailResponse.SadariDetail.DataSadariDetail> list) {
        sadariDetailAdapter = new SadariDetailAdapter(list);
        recyclerViewSadariDetail.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSadariDetail.setAdapter(sadariDetailAdapter);
    }

    public void setSadariResult(String idSadari, String isChecked) {
        if (isChecked.equalsIgnoreCase("t")) {
            textViewSadariIsChecked.setText("Sudah Diperiksa");
            textViewSadariIsChecked.setBackgroundResource(R.drawable.label_green);
        } else {
            textViewSadariIsChecked.setText("Belum Diperiksa");
            textViewSadariIsChecked.setBackgroundResource(R.drawable.label_red);
        }

        apiInterface.getSadariResult(
                idSadari
        ).enqueue(new Callback<SadariResultResponse>() {
            @Override
            public void onResponse(Call<SadariResultResponse> call, Response<SadariResultResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (response.body().data != null) {
                            textViewSadariDoctorName.setText(response.body().data.doctorName);
                            String nmyFormat = "dd MMMM yyyy"; //In which you need put here
                            SimpleDateFormat nsdf = new SimpleDateFormat(nmyFormat, new Locale("id", "ID"));
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                            try {
                                Date date = format.parse(response.body().data.dateSadariResult);
                                textViewSadariCheckDate.setText(nsdf.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Glide.with(SadariDetailUserActivity.this)
                                    .load(response.body().data.img1SadariResult)
                                    //.load(R.drawable.img_default_video)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .skipMemoryCache(true)
                                    .dontAnimate()
                                    .dontTransform()
                                    .priority(Priority.IMMEDIATE)
                                    .encodeFormat(Bitmap.CompressFormat.PNG)
                                    .format(DecodeFormat.DEFAULT)
                                    .placeholder(R.drawable.img_default_video)
                                    .into(imageViewResponse1);
                            Glide.with(SadariDetailUserActivity.this)
                                    .load(response.body().data.img2SadariResult)
                                    //.load(R.drawable.img_default_video)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .skipMemoryCache(true)
                                    .dontAnimate()
                                    .dontTransform()
                                    .priority(Priority.IMMEDIATE)
                                    .encodeFormat(Bitmap.CompressFormat.PNG)
                                    .format(DecodeFormat.DEFAULT)
                                    .placeholder(R.drawable.img_default_video)
                                    .into(imageViewResponse2);
                            textViewSadariResponseText.setText(response.body().data.contentSadariResult);
                        }
                    }
                } else {
                    alertErrorServer();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SadariResultResponse> call, Throwable t) {
                alertErrorServer();
                finish();
                Log.e("getSadariResult", t.getMessage());
            }
        });
    }
}