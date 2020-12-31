package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.SadariDetailAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariDetailResponse;
import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SadariDetailActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private TextView textViewNameSadari, textViewEmailSadari, textViewPhoneSadari, textViewDateBirthSadari, textViewIsIndicatedSadari, textViewDateSadari;
    private RecyclerView recyclerViewSadariDetail;
    private SadariDetailAdapter sadariDetailAdapter;
    private MaterialButton materialButtonResponse;

    private String idSadari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sadari_detail);

        idSadari = getIntent().getStringExtra("ID_SADARI");

        apiInterface = ApiClient.getClient();
        textViewNameSadari = findViewById(R.id.textViewNameSadari);
        textViewEmailSadari = findViewById(R.id.textViewEmailSadari);
        textViewPhoneSadari = findViewById(R.id.textViewPhoneSadari);
        textViewDateBirthSadari = findViewById(R.id.textViewDateBirthSadari);
        textViewIsIndicatedSadari = findViewById(R.id.textViewIsIndicatedSadari);
        textViewDateSadari = findViewById(R.id.textViewDateSadari);
        recyclerViewSadariDetail = findViewById(R.id.recyclerViewSadariDetail);
        materialButtonResponse = findViewById(R.id.materialButtonResponse);

        apiInterface.getSadariDetail(
                idSadari
        ).enqueue(new Callback<SadariDetailResponse>() {
            @Override
            public void onResponse(Call<SadariDetailResponse> call, Response<SadariDetailResponse> response) {
                if (response.body().status) {
                    if (response.body().data != null) {
                        if (!response.body().data.dataSadari.isEmpty()) {
                            SadariDetailResponse.SadariDetail.DataSadari dataSadari = response.body().data.dataSadari.get(0);
                            textViewNameSadari.setText(dataSadari.name);
                            textViewEmailSadari.setText(dataSadari.email);
                            textViewPhoneSadari.setText(dataSadari.phone);
                            textViewDateBirthSadari.setText(dataSadari.dateBirth);
                            if (dataSadari.isIndicated.equalsIgnoreCase("t")) {
                                textViewIsIndicatedSadari.setText("Terindikasi Mengidap Kanker");
                                textViewIsIndicatedSadari.setBackgroundResource(R.drawable.label_red);
                            } else {
                                textViewIsIndicatedSadari.setText("Tidak Terindikasi Mengidap Kanker");
                                textViewIsIndicatedSadari.setBackgroundResource(R.drawable.label_green);
                            }
                            String myFormat = "dd MMMM yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                            try {
                                Date date = format.parse(dataSadari.dateSadari);
                                textViewDateSadari.setText(sdf.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!response.body().data.dataSadariDetail.isEmpty()) {
                            Log.e("size", String.valueOf(response.body().data.dataSadariDetail.size()));
                            setRecyclerViewSadariDetail(response.body().data.dataSadariDetail);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SadariDetailResponse> call, Throwable t) {
                Log.e("getSadariDetail", t.getMessage());
            }
        });

        materialButtonResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SadariResponseActivity.class));
            }
        });
    }

    public void setRecyclerViewSadariDetail(ArrayList<SadariDetailResponse.SadariDetail.DataSadariDetail> list) {
        sadariDetailAdapter = new SadariDetailAdapter(list);
        recyclerViewSadariDetail.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSadariDetail.setAdapter(sadariDetailAdapter);
    }
}