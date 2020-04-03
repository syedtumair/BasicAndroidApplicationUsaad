package com.example.ahmed.ustaad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ahmed.ustaad.activities.ReadCustomWordActivity;
import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_menu);

        // Set the audio
        initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(R.raw.reading_menu2)));
    }

    public void readWordsButtonSelected(View view) {
        Intent intent = new Intent(this, ReadCommonWordsActivity.class);
        intent.putExtra("index", 0);
        this.startActivity(intent);
    }

    public void readNumbersButtonSelected(View view) {
        Intent intent = new Intent(this, ReadNumbersActivity.class);
        intent.putExtra("index", 0);
        this.startActivity(intent);
    }

    public void randomWordButtonSelected(View view) {
        Intent intent = new Intent(this, ReadCustomWordActivity.class);
        this.startActivity(intent);
    }

    public void backButtonSelected(View view) {
        this.finish();
    }

}
