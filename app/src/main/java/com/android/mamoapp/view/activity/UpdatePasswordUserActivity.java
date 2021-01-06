package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.preference.AppPreference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordUserActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private TextInputEditText textInputEditTextPassword, textInputEditTextConfirmPassword;
    private MaterialButton materialButtonPasswordSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_update_password_user);

        apiInterface = ApiClient.getClient();
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        materialButtonPasswordSave = findViewById(R.id.materialButtonPasswordSave);

        materialButtonPasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cek1 = true;
                boolean cek2 = true;
                boolean cek3 = true;

                if (textInputEditTextPassword.getText().toString().isEmpty()){
                    textInputEditTextPassword.setError("Mohon isi data berikut!");
                    cek1 = false;
                }
                if (textInputEditTextConfirmPassword.getText().toString().isEmpty()){
                    textInputEditTextConfirmPassword.setError("Mohon isi data berikut!");
                    cek2 = false;
                }
                if (cek1 && cek2) {
                    if (!textInputEditTextPassword.getText().toString().equals(textInputEditTextConfirmPassword.getText().toString())) {
                        textInputEditTextConfirmPassword.setError("Kombinasi password tidak cocok!");
                        cek3 = false;
                    }
                }

                if (cek1 && cek2 && cek3) {
                    apiInterface.putPassword(
                            AppPreference.getUser(v.getContext()).email,
                            textInputEditTextPassword.getText().toString()
                    ).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body() != null) {
                                if (response.body().status) {
                                    new AlertDialog.Builder(v.getContext())
                                            .setCancelable(false)
                                            .setTitle("Pesan")
                                            .setMessage(response.body().message)
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
                            } else {
                                alertErrorServer();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            alertErrorServer();
                            Log.e("putPassword", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(UpdatePasswordUserActivity.this)
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
}