package com.example.ahmed.ustaad.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.example.ahmed.ustaad.BaseActivity;
import com.example.ahmed.ustaad.R;

public class CalculationActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_calculation);

        this.getSetViews();

    }

    public void getSetViews() {
        this.videoView = this.findViewById(R.id.video_view);

        //play video
        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.calculation_video_small);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                CalculationActivity.this.finish();
            }
        });

    }


    public void backButtonSelected(View view) {
        this.finish();
    }


}
