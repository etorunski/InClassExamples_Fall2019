package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        //find button 1 from what was loaded, add a click listener
        Button b1 = findViewById(R.id.button1);
        if(b1 != null)
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Message", "You clicked button 1");
                }
            });

        //use a Lambda function to set a click listener
        Button b2 = (Button)findViewById(R.id.button2);
        b2.setOnClickListener( clk -> Log.i("Message", "You clicked button 1"));

         */
    }
}
