package com.example.inclassexamples_fall2019;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //find button 1 from what was loaded, add a click listener
        Button page1Button = findViewById(R.id.firstButton);
        if(page1Button != null)
            page1Button.setOnClickListener(v -> {
                Intent goToPage2 = new Intent(FirstActivity.this, SecondActivity.class);
                    startActivity(goToPage2);
            });

        EditText editText = findViewById(R.id.userInput);

        //use a Lambda function to set a click listener
        Button page2Button = (Button)findViewById(R.id.secondButton);
        if(page2Button != null){
            page2Button.setOnClickListener(clk -> {
                Intent goToPage3 = new Intent(FirstActivity.this, ThirdActivity.class);

                goToPage3.putExtra("name", "Eric");
                goToPage3.putExtra("age", 20);
                goToPage3.putExtra("typed", editText.getText().toString());

                startActivityForResult(goToPage3, 30);
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 30) //you went to page 3 (look at line 38)
        {
            if(resultCode == 50) //you hit the finish button
            {
                Toast.makeText(this, "You came back from page 3 by hitting the finish button",
                        Toast.LENGTH_LONG).show();
            }
            else if(resultCode == RESULT_CANCELED) //you hit the back button
            {
                Toast.makeText(this, "You came back from page 3 by hitting the back button",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
