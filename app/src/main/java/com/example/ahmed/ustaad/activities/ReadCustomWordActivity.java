package com.example.ahmed.ustaad.activities;

/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.ustaad.BaseActivity;
import com.example.ahmed.ustaad.R;
import com.example.ahmed.ustaad.classes.SequenceMediaPlayer;
import com.example.ahmed.ustaad.fragments.DrawFragment;
import com.example.ahmed.ustaad.fragments.MessageDialogFragment;
import com.example.ahmed.ustaad.services.SpeechService;
import com.example.ahmed.ustaad.utilities.VoiceRecorder;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ReadCustomWordActivity extends BaseActivity implements MessageDialogFragment.Listener {

    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static final String STATE_RESULTS = "results";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SpeechService mSpeechService;

    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            showStatus(true);
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            showStatus(false);
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
                stopVoiceRecorder();
                //unbinding service
                //if(mSpeechService != null) {
                //    unbindService(mServiceConnection);
                //    mSpeechService = null;
                //}
            }
            //stopping ripple animation
            //rippleBackground.stopRippleAnimation();
        }

    };

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;

    // View references
    //private TextView mStatus;
    //private TextView mText;
    //private ResultAdapter mAdapter;
    //private RecyclerView mRecyclerView;
    private FrameLayout frameLayoutFragmentContainer;
    private ImageView imageViewMic;
    private RippleBackground rippleBackground;
    private List<Integer> audioIdArr;
    private SequenceMediaPlayer sequenceMediaPlayer;
    private TextView textViewWord;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
            //mStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_custom_word);

        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.status_hearing, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.status_not_hearing, theme);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //getting and setting views
        this.frameLayoutFragmentContainer = this.findViewById(R.id.fragment_container);
        this.imageViewMic = this.findViewById(R.id.image_button_microphone);
        this.imageViewMic.setEnabled(false);
        this.rippleBackground = this.findViewById(R.id.ripple_background);
        this.textViewWord = this.findViewById(R.id.text_view_word);

        //setting views click listeners
        this.setViewsClickListeners();

        //mStatus = (TextView) findViewById(R.id.status);
        //mText = (TextView) findViewById(R.id.text);

        //mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //final ArrayList<String> results = savedInstanceState == null ? null :
        //        savedInstanceState.getStringArrayList(STATE_RESULTS);
        //mAdapter = new ResultAdapter(results);
        //mRecyclerView.setAdapter(mAdapter);

        //setting media player
        this.initialSequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(R.raw.speak_word)));//, R.raw.write_dotted_words, R.raw.navigation_converted, R.raw.back_to_menu)));
        this.initialSequenceMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ReadCustomWordActivity.this.imageViewMic.setEnabled(true);
                ReadCustomWordActivity.this.startRecognition();
            }
        });
        this.sequenceMediaPlayer = new SequenceMediaPlayer(this, new ArrayList<Integer>(Arrays.asList(R.raw.navigation_converted, R.raw.back_to_menu)));
        //this.sequenceMediaPlayer.start();
        //setting sequenceMediaPlayer onCompletionListener
        //this.sequenceMediaPlayer.setOnCompletionListener(this);

    }

    private void setViewsClickListeners() {
        this.imageViewMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadCustomWordActivity.this.imageViewMic.setEnabled(false);
                initialSequenceMediaPlayer.clear();
                sequenceMediaPlayer.clear();
                sequenceMediaPlayer.setAudioFilesArray(new ArrayList<Integer> (Arrays.asList(R.raw.speak_word)));
                sequenceMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ReadCustomWordActivity.this.imageViewMic.setEnabled(true);
                        ReadCustomWordActivity.this.startRecognition();
                    }
                });
                sequenceMediaPlayer.start();
            }
        });
    }

    private void startRecognition() {
        //starting ripple animation
        ReadCustomWordActivity.this.rippleBackground.startRippleAnimation();
        //binding speech service
        //SpeechRecognitionActivity.this.bindService(new Intent(SpeechRecognitionActivity.this, SpeechService.class), mServiceConnection, Service.BIND_AUTO_CREATE);
        //starting voice recorder
        ReadCustomWordActivity.this.stopVoiceRecorder();
        ReadCustomWordActivity.this.startVoiceRecorder();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Prepare Cloud Speech API
        this.bindService(new Intent(this, SpeechService.class), mServiceConnection, Service.BIND_AUTO_CREATE);

        // Start listening to T
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            //startVoiceRecorder();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sequenceMediaPlayer.clear();
    }

    @Override
    protected void onStop() {
        // Stop listening to voice
        stopVoiceRecorder();
        //stopping ripple
        rippleBackground.stopRippleAnimation();
        if(mSpeechService != null) {
            // Stop Cloud Speech API
            mSpeechService.removeListener(mSpeechServiceListener);
            unbindService(mServiceConnection);
            mSpeechService = null;
        }

        super.onStop();
    }

//    @Override
//    public void onSimilarityCheckComplete(float similarity) {
//        Log.d("Similarity", String.valueOf(similarity));
//        if(similarity > 0.4) {
//            //Toast.makeText(this, "Good", Toast.LENGTH_LONG).show();
//            sequenceMediaPlayer.clear();
//            sequenceMediaPlayer.setAudioFilesArray(new ArrayList<Integer> (Arrays.asList(R.raw.correct__answer)));
//            sequenceMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    finish();
//                }
//            });
//            sequenceMediaPlayer.start();
//        }
//        else {
//            this..clearView();
//            sequenceMediaPlayer.clear();
//            sequenceMediaPlayer.setAudioFilesArray(new ArrayList<Integer> (Arrays.asList(R.raw.wrong_answer_converted, R.raw.write_dotted_words, R.raw.navigation_converted, R.raw.back_to_menu)));
//            sequenceMediaPlayer.start();
//
//            //Toast.makeText(this, "Bad", Toast.LENGTH_LONG).show();
//        }
//        //Log.d("similarity", String.valueOf(similarity));
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //if (mAdapter != null) {
        //    outState.putStringArrayList(STATE_RESULTS, mAdapter.getResults());
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecorder();
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_file:
                mSpeechService.recognizeInputStream(getResources().openRawResource(R.raw.audio));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private void showPermissionMessageDialog() {
        MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }

    private void showStatus(final boolean hearingVoice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mStatus.setTextColor(hearingVoice ? mColorHearing : mColorNotHearing);
            }
        });
    }

    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private final SpeechService.Listener mSpeechServiceListener =
            new SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        //mVoiceRecorder.dismiss();
                    }
                    if (textViewWord != null && !TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    //setting word in drawFragment
                                    //getting first word from string
                                    String firstWord = text.split(" ")[0];
                                    textViewWord.setText(firstWord);

                                    //starting sequence media player
                                    sequenceMediaPlayer.clear();
                                    sequenceMediaPlayer.setAudioFilesArray(new ArrayList<Integer>(Arrays.asList( R.raw.navigation_converted, R.raw.back_to_menu)));
                                    sequenceMediaPlayer.setOnCompletionListener(null);
                                    sequenceMediaPlayer.start();

                                    //stopping ripple animation
                                    rippleBackground.stopRippleAnimation();
                                    //if(mSpeechService != null) {
                                    //unbindService(mServiceConnection);
                                    //}
                                    //stopping animation
                                    //mText.setText(null);
                                    //mAdapter.addResult(text);
                                    //mRecyclerView.smoothScrollToPosition(0);
                                } else {
                                    //not final yet
                                    //mText.setText(text);
                                }
                            }
                        });
                    }
                }
            };

    public void prevButtonSelected(View view) {
        this.finish();
    }

    public void backButtonSelected(View view) {
        this.finish();
    }

    public void nextButtonSelected(View view) {
        this.finish();
    }



//    private static class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView text;
//
//        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.item_result, parent, false));
//            text = (TextView) itemView.findViewById(R.id.text);
//        }
//
//    }

//    private static class ResultAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        private final ArrayList<String> mResults = new ArrayList<>();
//
//        ResultAdapter(ArrayList<String> results) {
//            if (results != null) {
//                mResults.addAll(results);
//            }
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.text.setText(mResults.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mResults.size();
//        }
//
//        void addResult(String result) {
//            mResults.add(0, result);
//            notifyItemInserted(0);
//        }
//
//        public ArrayList<String> getResults() {
//            return mResults;
//        }
//
//    }

}
