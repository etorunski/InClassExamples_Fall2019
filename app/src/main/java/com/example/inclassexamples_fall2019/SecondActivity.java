package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class        SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button back = findViewById(R.id.previousPageButton);
        back.setOnClickListener(clk -> {


                finish();
        });
    }
}
