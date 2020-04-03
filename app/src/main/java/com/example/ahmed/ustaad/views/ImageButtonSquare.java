package com.example.ahmed.ustaad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class ImageButtonSquare extends ImageButton {
    public ImageButtonSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButtonSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageButtonSquare(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ImageButtonSquare(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
