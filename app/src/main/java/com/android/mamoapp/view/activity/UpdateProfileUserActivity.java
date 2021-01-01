package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.api.reponse.LoginResponse;
import com.android.mamoapp.preference.AppPreference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileUserActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private TextInputEditText textInputEditTextName, textInputEditTextPhone, textInputEditTextDateBirth;
    private MaterialButton materialButtonProfileSave;
    private Calendar calendar;

    private String sDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_update_profile_user);

        calendar = Calendar.getInstance();
        apiInterface = ApiClient.getClient();
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone);
        textInputEditTextDateBirth = findViewById(R.id.textInputEditTextDateBirth);
        materialButtonProfileSave = findViewById(R.id.materialButtonProfileSave);

        sDate = AppPreference.getUser(this).dateBirth;

        Log.e("sDate", sDate);
        textInputEditTextName.setText(AppPreference.getUser(this).name);
        textInputEditTextPhone.setText(AppPreference.getUser(this).phone);
        String nmyFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat nsdf = new SimpleDateFormat(nmyFormat, new Locale("id", "ID"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(sDate);
            textInputEditTextDateBirth.setText(nsdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
                new DatePickerDialog(v.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        materialButtonProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cek1 = true;
                boolean cek2 = true;
                boolean cek3 = true;

                if (textInputEditTextName.getText().toString().isEmpty()){
                    textInputEditTextName.setError("Mohon isi data berikut!");
                    cek1 = false;
                }
                if (textInputEditTextPhone.getText().toString().isEmpty()){
                    textInputEditTextPhone.setError("Mohon isi data berikut!");
                    cek2 = false;
                }
                if (textInputEditTextDateBirth.getText().toString().isEmpty()){
                    textInputEditTextDateBirth.setError("Mohon isi data berikut!");
                    cek3 = false;
                }

                if (cek1 && cek2 && cek3) {
                    apiInterface.putProfile(
                            AppPreference.getUser(v.getContext()).email,
                            textInputEditTextName.getText().toString(),
                            sDate,
                            textInputEditTextPhone.getText().toString()
                    ).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
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

                            LoginResponse.LoginModel model = AppPreference.getUser(v.getContext());
                            model.name = textInputEditTextName.getText().toString();
                            model.phone = textInputEditTextPhone.getText().toString();
                            model.dateBirth = sDate;
                            AppPreference.removeUser(v.getContext());
                            AppPreference.saveUser(v.getContext(), model);
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.e("putProfile", t.getMessage());
                        }
                    });
                }
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