package com.example.ahmed.ustaad;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadCommonWordsActivity extends BaseActivity {

    private Integer index;
    private List<Integer> audioIdArr;
    private List<String> textArr;
    private List<Integer> imageSrcArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_common_words);

        // Set the data
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        audioIdArr = Arrays.asList(R.raw.apple2_converted,R.raw.orange2_converted, R.raw.chair2_converted, R.raw.clock2_converted);
        textArr = Arrays.asList("سیب","مالٹا", "کرسی", "گھڑی");
        imageSrcArr = Arrays.asList(R.drawable.apple, R.drawable.orange, R.drawable.chair, R.drawable.clock_image);

        // Set the data
        initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(audioIdArr.get(index),
                R.raw.repeat_word_converted, R.raw.navigation_converted, R.raw.back_to_menu)));

        ImageView wordImageView = (ImageView)findViewById(R.id.word_image);
        wordImageView.setImageResource(imageSrcArr.get(index));

        TextView wordTextView = (TextView)findViewById(R.id.word_label);
        wordTextView.setText(textArr.get(index));


    }

    public void prevButtonSelected(View view) {
        this.finish();
    }

    public void nextButtonSelected(View view) {

        if(index < textArr.size() - 1) {
            Intent intent = new Intent(this, ReadCommonWordsActivity.class);
            intent.putExtra("index", index+1);
            this.startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, ReadMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
        }
    }

    public void backButtonSelected(View view) {
        Intent intent = new Intent(this, ReadMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
