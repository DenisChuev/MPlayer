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

        ArrayList<String> songs = new ArrayList<>();

        try {
            for (String song : getAssets().list("sample_sounds")) {
                songs.add(song.split(".mp3")[0]);
            }
        } catch (IOException ignored) {
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.rv_sounds);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SoundViewAdapter(this, songs);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("SongsList", adapter.getSongs());
        bundle.putInt("Position", position);
        intent.putExtra("SongsBundle", bundle);

        startActivity(intent);


    }
}