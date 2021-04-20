package com.android.mamoapp.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.SadariImageResponse;
import com.android.mamoapp.api.reponse.SadariResultDetailResponse;
import com.android.mamoapp.preference.AppPreference;
import com.android.mamoapp.service.APIService;
import com.android.mamoapp.service.Client;
import com.android.mamoapp.service.Data;
import com.android.mamoapp.service.MyResponse;
import com.android.mamoapp.service.NotificationSender;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class SadariResponseActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private EditText editTextSadariResponseText;
    private ImageView imageViewSadariImageOne, imageViewSadariImageTwo;
    private MaterialButton materialButtonResponse;

    private String idSadari, email;
    private int imageNumber;
    private MultipartBody.Part img1, img2;
    private Uri uri1, uri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_sadari_response);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());

        idSadari = getIntent().getStringExtra("ID_SADARI");
        email = getIntent().getStringExtra("EMAIL").replaceAll("[-+.^:,]","");

        apiInterface = ApiClient.getClient();
        editTextSadariResponseText = findViewById(R.id.editTextSadariResponseText);
        materialButtonResponse = findViewById(R.id.materialButtonResponse);
        imageViewSadariImageOne = findViewById(R.id.imageViewSadariImageOne);
        imageViewSadariImageTwo = findViewById(R.id.imageViewSadariImageTwo);

        imageViewSadariImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNumber = 1;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SadariResponseActivity.this);
            }
        });

        imageViewSadariImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNumber = 2;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SadariResponseActivity.this);
            }
        });

        materialButtonResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri1 != null && uri2 != null) {
                    apiInterface.postResultDetailSadari(
                            idSadari,
                            AppPreference.getUser(v.getContext()).email,
                            editTextSadariResponseText.getText().toString(),
                            simpleDateFormat.format(date)
                    ).enqueue(new Callback<SadariResultDetailResponse>() {
                        @Override
                        public void onResponse(Call<SadariResultDetailResponse> call, Response<SadariResultDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().status) {
                                    Log.e("idSadariResult", response.body().data.idSadariResult);
                                    if (uri1 != null && uri2 != null) {
                                        postImageSadari(response.body().data.idSadariResult);
                                    }
                                    getUserToken(email);
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
                            } else {
                                alertErrorServer();
                            }
                        }

                        @Override
                        public void onFailure(Call<SadariResultDetailResponse> call, Throwable t) {
                            alertErrorServer();
                            Log.e("postResultDetailSadari", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(SadariResponseActivity.this, "Harap Mingirim Gambar 1 dan Gambar 2", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void postImageSadari(String idSR) {
        RequestBody idSadariResult = RequestBody.create(MediaType.parse("text/plain"), idSR);
        apiInterface.postImageResultSadari(
                idSadariResult,
                compressFile(uri1, "img1"),
                compressFile(uri2, "img2")
        ).enqueue(new Callback<SadariImageResponse>() {
            @Override
            public void onResponse(Call<SadariImageResponse> call, Response<SadariImageResponse> response) {
                if (response.body() != null) {
                    Log.e("img1", response.body().img1.message);
                    Log.e("img2", response.body().img2.message);
                }
            }

            @Override
            public void onFailure(Call<SadariImageResponse> call, Throwable t) {
                Log.e("postImageSadari", t.getMessage());
            }
        });
    }

    private File createTempFile(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_"+ idSadari +".JPEG");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
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

    private MultipartBody.Part compressFile(Uri uri, String path) {
        File file = new File(uri.getPath());
        try {
            File fileCompress = new Compressor(this)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(file);
            File file1 = createTempFile(Uri.fromFile(fileCompress.getAbsoluteFile()));
            Log.e("path", file1.getAbsolutePath());
            return MultipartBody.Part.createFormData(path, file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                        Toast.makeText(SadariResponseActivity.this, "Failed", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    public void getUserToken(String userKey) {
        FirebaseDatabase.getInstance().getReference().child("MamoApp").child("Token").child(userKey).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userToken = dataSnapshot.getValue(String.class);
                sendNotifications(userToken, "Hasil Sadari Telah Direspon Oleh Dokter!", "Silahkan cek aplikasi Anda.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(SadariResponseActivity.this)
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
//                Uri resultUri = result.getUri();
                switch (imageNumber) {
                    case 1:
                        uri1 = result.getUri();
                        imageViewSadariImageOne.setImageURI(uri1);
//                        File file1 = new File(resultUri.getPath());
//                        try {
//                            File fileCompress1 = new Compressor(this)
//                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                                    .compressToFile(file1);
//                            File file11 = createTempFile(Uri.fromFile(fileCompress1.getAbsoluteFile()));
//                            Uri uri1 = Uri.fromFile(file11);
//                            Log.e("path1", file11.getAbsolutePath());
//                            imageViewSadariImageOne.setImageURI(uri1);
//                            img1 = MultipartBody.Part.createFormData("img1", file11.getName(), RequestBody.create(MediaType.parse("image/*"), file11));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        break;
                    case 2:
                        uri2 = result.getUri();
                        imageViewSadariImageTwo.setImageURI(uri2);
//                        File file2 = new File(resultUri.getPath());
//                        try {
//                            File fileCompress2 = new Compressor(this)
//                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                                    .compressToFile(file2);
//                            File file22 = createTempFile(Uri.fromFile(fileCompress2.getAbsoluteFile()));
//                            Uri uri2 = Uri.fromFile(file22);
//                            Log.e("path2", file22.getAbsolutePath());
//                            imageViewSadariImageTwo.setImageURI(uri2);
//                            img2 = MultipartBody.Part.createFormData("img2", file22.getName(), RequestBody.create(MediaType.parse("image/*"), file22));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        break;
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}