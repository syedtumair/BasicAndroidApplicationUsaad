package com.example.ahmed.ustaad.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.views.DrawView;
import com.example.ahmed.ustaad.R;
import com.example.ahmed.ustaad.asynctasks.SimilarityComparisonAsyncTask;
import com.example.ahmed.ustaad.fragments.DrawFragment;
import com.example.ahmed.ustaad.utilities.Utility;

public class DrawActivity extends AppCompatActivity implements DrawFragment.DrawFragmentCallback {

    private FrameLayout frameLayoutFragmentContainer;
    private Button button;
    private DrawFragment drawFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_draw);
        this.getSetViews();
        this.setViewsClickListeners();
    }

    public void getSetViews() {
        this.frameLayoutFragmentContainer = this.findViewById(R.id.fragment_container);
        this.button = (Button) this.findViewById(R.id.button);
        //initializing and setting drawFragment
        initializeSetFragment();
    }

    public void initializeSetFragment() {
        //initializing drawFragment
        this.drawFragment = new DrawFragment();
        //setting bundle for DrawFragment
        Bundle bundle = new Bundle();
        //putting name to be drawn
        bundle.putString(DrawFragment.DRAW_FRAGMENT_WORD_KEY, "احمد");
        //setting arguments for DrawFragment
        this.drawFragment.setArguments(bundle);
        //getting fragment manager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //beginning fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //adding drawFragment to fragment transaction
        fragmentTransaction.add(R.id.fragment_container, this.drawFragment);
        //comming fragment transaction
        fragmentTransaction.commit();
    }

    public void setViewsClickListeners() {
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawActivity.this.drawFragment.checkSimilarity();
            }
        });
    }

    @Override
    public void onSimilarityCheckComplete(float similarity) {
        Log.d("Similarity", String.valueOf(similarity));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
