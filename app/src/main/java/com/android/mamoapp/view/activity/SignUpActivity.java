package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.BaseResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;

    private TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextPhone, textInputEditTextDateBirth, textInputEditTextPassword, textInputEditTextConfirmPassword;
    private MaterialButton materialButtonRegister;
    private TextView textViewSignIn;

    private Calendar calendar;
    private String sDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sign_up);

        calendar = Calendar.getInstance();

        progressDialog = new ProgressDialog(this);
        apiInterface = ApiClient.getClient();
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone);
        textInputEditTextDateBirth = findViewById(R.id.textInputEditTextDateBirth);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        materialButtonRegister = findViewById(R.id.materialButtonRegister);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateText();
            }
        };

        textInputEditTextDateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        materialButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cek1 = true;
                boolean cek2 = true;
                boolean cek3 = true;
                boolean cek4 = true;
                boolean cek5 = true;
                boolean cek6 = true;
                boolean cek7 = true;

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
                if (textInputEditTextDateBirth.getText().toString().isEmpty()){
                    textInputEditTextDateBirth.setError("Mohon isi data berikut!");
                    cek7 = false;
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

                if (cek1 && cek2 && cek3 && cek4 && cek5 && cek6 && cek7) {
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
                                sDate,
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

    private void updateText() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        String textFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
        SimpleDateFormat nSdf = new SimpleDateFormat(textFormat, new Locale("id", "ID"));
        sDate = sdf.format(calendar.getTime());
        textInputEditTextDateBirth.setText(nSdf.format(calendar.getTime()));
    }
}