package org.shi.mplayer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    MediaPlayer player = new MediaPlayer();
    SeekBar sk;
    TextView currentTimeTv;
    TextView totalTimeTv;
    ArrayList<String> songs;
    int currentPosition;
    String songName;
    TextView songNameTV;
    ImageView play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("SongsBundle");
        songs = bundle.getStringArrayList("SongsList");
        currentPosition = bundle.getInt("Position");
        songName = songs.get(currentPosition);

        songNameTV = findViewById(R.id.song_name);
        songNameTV.setText(songName);

        ImageView nextSong = findViewById(R.id.skip_next_btn);
        nextSong.setOnClickListener(view -> {
            if (currentPosition + 1 < songs.size()) {
                ++currentPosition;
                changeSong();
            }
        });

        ImageView prevSong = findViewById(R.id.skip_prev_btn);
        prevSong.setOnClickListener(view ->{
            if (currentPosition != 0) {
                --currentPosition;
                changeSong();
            }
        });

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

        play = findViewById(R.id.play_btn);
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

    private void changeSong() {
        songName = songs.get(currentPosition);
        songNameTV.setText(songName);
        player.stop();
        player = new MediaPlayer();
        playMusic(songName);
        play.setImageResource(R.drawable.ic_pause);
        Log.d("PlayerActivity", songName);
    }

    private void stopMusic() {
        player.pause();
    }

    public void playMusic(String songName) {
        AssetFileDescriptor afd;
        try {
            afd = getAssets().openFd("sample_sounds/" + songName + ".mp3");
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            player.setVolume(1f, 1f);
            player.setLooping(true);
            player.prepare();
            sk.setMax(player.getDuration());
            totalTimeTv.setText(getFormattedDuration(player.getDuration()));
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startMusic(String songName) {
        if (player.getCurrentPosition() == 0) {
            playMusic(songName);
        } else {
            player.start();
        }
        changeSeekbar();
    }

    private void changeSeekbar() {
        int currentTime = player.getCurrentPosition();
        sk.setProgress(currentTime);
        currentTimeTv.setText(getFormattedDuration(currentTime));

        if (player.isPlaying()) {
            handler.postDelayed(this::changeSeekbar, 250);
        }
    }

    private String getFormattedDuration(int duration) {
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