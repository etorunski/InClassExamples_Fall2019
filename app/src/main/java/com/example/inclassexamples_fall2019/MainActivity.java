package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent playMovie = new Intent(this, PlayerActivity.class);

        //set button 1 to play this movie:
        Button playButton = findViewById(R.id.button1);
        playButton.setOnClickListener( click -> {
            playMovie.putExtra(
                    "URL",
                    "http://ftp.halifax.rwth-aachen.de/blender/demo/movies/ToS/tears_of_steel_720p.mov"
            );
            startActivity(playMovie);
        });


        //set button 2 to play this movie:
        playButton = findViewById(R.id.button2);
        playButton.setOnClickListener( click -> {
            playMovie.putExtra("URL", "http://peach.themazzone.com/durian/movies/sintel-1280-surround.mp4");
            startActivity(playMovie);
        });

        //set button 3 to play this movie:
        playButton = findViewById(R.id.button3);
        playButton.setOnClickListener( click -> {
            playMovie.putExtra("URL", "https://archive.org/download/ElephantsDream/ed_1024_512kb.mp4");
            startActivity(playMovie);
        });

        //set button 4 is for weather forecast
        playButton = findViewById(R.id.button4);
        playButton.setOnClickListener( click -> {
            startActivity(new Intent(this, WeatherActivity.class));
        });
    }
}
