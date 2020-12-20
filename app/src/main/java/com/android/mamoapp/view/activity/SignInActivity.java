package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.LoginResponse;
import com.android.mamoapp.preference.AppPreference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;

    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    private MaterialButton materialButtonLogin;
    private TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sign_in);

        progressDialog = new ProgressDialog(this);
        apiInterface = ApiClient.getClient();
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        materialButtonLogin = findViewById(R.id.materialButtonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        materialButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cek1 = true;
                boolean cek2 = true;

                if (textInputEditTextEmail.getText().toString().isEmpty()){
                    textInputEditTextEmail.setError("Mohon isi data berikut!");
                    cek1 = false;
                }
                if (textInputEditTextPassword.getText().toString().isEmpty()){
                    textInputEditTextPassword.setError("Mohon isi data berikut!");
                    cek2 = false;
                }

                if (cek1 && cek2) {
                    progressDialog.setMessage("Mohon tunggu sebentar...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    int loadingTime = 3000;
                    new Handler().postDelayed(() -> {
                        apiInterface.postLogin(
                                textInputEditTextEmail.getText().toString(),
                                textInputEditTextPassword.getText().toString()
                        ).enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                    if (response.body().status) {
                                        AppPreference.saveUser(v.getContext(), response.body().data);
                                        startActivity(new Intent(v.getContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        new AlertDialog.Builder(v.getContext())
                                                .setTitle("Pesan")
                                                .setMessage(response.body().message)
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                Log.e("login", t.getMessage());
                            }
                        });
                    }, loadingTime);
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SignUpActivity.class));
            }
        });
    }
}