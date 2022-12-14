package org.shi.mplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SoundViewAdapter.ItemClickListener {
    SoundViewAdapter adapter;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> sounds = new ArrayList<>();

        try {
            for (String sound : getAssets().list("sample_sounds")) {
                sounds.add(sound.split(".mp3")[0]);
            }
        } catch (IOException ignored) {
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_sounds);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SoundViewAdapter(this, sounds);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra("SongName", adapter.getItem(position));
        Log.d("MainActivity", adapter.getItem(position));

        startActivity(intent);

//        mediaPlayer.stop();
//        mediaPlayer = new MediaPlayer();
//
//        AssetFileDescriptor afd;
//        try {
//            afd = getAssets().openFd("sample_sounds/" + adapter.getItem(position) + ".mp3");
//            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            afd.close();
//
//            mediaPlayer.setVolume(1f, 1f);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}