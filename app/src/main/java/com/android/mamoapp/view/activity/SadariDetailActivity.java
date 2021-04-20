package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    private ImageView imageViewDokumenUser1, imageViewDokumenUser2;
    private LinearLayout linearLayoutDokumenUser;
    private TextView textViewNameSadari, textViewEmailSadari, textViewPhoneSadari, textViewDateBirthSadari, textViewIsIndicatedSadari, textViewDateSadari;
    private RecyclerView recyclerViewSadariDetail;
    private SadariDetailAdapter sadariDetailAdapter;
    private MaterialButton materialButtonResponse;

    private String idSadari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
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
        imageViewDokumenUser1 = findViewById(R.id.imageViewDokumenUser1);
        imageViewDokumenUser2 = findViewById(R.id.imageViewDokumenUser2);
        linearLayoutDokumenUser = findViewById(R.id.linearLayoutDokumenUser);

        getData();

        materialButtonResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SadariResponseActivity.class);
                intent.putExtra("ID_SADARI", idSadari);
                intent.putExtra("EMAIL", textViewEmailSadari.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        apiInterface.getSadariDetail(
                idSadari
        ).enqueue(new Callback<SadariDetailResponse>() {
            @Override
            public void onResponse(Call<SadariDetailResponse> call, Response<SadariDetailResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (response.body().data != null) {
                            if (!response.body().data.dataSadari.isEmpty()) {
                                SadariDetailResponse.SadariDetail.DataSadari dataSadari = response.body().data.dataSadari.get(0);
                                textViewNameSadari.setText(dataSadari.name);
                                textViewEmailSadari.setText(dataSadari.email);
                                textViewPhoneSadari.setText(dataSadari.phone);
                                if (dataSadari.isIndicated.equalsIgnoreCase("t")) {
                                    textViewIsIndicatedSadari.setText("Terindikasi Mengidap Kanker");
                                    textViewIsIndicatedSadari.setBackgroundResource(R.drawable.label_red);
                                } else {
                                    textViewIsIndicatedSadari.setText("Tidak Terindikasi Mengidap Kanker");
                                    textViewIsIndicatedSadari.setBackgroundResource(R.drawable.label_green);
                                }
                                if (dataSadari.isChecked.equalsIgnoreCase("t")) {
                                    materialButtonResponse.setVisibility(View.GONE);
                                } else {
                                    materialButtonResponse.setVisibility(View.VISIBLE);
                                }
                                String myFormat = "dd MMMM yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                try {
                                    Date date = format.parse(dataSadari.dateSadari);
                                    Date date1 = format1.parse(dataSadari.dateBirth);
                                    textViewDateSadari.setText(sdf.format(date));
                                    textViewDateBirthSadari.setText(sdf.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (response.body().data.dataSadari.get(0).imgSadari1 != null || response.body().data.dataSadari.get(0).imgSadari2 != null) {
                                    linearLayoutDokumenUser.setVisibility(View.VISIBLE);

                                    if (response.body().data.dataSadari.get(0).imgSadari1 != null) {
                                        Glide.with(SadariDetailActivity.this)
                                                .load(response.body().data.dataSadari.get(0).imgSadari1)
                                                //.load(R.drawable.img_default_video)
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .skipMemoryCache(true)
                                                .dontAnimate()
                                                .dontTransform()
                                                .priority(Priority.IMMEDIATE)
                                                .encodeFormat(Bitmap.CompressFormat.PNG)
                                                .format(DecodeFormat.DEFAULT)
                                                .placeholder(R.drawable.img_default_video)
                                                .into(imageViewDokumenUser1);
                                    }

                                    if (response.body().data.dataSadari.get(0).imgSadari2 != null) {
                                        Glide.with(SadariDetailActivity.this)
                                                .load(response.body().data.dataSadari.get(0).imgSadari2)
                                                //.load(R.drawable.img_default_video)
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .skipMemoryCache(true)
                                                .dontAnimate()
                                                .dontTransform()
                                                .priority(Priority.IMMEDIATE)
                                                .encodeFormat(Bitmap.CompressFormat.PNG)
                                                .format(DecodeFormat.DEFAULT)
                                                .placeholder(R.drawable.img_default_video)
                                                .into(imageViewDokumenUser2);
                                    }
                                }
                            }
                            if (!response.body().data.dataSadariDetail.isEmpty()) {
                                Log.e("size", String.valueOf(response.body().data.dataSadariDetail.size()));
                                setRecyclerViewSadariDetail(response.body().data.dataSadariDetail);
                            }
                        }
                    } else {
                        alertErrorServer();
                        finish();
                    }
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

    public void setRecyclerViewSadariDetail(ArrayList<SadariDetailResponse.SadariDetail.DataSadariDetail> list) {
        sadariDetailAdapter = new SadariDetailAdapter(list);
        recyclerViewSadariDetail.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSadariDetail.setAdapter(sadariDetailAdapter);
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(SadariDetailActivity.this)
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
    protected void onRestart() {
        super.onRestart();
        getData();
    }
}