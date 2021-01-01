package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.SadariDetailAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariDetailResponse;
import com.android.mamoapp.api.reponse.SadariResultResponse;

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
            }

            @Override
            public void onFailure(Call<SadariDetailResponse> call, Throwable t) {
                Log.e("getSadariDetail", t.getMessage());
            }
        });
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
                        textViewSadariResponseText.setText(response.body().data.contentSadariResult);
                    }
                }
            }

            @Override
            public void onFailure(Call<SadariResultResponse> call, Throwable t) {
                Log.e("getSadariResult", t.getMessage());
            }
        });
    }
}