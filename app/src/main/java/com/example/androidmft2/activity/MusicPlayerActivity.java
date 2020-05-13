package com.example.androidmft2.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.androidmft2.R;
import com.example.androidmft2.databinding.ActivityMusicPlayerBinding;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String TAG = "MusicPlayerActivity";
    private ActivityMusicPlayerBinding activityMusicPlayerBinding;
    private MediaPlayer mediaPlayer;
//    private Boolean playCheck = false;
    private Timer timer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMusicPlayerBinding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        View root = activityMusicPlayerBinding.getRoot();
        setContentView(root);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(MusicPlayerActivity.this, Uri.parse("https://dl.nex1music.ir/1399/02/11/Saman%20Jalili%20-%20Azadi%20[128].mp3?time=1588775134&filename=/1399/02/11/Saman%20Jalili%20-%20Azadi%20[128].mp3"));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    setUpView();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
//            playCheck = false;
        }

//        activityMusicPlayerBinding.play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!playCheck) {
//                    playCheck = true;
//                    mediaPlayer.prepareAsync();
//                }
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//                        if (!mediaPlayer.isPlaying()) {
//                            mediaPlayer.start();
//                            activityMusicPlayerBinding.play.setImageResource(R.drawable.ic_pause_black_24dp);
//                        }
//                    }
//                });
//                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.pause();
//                    activityMusicPlayerBinding.play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
//                } else {
//                    mediaPlayer.start();
//                    activityMusicPlayerBinding.play.setImageResource(R.drawable.ic_pause_black_24dp);
//                }
//            }
//        });


    }

    private void setUpView() {
        activityMusicPlayerBinding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    activityMusicPlayerBinding.play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    mediaPlayer.start();
                    activityMusicPlayerBinding.play.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });

        activityMusicPlayerBinding.duration.setText(formatDuration(mediaPlayer.getDuration()));
        activityMusicPlayerBinding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
            }
        });
        activityMusicPlayerBinding.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
            }
        });

        timer = new Timer();
        timer.schedule(new MyTimrer(),0,1000);

        activityMusicPlayerBinding.seekBar.setMax(mediaPlayer.getDuration()/1000);
        activityMusicPlayerBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b && mediaPlayer != null){
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private class MyTimrer extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityMusicPlayerBinding.currentDuration.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                    activityMusicPlayerBinding.seekBar.setProgress(mediaPlayer.getCurrentPosition() / 1000);
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


}
