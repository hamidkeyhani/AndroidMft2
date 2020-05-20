package com.example.androidmft2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.androidmft2.R;
import com.example.androidmft2.databinding.ActivityMusicPlayerBinding;
import com.example.androidmft2.databinding.ActivityVideoPlayerBinding;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class VideoPlayerActivity extends AppCompatActivity {

    private ActivityVideoPlayerBinding actvityVideoBinding;
    private Timer timer;
    private RelativeLayout.LayoutParams portraitLayoutParams;
    private RelativeLayout.LayoutParams landscapeLayoutParams;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actvityVideoBinding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        View root = actvityVideoBinding.getRoot();
        setContentView(root);

        actvityVideoBinding.videoView.setVideoURI(Uri.parse("https://dl.bestshow.co/public/uploads/officials/bestshow/video/2018/09/1809GoREA/1809GoREA_720p.mp4"));
        actvityVideoBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setLayoutParams();
                setViews();
            }
        });
    }

    private void setViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actvityVideoBinding.seekbar.setSecondaryProgressTintList(ContextCompat.getColorStateList(this,R.color.red));
        }
        actvityVideoBinding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actvityVideoBinding.videoView.isPlaying()) {
                    actvityVideoBinding.videoView.pause();
                    actvityVideoBinding.play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    actvityVideoBinding.videoView.start();
                    actvityVideoBinding.play.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });
        actvityVideoBinding.duration.setText(formatDuration(actvityVideoBinding.videoView.getDuration()));
        actvityVideoBinding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actvityVideoBinding.videoView.seekTo(actvityVideoBinding.videoView.getCurrentPosition() + 5000);
            }
        });

        actvityVideoBinding.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actvityVideoBinding.videoView.seekTo(actvityVideoBinding.videoView.getCurrentPosition() - 5000);
            }
        });

        timer = new Timer();
        timer.schedule(new MyTimrer(),0,1000);

        actvityVideoBinding.seekbar.setMax(actvityVideoBinding.videoView.getDuration()/1000);
        actvityVideoBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b && actvityVideoBinding.videoView != null){
                    actvityVideoBinding.videoView.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        actvityVideoBinding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                actvityVideoBinding.play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                actvityVideoBinding.currentDuration.setText(formatDuration(0));
                actvityVideoBinding.seekbar.setProgress(0);
                timer.cancel();
            }
        });

    }

    private class MyTimrer extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (actvityVideoBinding.videoView != null) {
                        actvityVideoBinding.currentDuration.setText(formatDuration(actvityVideoBinding.videoView.getCurrentPosition()));
                        actvityVideoBinding.seekbar.setProgress(actvityVideoBinding.videoView.getCurrentPosition() / 1000);
                        // todo : fix this line bug
                        actvityVideoBinding.seekbar.setSecondaryProgress((actvityVideoBinding.videoView.getBufferPercentage()*actvityVideoBinding.videoView.getDuration())/1000);
                    }
                }
            });
        }
    }

    private String formatDuration(long duration) {
        int seconds = (int) (duration / 1000);
        int min = seconds / 60;
        seconds%=60;
        return String.format(Locale.ENGLISH, "%02d",min) + ":" + String.format(Locale.ENGLISH,"%02d",seconds);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        timer.purge();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            actvityVideoBinding.frameLayout.setLayoutParams(portraitLayoutParams);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            actvityVideoBinding.frameLayout.setLayoutParams(landscapeLayoutParams);
            actvityVideoBinding.frameLayout.bringToFront();
            // clear status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void setLayoutParams(){
        landscapeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        portraitLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        portraitLayoutParams.addRule(RelativeLayout.BELOW, actvityVideoBinding.toolbar.getId());
        portraitLayoutParams.addRule(RelativeLayout.ABOVE, actvityVideoBinding.container.getId());
    }
}
