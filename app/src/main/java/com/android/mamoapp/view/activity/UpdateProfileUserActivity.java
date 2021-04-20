package com.android.mamoapp.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.AvatarResponse;
import com.android.mamoapp.api.reponse.BaseResponse;
import com.android.mamoapp.api.reponse.LoginResponse;
import com.android.mamoapp.preference.AppPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileUserActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private FloatingActionButton floatingActionButtonAvatar;
    private ShapeableImageView shapeableImageViewAvatar;
    private TextInputEditText textInputEditTextName, textInputEditTextPhone, textInputEditTextDateBirth;
    private MaterialButton materialButtonProfileSave;
    private Calendar calendar;
    private ProgressDialog progressDialog;

    private String sDate;
    private MultipartBody.Part img1;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_update_profile_user);

        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();
        apiInterface = ApiClient.getClient();
        floatingActionButtonAvatar = findViewById(R.id.floatingActionButtonAvatar);
        shapeableImageViewAvatar = findViewById(R.id.shapeableImageViewAvatar);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone);
        textInputEditTextDateBirth = findViewById(R.id.textInputEditTextDateBirth);
        materialButtonProfileSave = findViewById(R.id.materialButtonProfileSave);

        sDate = AppPreference.getUser(this).dateBirth;

        Log.e("sDate", sDate);
        Glide.with(this)
                .load(AppPreference.getUser(this).profilpicUser)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .placeholder(R.drawable.img_icon)
                .into(shapeableImageViewAvatar);
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

        floatingActionButtonAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UpdateProfileUserActivity.this);
            }
        });

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
                progressDialog.setMessage("Mohon tunggu sebentar...");
                progressDialog.setCancelable(false);
                progressDialog.show();

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
                    if (uri != null) {
                        postAvatar();
                    }
                    apiInterface.putProfile(
                            AppPreference.getUser(v.getContext()).email,
                            textInputEditTextName.getText().toString(),
                            sDate,
                            textInputEditTextPhone.getText().toString()
                    ).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (response.body() != null) {
                                if (response.body().status) {
                                    new AlertDialog.Builder(v.getContext())
                                            .setCancelable(false)
                                            .setTitle("Pesan")
                                            .setMessage(response.body().message)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    postLogin();
                                                    dialog.dismiss();
//                                                    finish();
//                                                    onBackPressed();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    new AlertDialog.Builder(v.getContext())
                                            .setCancelable(false)
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
                            } else {
                                alertErrorServer();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            alertErrorServer();
                            Log.e("putProfile", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void postAvatar() {
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), AppPreference.getUser(this).email);
        apiInterface.postAvatar(
                email,
                compressFile(uri)
        ).enqueue(new Callback<AvatarResponse>() {
            @Override
            public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                if (response.body() != null) {
                    Log.e("postAvatar", response.body().avatar.message);
                }
            }

            @Override
            public void onFailure(Call<AvatarResponse> call, Throwable t) {
                Log.e("postAvatar", t.getMessage());
            }
        });
    }

    public void postLogin() {
        progressDialog.setMessage("Mohon tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        apiInterface.postLogin(
                AppPreference.getUser(this).email,
                AppPreference.getUser(this).password
        ).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body().status) {
                    if (response.body().data != null) {
                        saveUser(response.body().data);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("postLogin", t.getMessage());
            }
        });
    }

    public void saveUser(LoginResponse.LoginModel loginModel) {
        LoginResponse.LoginModel model = new LoginResponse.LoginModel();
        model.email = loginModel.email;
        model.name = loginModel.name;
        model.profilpicUser = loginModel.profilpicUser;
        model.nameRole = loginModel.nameRole;
        model.phone = loginModel.phone;
        model.dateBirth = loginModel.dateBirth;
        model.password = AppPreference.getUser(this).password;
        AppPreference.removeUser(this);
        AppPreference.saveUser(this, model);
    }

    private void updateText() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        String textFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
        SimpleDateFormat nSdf = new SimpleDateFormat(textFormat, new Locale("id", "ID"));
        sDate = sdf.format(calendar.getTime());
        textInputEditTextDateBirth.setText(nSdf.format(calendar.getTime()));
    }

    private File createTempFile(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_avatar.JPEG");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        //write the bytes in file
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArray);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private MultipartBody.Part compressFile(Uri uri) {
        File file = new File(uri.getPath());
        try {
            File fileCompress = new Compressor(this)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(file);
            File file1 = createTempFile(Uri.fromFile(fileCompress.getAbsoluteFile()));
            Uri uri1 = Uri.fromFile(file1);
            Log.e("path", file1.getAbsolutePath());
            return MultipartBody.Part.createFormData("avatar", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void alertErrorServer() {
        new AlertDialog.Builder(UpdateProfileUserActivity.this)
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Uri resultUri = result.getUri();
                uri = result.getUri();
//                shapeableImageViewAvatar.setImageURI(uri);
                Glide.with(this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .dontTransform()
                        .priority(Priority.IMMEDIATE)
                        .encodeFormat(Bitmap.CompressFormat.PNG)
                        .format(DecodeFormat.DEFAULT)
                        .placeholder(R.drawable.img_icon)
                        .into(shapeableImageViewAvatar);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}