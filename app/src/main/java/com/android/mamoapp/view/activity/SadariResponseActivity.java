package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariResultDetailResponse;
import com.android.mamoapp.preference.AppPreference;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SadariResponseActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private EditText editTextSadariResponseText;
    private MaterialButton materialButtonResponse;

    private String idSadari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sadari_response);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

        idSadari = getIntent().getStringExtra("ID_SADARI");

        apiInterface = ApiClient.getClient();
        editTextSadariResponseText = findViewById(R.id.editTextSadariResponseText);
        materialButtonResponse = findViewById(R.id.materialButtonResponse);

        materialButtonResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface.postResultDetailSadari(
                        idSadari,
                        AppPreference.getUser(v.getContext()).email,
                        editTextSadariResponseText.getText().toString(),
                        simpleDateFormat.format(date)
                ).enqueue(new Callback<SadariResultDetailResponse>() {
                    @Override
                    public void onResponse(Call<SadariResultDetailResponse> call, Response<SadariResultDetailResponse> response) {
                        if (response.body().status) {
                            Log.e("idSadariResult", response.body().data.idSadariResult);
                            new AlertDialog.Builder(SadariResponseActivity.this)
                                    .setTitle("Pesan")
                                    .setMessage("Tanggapan berhasil dikirim!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SadariResultDetailResponse> call, Throwable t) {
                        Log.e("postResultDetailSadari", t.getMessage());
                    }
                });
            }
        });
    }
}