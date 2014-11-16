package com.github.tarasmazepa.uateam.uateamtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.github.tarasmazepa.uateam.uateamtv.R;
import com.github.tarasmazepa.uateam.uateamtv.activity.base.BaseChildActivity;
import com.github.tarasmazepa.uateam.uateamtv.analytics.Analytics;
import com.github.tarasmazepa.uateam.uateamtv.view.SystemUiHider;

import java.util.Timer;
import java.util.TimerTask;

public class OnlineVideoActivity extends BaseChildActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final boolean TOGGLE_ON_CLICK = true;
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    public static void start(Activity activity, String watchOnlineLink, String title) {
        start(OnlineVideoActivity.class, activity, watchOnlineLink, title);
    }

    private SystemUiHider mSystemUiHider;
    private VideoView videoView;
    private SeekBar seekBar;
    private boolean playing = true;
    private Timer timer;
    private boolean ignoreProgressUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        timer = new Timer();

        final View controlsView = findViewById(R.id.video_controls);
        seekBar = (SeekBar) findViewById(R.id.video_progress);
        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(getIntent().getStringExtra(KEY_LINK)));
        videoView.start();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seekBar.setMax(videoView.getDuration());
                seekBar.setSecondaryProgress(getBufferedAmount());
                if (!ignoreProgressUpdate) {
                    seekBar.setProgress(videoView.getCurrentPosition());
                }
            }
        }, 0, 50);
        seekBar.setOnTouchListener(mDelayHideTouchListener);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int currentPosition = videoView.getCurrentPosition();
                    int buffered = Math.max(getBufferedAmount(), currentPosition);
                    progress = Math.min(progress, buffered);
                    progress = Math.max(progress, currentPosition);
                    this.progress = progress;
                    seekBar.setProgress(this.progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ignoreProgressUpdate = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(progress);
                ignoreProgressUpdate = false;
            }

        });

        mSystemUiHider = new SystemUiHider(this, videoView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
            int mControlsHeight;
            int mShortAnimTime;

            public void onVisibilityChange(boolean visible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    if (mControlsHeight == 0) {
                        mControlsHeight = controlsView.getHeight();
                    }
                    if (mShortAnimTime == 0) {
                        mShortAnimTime = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                    }
                    controlsView.animate()
                            .translationY(visible ? 0 : mControlsHeight)
                            .setDuration(mShortAnimTime);
                } else {
                    controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                if (visible && AUTO_HIDE) {
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        final Button playPauseButton = (Button) findViewById(R.id.video_control_button);
        playPauseButton.setOnTouchListener(mDelayHideTouchListener);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    videoView.pause();
                    playPauseButton.setText(R.string.video_play);
                } else {
                    videoView.resume();
                    playPauseButton.setText(R.string.video_stop);
                }
                playing = !playing;
            }
        });
    }

    private int getBufferedAmount() {
        return Math.max((videoView.getBufferPercentage() * 10 - 5) * videoView.getDuration() / 1000, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_watch_in_other_app:
                analytics.actionGeneral(Analytics.Action.WATCH_VIDEO_OTHER_APP);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra(KEY_LINK))));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        analytics.view(Analytics.ScreenName.VIDEO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
