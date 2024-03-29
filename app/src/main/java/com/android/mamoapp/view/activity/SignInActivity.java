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
import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.api.reponse.LoginResponse;
import com.android.mamoapp.preference.AppPreference;
import com.android.mamoapp.service.Token;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;

    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    private MaterialButton materialButtonLogin;
    private TextView textViewSignUp;

    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sign_in);

        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MamoApp");
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
                                    if (response.body() != null) {
                                        if (response.body().status) {
                                            if (response.body().data.nameRole.equalsIgnoreCase("user") || response.body().data.nameRole.equalsIgnoreCase("doctor")) {
                                                saveUser(response.body().data);
                                                String refreshToken = FirebaseInstanceId.getInstance().getToken();
                                                updateToken(refreshToken);
                                                if (response.body().data.nameRole.equalsIgnoreCase("user")) {
                                                    apiInterface.postToken(
                                                            token.getToken()
                                                    ).enqueue(new Callback<BaseResponse>() {
                                                        @Override
                                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                                            startActivity(new Intent(v.getContext(), UserMainActivity.class));
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                                                        }
                                                    });
                                                } else {
                                                    startActivity(new Intent(v.getContext(), DoctorMainActivity.class));
                                                    finish();
                                                }
                                            }
                                        } else {
                                            new AlertDialog.Builder(SignInActivity.this)
                                                    .setTitle("Pesan")
                                                    .setMessage(response.body().message)
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
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                               alertErrorServer();
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

    public void alertErrorServer() {
        new AlertDialog.Builder(SignInActivity.this)
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

    public void saveUser(LoginResponse.LoginModel loginModel) {
        LoginResponse.LoginModel model = new LoginResponse.LoginModel();
        model.email = loginModel.email;
        model.name = loginModel.name;
        model.profilpicUser = loginModel.profilpicUser;
        model.nameRole = loginModel.nameRole;
        model.phone = loginModel.phone;
        model.dateBirth = loginModel.dateBirth;
        model.password = textInputEditTextPassword.getText().toString();
        AppPreference.saveUser(this, model);
    }

    private void updateToken(String refreshToken) {
        String userKey = AppPreference.getUser(getApplicationContext()).email.replaceAll("[-+.^:,]","");
        Log.e("userKey", userKey);
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("MamoApp").child("Token").child(userKey).setValue(token);

        this.token = token;
    }
}