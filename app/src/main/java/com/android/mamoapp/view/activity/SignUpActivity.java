package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.BaseResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;

    private TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextPhone, textInputEditTextPassword, textInputEditTextConfirmPassword;
    private MaterialButton materialButtonRegister;
    private TextView textViewSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        apiInterface = ApiClient.getClient();
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        materialButtonRegister = findViewById(R.id.materialButtonRegister);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        materialButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cek1 = true;
                boolean cek2 = true;
                boolean cek3 = true;
                boolean cek4 = true;
                boolean cek5 = true;
                boolean cek6 = true;

                if (textInputEditTextName.getText().toString().isEmpty()){
                    textInputEditTextName.setError("Mohon isi data berikut!");
                    cek1 = false;
                }
                if (textInputEditTextEmail.getText().toString().isEmpty()){
                    textInputEditTextEmail.setError("Mohon isi data berikut!");
                    cek2 = false;
                }
                if (textInputEditTextPhone.getText().toString().isEmpty()){
                    textInputEditTextPhone.setError("Mohon isi data berikut!");
                    cek3 = false;
                }
                if (textInputEditTextPassword.getText().toString().isEmpty()){
                    textInputEditTextPassword.setError("Mohon isi data berikut!");
                    cek4 = false;
                }
                if (textInputEditTextConfirmPassword.getText().toString().isEmpty()){
                    textInputEditTextConfirmPassword.setError("Mohon isi data berikut!");
                    cek5 = false;
                }
                if (cek4 && cek5) {
                    if (!textInputEditTextPassword.getText().toString().equals(textInputEditTextConfirmPassword.getText().toString())) {
                        textInputEditTextConfirmPassword.setError("Kombinasi password tidak cocok!");
                        cek6 = false;
                    }
                }

                if (cek1 && cek2 && cek3 && cek4 && cek5 && cek6) {
                    progressDialog.setMessage("Mohon tunggu sebentar...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    int loadingTime = 3000;
                    new Handler().postDelayed(() -> {
                        apiInterface.postRegister(
                                textInputEditTextEmail.getText().toString(),
                                textInputEditTextPassword.getText().toString(),
                                "1",
                                textInputEditTextName.getText().toString(),
                                textInputEditTextPhone.getText().toString()
                        ).enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                    new AlertDialog.Builder(v.getContext())
                                            .setTitle("Pesan")
                                            .setMessage(response.body().message)
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (response.body().status) {
                                                        finish();
                                                    } else {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            })
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Log.e("register", t.getMessage());
                            }
                        });
                    }, loadingTime);
                }
            }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}