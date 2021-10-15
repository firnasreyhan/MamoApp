package com.strada.mamoapp.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.strada.mamoapp.R;
import com.strada.mamoapp.api.ApiClient;
import com.strada.mamoapp.api.ApiInterface;
import com.strada.mamoapp.api.reponse.BaseResponse;
import com.strada.mamoapp.api.reponse.QuestionResponse;
import com.strada.mamoapp.api.reponse.SadariResponse;
import com.strada.mamoapp.preference.AppPreference;
import com.strada.mamoapp.service.APIService;
import com.strada.mamoapp.service.Client;
import com.strada.mamoapp.service.Data;
import com.strada.mamoapp.service.MyResponse;
import com.strada.mamoapp.service.NotificationSender;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestResultActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ImageView imageViewPositive, imageViewNegative;
    private MaterialButton materialButtonBack;

    private ArrayList<QuestionResponse.QuestionModel> questionModelArrayList;

    private String idSadari;
    private int point = 0;
    private boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_test_result);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        apiInterface = ApiClient.getClient();
        questionModelArrayList = (ArrayList<QuestionResponse.QuestionModel>) getIntent().getSerializableExtra("RESULT");

        imageViewPositive = findViewById(R.id.imageViewPositive);
        imageViewNegative = findViewById(R.id.imageViewNegative);
        materialButtonBack = findViewById(R.id.materialButtonBack);

        for (int i = 0; i < questionModelArrayList.size(); i++) {
            if (questionModelArrayList.get(i).answer) {
                point++;
            }
        }

        result = point > (point/2);

        if (result) {
            imageViewPositive.setVisibility(View.VISIBLE);
            getUserToken("dokter@gmailcom");
        } else {
            imageViewNegative.setVisibility(View.VISIBLE);
        }

        postSadari(
                AppPreference.getUser(this).email,
                simpleDateFormat.format(date)
        );

        materialButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result) {
                    new AlertDialog.Builder(TestResultActivity.this)
                            .setTitle("Pesan")
                            .setMessage("Apakah anda ingin mengirim gambar hasil test?")
                            .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(v.getContext(), UploadGambarActivity.class);
                                    intent.putExtra("ID_SADARI", idSadari);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .create()
                            .show();
                } else {
                    finish();
                }
            }
        });
    }

    public void postSadari(String email, String date) {
        apiInterface.postSadari(
                email,
                date
        ).enqueue(new Callback<SadariResponse>() {
            @Override
            public void onResponse(Call<SadariResponse> call, Response<SadariResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        postDetailSadari(response.body().data.idSadari);
                        idSadari = response.body().data.idSadari;
                    }
                } else {
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<SadariResponse> call, Throwable t) {
                alertErrorServer();
                Log.e("postSadari", t.getMessage());
            }
        });
    }

    public void postDetailSadari(String idSadari) {
        for (int i = 0; i < questionModelArrayList.size(); i++) {
            apiInterface.postDetailSadari(
                    idSadari,
                    questionModelArrayList.get(i).idQuestion,
                    questionModelArrayList.get(i).answer ? 1 : 0
            ).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        Log.e("postDetailSadari", response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("postDetailSadari", t.getMessage());
                }
            });
        }

        putResultSadari(idSadari, result ? 1 : 0);
    }

    public void putResultSadari(String idSadari, int isIdicated) {
        apiInterface.putResultSadari(
                idSadari,
                isIdicated
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    Log.e("putResultSadari", response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("putResultSadari", t.getMessage());
            }
        });
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(TestResultActivity.this)
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

    public void getUserToken(String userKey) {
        FirebaseDatabase.getInstance().getReference().child("MamoApp").child("Token").child(userKey).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userToken = dataSnapshot.getValue(String.class);
                sendNotifications(userToken, "Terdapat data kuesioner yang harus diperiksa!", "Silahkan cek aplikasi Anda.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, userToken);
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(TestResultActivity.this, "Failed", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}