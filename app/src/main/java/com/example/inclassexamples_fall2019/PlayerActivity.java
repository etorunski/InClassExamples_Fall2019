package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerActivity extends AppCompatActivity {


    private int position = 0;
    private MediaController mediaController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String URL = getIntent().getStringExtra("URL");

        VideoView player = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(player);
        mediaController.setMediaPlayer(player);

        player.setMediaController(mediaController);
        player.setVideoURI(Uri.parse(URL));
        player.setOnPreparedListener( video ->{
            player.seekTo(position);
            if(position == 0)
                player.start();
        });
    }
}
