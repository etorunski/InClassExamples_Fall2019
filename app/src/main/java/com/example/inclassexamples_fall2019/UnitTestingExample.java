package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class UnitTestingExample extends AppCompatActivity {

    Button b1, b2;
    EditText response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_testing_example);

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        response = findViewById(R.id.response);

        b1.setOnClickListener(click -> response.setText("Clicked button 1"));
        b2.setOnClickListener(click -> response.setText("Clicked button 2"));
    }
}
