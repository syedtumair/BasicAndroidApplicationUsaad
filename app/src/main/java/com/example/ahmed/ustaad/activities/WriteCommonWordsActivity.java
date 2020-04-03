package com.example.ahmed.ustaad.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.ahmed.ustaad.BaseActivity;
import com.example.ahmed.ustaad.R;
import com.example.ahmed.ustaad.WriteMenuActivity;
import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;
import com.example.ahmed.ustaad.fragments.DrawFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteCommonWordsActivity extends BaseActivity implements DrawFragment.DrawFragmentCallback, MediaPlayer.OnCompletionListener {

    public static final String INTENT_KEY_INDEX = "index";

    private Integer index;
    private List<Integer> audioIdArr;
    private List<String> textArr;
    private DrawFragment drawFragment;
    private SequenceMediaPlayer sequenceMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting content view
        this.setContentView(R.layout.activity_write_common_words);

        //getting intent
        Intent intent = this.getIntent();
        this.index = intent.getIntExtra(INTENT_KEY_INDEX, 0);
        this.audioIdArr = Arrays.asList(R.raw.apple2_converted, R.raw.orange2_converted, R.raw.chair2_converted, R.raw.clock2_converted);
        this.textArr = Arrays.asList("سیب","مالٹا", "کرسی", "گھڑی");

        //setting media player
        this.initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(this.audioIdArr.get(this.index), R.raw.write_dotted_words, R.raw.navigation_converted, R.raw.back_to_menu)));
        this.sequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>());
        //setting sequenceMediaPlayer onCompletionListener
        this.sequenceMediaPlayer.setOnCompletionListener(this);

        //getting and setting views
        this.getSetViews();
    }

    /**
     * Get sets views
     */
    public void getSetViews() {
        //initializing fragment
        this.drawFragment = new DrawFragment();
        //setting drawFragment arguments
        Bundle bundle = new Bundle();
        bundle.putString(DrawFragment.DRAW_FRAGMENT_WORD_KEY, this.textArr.get(this.index));
        this.drawFragment.setArguments(bundle);
        //getting fragment manager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //beginning fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //adding drawFragment to fragment transaction
        fragmentTransaction.add(R.id.fragment_container, this.drawFragment);
        //committing fragment transaction
        fragmentTransaction.commit();

    }

    @Override
    public void onSimilarityCheckComplete(float similarity) {
        if(similarity >= 0.4) { //go to next word
            if(this.index < this.textArr.size()) {
                Intent intent = new Intent(this, WriteCommonWordsActivity.class);
                //putting index + 1 in intent extra
                intent.putExtra(INTENT_KEY_INDEX, (index + 1));
                //launching activity
                this.startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, WriteMenuActivity.class);
                //starting activity
                this.startActivity(intent);
            }
        }
        else {
            this.drawFragment.clearView();
            this.initialSequenceMediaPlayer.clear();
            this.sequenceMediaPlayer.setAudioFilesArray(new ArrayList<Integer>(Arrays.asList(R.raw.wrong_only, R.raw.write_dotted_words, R.raw.navigation_converted, R.raw.back_to_menu)));
            this.sequenceMediaPlayer.clear();
            this.sequenceMediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }



    public void prevButtonSelected(View view) {
        this.finish();
    }

    public void nextButtonSelected(View view) {
        this.drawFragment.checkSimilarity();
    }

    public void backButtonSelected(View view) {
        Intent intent = new Intent(this, WriteMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //starting activity
        this.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sequenceMediaPlayer.clear();
    }

}
