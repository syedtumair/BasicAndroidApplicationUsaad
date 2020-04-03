package com.example.ahmed.ustaad.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.ustaad.activities.DrawActivity;
import com.example.ahmed.ustaad.utilities.Utility;

public class SimilarityComparisonAsyncTask extends AsyncTask<Bitmap, Integer, Float> {

    SimilarityComparisonAsyncTaskCallback callback;

    public SimilarityComparisonAsyncTask(SimilarityComparisonAsyncTaskCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        this.callback.onPreExecute();
    }

    @Override
    public Float doInBackground(Bitmap... bitmaps) {
        //checking if 2 bitmaps have been passed
        if(bitmaps.length < 1) {
            Log.e("SimilarCompariAsyncTask","SimilarityComparisonAsyncTask requires two bitmaps for comparison");
            return Float.valueOf(0);
        }
        //getting bitmaps
        Bitmap bitmapUserCanvas = bitmaps[0];
        Bitmap bitmapFilled = bitmaps[1];
        //comparing bitmap
        float fractionMatched = Utility.compareBitmap(bitmapUserCanvas, bitmapFilled);
        Log.d("Matched", String.valueOf(fractionMatched));
        return fractionMatched;
        //Toast.makeText(DrawActivity.this, "Matched: " + fractionMatched, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(Integer... progresses) {
        super.onProgressUpdate(progresses);
        int progress = progresses[0];
        this.callback.onProgressUpdate(progress);
    }

    @Override
    public void onPostExecute(Float similarity) {
        super.onPostExecute(similarity);
        this.callback.onPostExecute(similarity);
    }

    public static interface SimilarityComparisonAsyncTaskCallback {
        public void onPreExecute();
        public void onProgressUpdate(int progress);
        public void onPostExecute(float similarity);
    }
}