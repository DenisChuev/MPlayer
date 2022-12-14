package org.shi.mplayer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity{
    private Handler handler = new Handler();
    MediaPlayer player = new MediaPlayer();
    SeekBar sk;
    TextView currentTimeTv;
    TextView totalTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        String songName = intent.getStringExtra("SongName");
        TextView songNameTV = findViewById(R.id.song_name);
        songNameTV.setText(songName);

        sk = findViewById(R.id.seek_bar);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);

        ImageView play = findViewById(R.id.play_btn);
        play.setOnClickListener(view -> {
            if (!player.isPlaying()) {
                play.setImageResource(R.drawable.ic_pause);
                startMusic(songName);
            } else {
                play.setImageResource(R.drawable.ic_play);
                stopMusic();
            }
        });

    }




    private void stopMusic() {
        player.pause();
    }

    public void startMusic(String songName) {
        if (player.getCurrentPosition() == 0) {
            AssetFileDescriptor afd;
            try {
                afd = getAssets().openFd("sample_sounds/" + songName + ".mp3");
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();

                player.setVolume(1f, 1f);
                player.setLooping(true);
                player.prepare();
                sk.setMax(player.getDuration());
                totalTimeTv.setText(getFormatedDuration(player.getDuration()));
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.start();
        }
        changeSeekbar();
    }

    private void changeSeekbar() {
        int currentTime = player.getCurrentPosition();
        sk.setProgress(currentTime);
        currentTimeTv.setText(getFormatedDuration(currentTime));

        if (player.isPlaying()) {
            handler.postDelayed(() -> changeSeekbar(), 250);
        }
    }

    private String getFormatedDuration(int duration) {
        int minutes = duration / 1000 / 60;
        int seconds = duration / 1000 - minutes * 60;
        String sec = seconds < 10 ? "0" + seconds : "" + seconds;
        return "" + minutes + ":" + sec;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}