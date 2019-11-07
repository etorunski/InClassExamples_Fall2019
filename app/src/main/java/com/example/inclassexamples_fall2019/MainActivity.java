package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);



        //find button 1 from what was loaded, add a click listener
        Button b1 = findViewById(R.id.button1);
        if(b1 != null)
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Message", "You clicked button 1");
                    b1.setText("You got me");
                }
            });


        //use a Lambda function to set a click listener
        Button b2 = (Button)findViewById(R.id.button2);
        b2.setOnClickListener( clk -> {
            Log.i("Message", "You clicked button 1");
            b2.setText("you clicked button 2");
        });


        //find the edit text:
        EditText input = findViewById(R.id.inputtext);


        ImageButton b3 = (ImageButton)findViewById(R.id.button3);
        b3.setOnClickListener( clk -> {


            String answer = input.getText().toString(); // will read what the user put
            Log.i("Message", "You typed:" + answer);
        });
    }
}
