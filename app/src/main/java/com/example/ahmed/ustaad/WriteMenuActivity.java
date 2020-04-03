package com.example.ahmed.ustaad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ahmed.ustaad.activities.SpeechRecognitionActivity;
import com.example.ahmed.ustaad.activities.WriteCommonWordsActivity;
import com.example.ahmed.ustaad.activities.WriteNumbersActivity;
import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class WriteMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_menu);

        // Set the audio
        initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(R.raw.writing_menu)));
    }

    public void writeWordsButtonSelected(View view) {
        Intent intent = new Intent(this, WriteCommonWordsActivity.class);
        this.startActivity(intent);
    }

    public void writeNumbersButtonSelected(View view) {
        Intent intent = new Intent(this, WriteNumbersActivity.class);
        this.startActivity(intent);
    }

    public void randomWordButtonSelected(View view) {
        Intent intent = new Intent(this, SpeechRecognitionActivity.class);
        this.startActivity(intent);
    }

    public void writeNameButtonSelected(View view) {
        Intent intent = new Intent(this, SpeechRecognitionActivity.class);
        this.startActivity(intent);
    }

    public void backButtonSelected(View view) {
        this.finish();
    }
}
