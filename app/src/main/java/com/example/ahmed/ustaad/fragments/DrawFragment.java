package com.example.ahmed.ustaad.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.views.DrawView;
import com.example.ahmed.ustaad.R;
import com.example.ahmed.ustaad.activities.DrawActivity;
import com.example.ahmed.ustaad.asynctasks.SimilarityComparisonAsyncTask;
import com.example.ahmed.ustaad.utilities.Consts;
import com.example.ahmed.ustaad.utilities.Utility;

public class DrawFragment extends Fragment {

    public static interface DrawFragmentCallback {
        public void onSimilarityCheckComplete(float similarity);
    };

    public static final String DRAW_FRAGMENT_WORD_KEY = "word_key";
    public static final String DRAW_FRAGMENT_FONT_OUTLINE_KEY = "font_outline";
    public static final String DRAW_FRAGMENT_FONT_FILLED_KEY = "font_filled";
    public static final String DRAW_FRAGMENT_IS_NUMBER_LAYOUT = "is_number_layout";


    private View view;
    private Context activityContext;
    private DrawView drawView;
    private TextView textViewOutline;
    private TextView textViewFilled;
    private SimilarityComparisonAsyncTask similarityComparisonAsyncTask;
    private ProgressDialog progressDialog;
    private DrawFragmentCallback drawFragmentCallback;
    private String word;
    //private int font_outline;
    //private int font_filled;
    private boolean isNumberLayout;


    public DrawFragment() {
        this.drawFragmentCallback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getting word to show
        //getting arguments
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String word = bundle.getString(DrawFragment.DRAW_FRAGMENT_WORD_KEY);
            if (word != null) {
                this.word = word;
            } else {
                Log.e("DrawFragment", "Word argument must be passed. Setting default word");
                this.word = "";
            }
            this.isNumberLayout = bundle.getBoolean(DrawFragment.DRAW_FRAGMENT_IS_NUMBER_LAYOUT, false);
//            Integer font_outline = bundle.getInt(DRAW_FRAGMENT_FONT_OUTLINE_KEY, 0);
//            if(font_outline != 0) {
//                this.font_outline = font_outline;
//            }
//            Integer font_filled = bundle.getInt(DRAW_FRAGMENT_FONT_FILLED_KEY, 0);
//            if(font_filled != 0) {
////                this.font_filled = font_filled;
//            }
        }
        else {
            this.word = "";
            this.isNumberLayout = false;
//            this.font_filled = 0;
//            this.font_outline = 0;
        }
    }

    public void setWord(String word) {
        this.word = word;
        //setting word in textViews text
        this.textViewFilled.setText(word);
        this.textViewOutline.setText(word);
        //building drawing cache of textviews
        this.textViewFilled.buildDrawingCache();
        this.textViewOutline.buildDrawingCache();
        //clearing drawView
        this.drawView.restartDrawing();
    }

    public String getWord() {
        return this.word;
    }

    public void clearView() {
        this.drawView.restartDrawing();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        if(!this.isNumberLayout) {
            this.view = layoutInflater.inflate(R.layout.fragment_draw, container, false);
        }
        else {
            this.view = layoutInflater.inflate(R.layout.fragment_draw_number, container, false);
        }
        this.progressDialog = new ProgressDialog(this.activityContext);
        this.getSetViews();
        return this.view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activityContext = context;
        this.drawFragmentCallback = (DrawFragmentCallback) context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getSetViews() {
        this.drawView = (DrawView) this.view.findViewById(R.id.draw_view);
        this.textViewOutline = (TextView) this.view.findViewById(R.id.text_view_outline);
        this.textViewFilled = (TextView) this.view.findViewById(R.id.text_view_filled);

        //setting font of textViewOutline
//        if(this.font_outline != 0) {
//            Typeface fontOutlineTypeFace = ResourcesCompat.getFont(this.getContext(), this.font_outline);
//            this.textViewOutline.setTypeface(fontOutlineTypeFace);
//        }
//
//        //setting font of textViewFilled
//        if(this.font_filled != 0) {
//            Typeface fontFilledTypeFace = ResourcesCompat.getFont(this.getContext(), this.font_filled);
//            this.textViewFilled.setTypeface(fontFilledTypeFace);
//        }

        //setting textViews text
        this.textViewOutline.setText(this.word);
        this.textViewFilled.setText(this.word);

        this.textViewFilled.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });

        //setting textView to be able to extract bitmap
        //enabling drawing cache of textview
        textViewOutline.setDrawingCacheEnabled(true);
        textViewFilled.setDrawingCacheEnabled(true);
        //setting width and height of textviewOutline and building drawing cache
        //textViewOutline.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        //        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //textViewOutline.layout(0, 0, textViewOutline.getMeasuredWidth(), textViewOutline.getMeasuredHeight());
        textViewOutline.buildDrawingCache();
        //setting width and height of textviewFilled and building drawing cache
        //textViewFilled.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        //        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //textViewFilled.layout(0, 0, textViewOutline.getMeasuredWidth(), textViewOutline.getMeasuredHeight());
        textViewFilled.buildDrawingCache();

    }


    public void checkSimilarity() {
                //getting image of user canvas
                Object[] objects =  this.drawView.createCapture(DrawingCapture.BITMAP);
                //getting bitmap of user canvas from object
                if(objects != null && objects.length > 0) {
                    Bitmap bitmapUserCanvas = (Bitmap) objects[0];
                    //getting bitmap of textviewFilled
                    Bitmap bitmapFilled = this.textViewFilled.getDrawingCache();
                    if(bitmapFilled != null) {
                        //comparing bitmap
                        //float fractionMatched = DrawActivity.this.compareBitmap(bitmapUserCanvas, bitmapFilled);
                        //Log.d("Matched", String.valueOf(fractionMatched));
                        //Toast.makeText(DrawActivity.this, "Matched: " + fractionMatched, Toast.LENGTH_SHORT).show();
                        //calling SimilarityComparisonAsyncTask
                        if(this.similarityComparisonAsyncTask != null) {
                            this.similarityComparisonAsyncTask.cancel(true);
                        }
                        this.similarityComparisonAsyncTask = new SimilarityComparisonAsyncTask(new SimilarityComparisonAsyncTaskCallbackClass());
                        this.similarityComparisonAsyncTask.execute(bitmapUserCanvas, bitmapFilled);
                    }
                    else{
                        Log.e("OnComparison", "Could not get bitmap of textviewFilled");
                    }
                }
                else{
                    Log.e("OnComparison", "Could not get bitmap of canvas");
                }
                Log.e("sdf","sdf");
    }

    private class SimilarityComparisonAsyncTaskCallbackClass implements SimilarityComparisonAsyncTask.SimilarityComparisonAsyncTaskCallback {

        public SimilarityComparisonAsyncTaskCallbackClass() {

        }

        @Override
        public void onPreExecute() {
            Utility.showProgressDialog(DrawFragment.this.activityContext, DrawFragment.this.progressDialog, R.string.please_wait);
        }

        @Override
        public void onProgressUpdate(int progress) {

        }

        @Override
        public void onPostExecute(float result) {
            DrawFragment.this.drawFragmentCallback.onSimilarityCheckComplete(result);
            Toast.makeText(DrawFragment.this.getContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
            Utility.hideProgressDialog(DrawFragment.this.progressDialog);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
