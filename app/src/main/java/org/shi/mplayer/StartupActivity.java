package org.shi.mplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        new Thread(() -> {
            delay();
            startApp();
            finish();
        }).start();
    }

    private void delay() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startApp() {
        Intent i = new Intent(StartupActivity.this, MainActivity.class);
        startActivity(i);
    }
}