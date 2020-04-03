package com.example.ahmed.ustaad.classes;

import android.media.MediaPlayer;
import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SequenceMediaPlayer extends Object {



    private ArrayList<Integer> audioFileIds;
    private ArrayList<MediaPlayer> mediaPlayers;
    private WeakReference<Context> weakContext;
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public SequenceMediaPlayer(Context context, ArrayList<Integer> audioFileIdList) {
        weakContext = new WeakReference<>(context);
        mediaPlayers = new ArrayList<MediaPlayer>();
        audioFileIds = new ArrayList<Integer>();
        audioFileIds.addAll(audioFileIdList);
    }

    public void setAudioFiles(Context context) {

        // Initialize all media players
        for(Integer audioFileName : audioFileIds) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, audioFileName);
            mediaPlayers.add(mediaPlayer);
        }
        //setting last media player callback
        if(this.mediaPlayers.size() > 0) {
            this.mediaPlayers.get(this.mediaPlayers.size() - 1).setOnCompletionListener(this.onCompletionListener);
        }

        // Play media one after the other
        for(int i = 0; i < mediaPlayers.size() - 1; i++) {
            mediaPlayers.get(i).setNextMediaPlayer(mediaPlayers.get(i+1));
        }

    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void setAudioFilesArray(ArrayList<Integer> audioFileIds) {
        if(audioFileIds != null) {
            this.audioFileIds = audioFileIds;
        }
        else {
            this.audioFileIds = new ArrayList<Integer>();
            Log.d("SequenceMediaPlayer", "passing null audioFileIds not possible setting it to new ArrayList");
        }
    }

    public void start() {
        if(mediaPlayers.size() == 0 && weakContext.get() !=null) {
            setAudioFiles(weakContext.get());
        }
        else {
            return;
        }
        if(mediaPlayers.size() > 0) {
            MediaPlayer mediaPlayer = mediaPlayers.get(0);
            mediaPlayers.get(0).start();
        }
    }

    public void clear() {
        // Stop and release
        for(MediaPlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Empty the media player list
        mediaPlayers.clear();
    }

//    @Override
//    public void onPrepared(MediaPlayer mp) {
//
//    }
}
