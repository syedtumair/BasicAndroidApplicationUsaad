package com.example.ahmed.ustaad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.ahmed.ustaad.BaseActivity;
import com.example.ahmed.ustaad.R;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.example.ahmed.ustaad.ReadMenuActivity;
import com.example.ahmed.ustaad.WriteMenuActivity;
import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, SpeechRecognitionActivity.class);

        // Set the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // Set the audio
        initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(R.raw.main_menu_converted)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void readingButtonSelected(View view) {
        Intent intent = new Intent(this, ReadMenuActivity.class);
        this.startActivity(intent);
    }

    public void writingButtonSelected(View view) {
        Intent intent = new Intent(this, WriteMenuActivity.class);
        this.startActivity(intent);
    }

    public void calcButtonSelected(View view) {
        Intent intent = new Intent(this, CalculationActivity.class);
        this.startActivity(intent);
    }
}