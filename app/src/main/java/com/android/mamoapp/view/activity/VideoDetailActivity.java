package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.VideoDetailResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDetailActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private YouTubePlayerView youTubePlayerViewDetailVideo;
    private TextView textViewTitleVideo, textViewDateVideo, textViewDescriptionVideo;
    private ShimmerFrameLayout shimmerFrameLayoutDetailVideo;
    private LinearLayout linearLayoutDetailVideo;

    private String idVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeWhiteMamoApp);
        setContentView(R.layout.activity_video_detail);

        idVideo = getIntent().getStringExtra("ID_VIDEO");

        apiInterface = ApiClient.getClient();
        youTubePlayerViewDetailVideo = findViewById(R.id.youtubePlayerViewDetailVideo);
        textViewTitleVideo = findViewById(R.id.textViewTitleVideo);
        textViewDateVideo = findViewById(R.id.textViewDateVideo);
        textViewDescriptionVideo = findViewById(R.id.textViewDescriptionVideo);
        shimmerFrameLayoutDetailVideo = findViewById(R.id.shimmerFrameLayoutDetailVideo);
        linearLayoutDetailVideo = findViewById(R.id.linearLayoutDetailVideo);
        getLifecycle().addObserver(youTubePlayerViewDetailVideo);

        apiInterface.getVideoDetail(
                idVideo
        ).enqueue(new Callback<VideoDetailResponse>() {
            @Override
            public void onResponse(Call<VideoDetailResponse> call, Response<VideoDetailResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        VideoDetailResponse.VideoModel model = response.body().data;
                        textViewTitleVideo.setText(model.title);

                        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy");
                        try {
                            Date date = oldFormat.parse(model.datePublisher);
                            textViewDateVideo.setText(newFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        textViewDescriptionVideo.setText(model.description);
                        setYouTubePlayerViewDetailVideo(model.idVideo);

                        shimmerFrameLayoutDetailVideo.stopShimmer();
                        shimmerFrameLayoutDetailVideo.setVisibility(View.GONE);
                        linearLayoutDetailVideo.setVisibility(View.VISIBLE);
                    }
                } else {
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<VideoDetailResponse> call, Throwable t) {
                Log.e("getVideoDetail", t.getMessage());
            }
        });
    }

    public void setYouTubePlayerViewDetailVideo(String idVideo) {
        youTubePlayerViewDetailVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(idVideo, 0);
            }
        });
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(VideoDetailActivity.this)
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
    public void onResume() {
        super.onResume();
        shimmerFrameLayoutDetailVideo.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayoutDetailVideo.stopShimmer();
        super.onPause();
    }
}