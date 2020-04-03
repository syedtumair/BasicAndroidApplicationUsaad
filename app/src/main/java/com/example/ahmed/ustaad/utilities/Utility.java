package com.example.ahmed.ustaad.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.ahmed.ustaad.R;
import com.example.ahmed.ustaad.applications.App;

public class Utility {

    /**
     * Compare the bitmapUser with the bitmapFilled and return the percentage of similarity
     * @param bitmapUser
     * @param bitmapFilled
     * @return
     */
    public static float compareBitmap(Bitmap bitmapUser, Bitmap bitmapFilled) {
        if((bitmapFilled.getWidth() == bitmapUser.getWidth()) && (bitmapFilled.getHeight() == bitmapUser.getHeight())) {
            int noOfPixelsMatched = 0;
            int noOfPixelsDrawn = 0;
            int noOfPixelsOriginal = 0;
            for(int i = 0; i < bitmapFilled.getHeight(); ++i) {
                for(int j = 0; j < bitmapFilled.getWidth(); ++j) {
                    if(bitmapUser.getPixel(j, i) != 0) {
                        ++noOfPixelsDrawn;
                        if (bitmapFilled.getPixel(j, i) != 0) {
                            ++noOfPixelsMatched;
                        }
                    }
                    if(bitmapFilled.getPixel(j, i) != 0) {
                        ++noOfPixelsOriginal;
                    }
                }
            }
            //returning fraction of image matched
            if(noOfPixelsDrawn > noOfPixelsOriginal) {
                return (((float)noOfPixelsMatched) / noOfPixelsDrawn);
            }
            else {
                return ((float) noOfPixelsMatched / noOfPixelsOriginal);
            }
        }
        else {
            Log.e("CompareBitmap", "Dimensions of bitmaps are not the same");
            return 0;
        }
    }

    public static void showProgressDialog(Context context, ProgressDialog progressDialog, int message) {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        if(progressDialog.isShowing()) {
            progressDialog.hide();
        }
        progressDialog.setMessage(App.getAppContext().getResources().getString(message));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public static void hideProgressDialog(ProgressDialog progressDialog) {
        progressDialog.hide();
    }

    public static void showProgressDialog(Context context, ProgressDialog progressDialog, String message) {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        if(progressDialog.isShowing()) {
            progressDialog.hide();
        }
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }
}
