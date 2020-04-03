package com.example.ahmed.ustaad;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    protected SequenceMediaPlayer initialSequenceMediaPlayer;

    @Override
    public void onResume() {
        super.onResume();

        if (initialSequenceMediaPlayer != null) {
            initialSequenceMediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(initialSequenceMediaPlayer != null) {
            if(initialSequenceMediaPlayer != null) {
                initialSequenceMediaPlayer.clear();
            }
        }
    }

}
