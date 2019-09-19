package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent dataFromPreviousPage = getIntent();
        String nameFromPage1 = dataFromPreviousPage.getStringExtra("name");
        int ageFromPage1 = dataFromPreviousPage.getIntExtra("age", 0);
        String whatUserTyped = dataFromPreviousPage.getStringExtra("typed");

        TextView age = findViewById(R.id.ageField);
        age.setText("Age is:" + ageFromPage1);

        TextView name = findViewById(R.id.nameField);
        name.setText("Name is:" + nameFromPage1);

        TextView typed = findViewById(R.id.typedField);
        typed.setText( "You typed in page 1:"+ whatUserTyped );

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener( clk ->
        {
            setResult(50, null); //a unique number that can only be 50 if you clicked on the back button
            finish();
        });

        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener( bt -> {
            startActivity(new Intent(ThirdActivity.this, SharedPreferencesExample.class));
        });
    }
}
