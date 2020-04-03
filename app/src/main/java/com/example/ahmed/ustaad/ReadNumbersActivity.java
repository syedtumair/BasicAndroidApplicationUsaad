package com.example.ahmed.ustaad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadNumbersActivity extends BaseActivity {

    private Integer index;
    private List<Integer> audioIdArr;
    private List<Integer> imageSrcArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_numbers);

        // Set the data
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        audioIdArr = Arrays.asList(R.raw.zero, R.raw.one_converted, R.raw.two_converted, R.raw.write_three, R.raw.four, R.raw.five,
                                    R.raw.six, R.raw.seven, R.raw.eight, R.raw.nine);
        imageSrcArr = Arrays.asList(R.drawable.zero_balls, R.drawable.one_ball, R.drawable.two_balls, R.drawable.three_balls, R.drawable.four_balls, R.drawable.five_balls,
                                    R.drawable.six_balls, R.drawable.seven_balls, R.drawable.eight_balls, R.drawable.nine_balls);

        // Set the data
        initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(audioIdArr.get(index),
                R.raw.repeat_word_converted, R.raw.navigation_converted, R.raw.back_to_menu)));

        ImageView wordImageView = (ImageView)findViewById(R.id.number_image_view);
        wordImageView.setImageResource(imageSrcArr.get(index));

        TextView wordTextView = (TextView)findViewById(R.id.number_text_view);
        wordTextView.setText(index.toString());

    }

    public void prevButtonSelected(View view) {
        this.finish();
    }

    public void nextButtonSelected(View view) {

        if(index < imageSrcArr.size() - 1) {
            Intent intent = new Intent(this, ReadNumbersActivity.class);
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
