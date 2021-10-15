package com.strada.mamoapp.view.activity;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.strada.mamoapp.R;
import com.strada.mamoapp.api.ApiClient;
import com.strada.mamoapp.api.ApiInterface;
import com.strada.mamoapp.api.reponse.SadariImageResponse;
import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadGambarActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ImageView imageViewSadariImageOne, imageViewSadariImageTwo;
    private MaterialButton materialButtonResponse;

    private String idSadari;
    private int imageNumber;
    private Uri uri1, uri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_upload_gambar);

        idSadari = getIntent().getStringExtra("ID_SADARI");
        Log.e("ID_SADARI", idSadari);

        apiInterface = ApiClient.getClient();
        materialButtonResponse = findViewById(R.id.materialButtonResponse);
        imageViewSadariImageOne = findViewById(R.id.imageViewSadariImageOne);
        imageViewSadariImageTwo = findViewById(R.id.imageViewSadariImageTwo);

        imageViewSadariImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNumber = 1;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UploadGambarActivity.this);
            }
        });

        imageViewSadariImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNumber = 2;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UploadGambarActivity.this);
            }
        });

        materialButtonResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri1 != null && uri2 != null) {
                    postImageSadari(idSadari);
                } else {
                    Toast.makeText(UploadGambarActivity.this, "Harap Mingirim Gambar 1 dan Gambar 2", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void postImageSadari(String idSR) {
        RequestBody idSadariResult = RequestBody.create(MediaType.parse("text/plain"), idSR);
        apiInterface.postImageSadari(
                idSadariResult,
                compressFile(uri1, "img1"),
                compressFile(uri2, "img2")
        ).enqueue(new Callback<SadariImageResponse>() {
            @Override
            public void onResponse(Call<SadariImageResponse> call, Response<SadariImageResponse> response) {
                if (response.body() != null) {
                    Log.e("img1", response.body().img1.message);
                    Log.e("img2", response.body().img2.message);

                    Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<SadariImageResponse> call, Throwable t) {
                Log.e("postImageSadari", t.getMessage());
                alertErrorServer();
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

    public void alertErrorServer() {
        new AlertDialog.Builder(UploadGambarActivity.this)
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
                switch (imageNumber) {
                    case 1:
                        uri1 = result.getUri();
                        imageViewSadariImageOne.setImageURI(uri1);
                        break;
                    case 2:
                        uri2 = result.getUri();
                        imageViewSadariImageTwo.setImageURI(uri2);
                        break;
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}