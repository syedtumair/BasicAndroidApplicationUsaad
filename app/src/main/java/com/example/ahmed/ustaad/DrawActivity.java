package com.example.ahmed.ustaad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.views.DrawView;

public class DrawActivity extends BaseActivity {

    DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_draw);
        this.getViews();
        this.setDrawView();

    }

    public void getViews() {
        this.drawView = (DrawView) this.findViewById(R.id.draw_view);
    }

    public void setDrawView() {
        //this.drawView.setDrawingMode(DrawingMode.TEXT);
        //this.drawView.refreshLastText("Ahmed");
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
