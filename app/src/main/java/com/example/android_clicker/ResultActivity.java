package com.example.android_clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textCorrectCount;
    int correctCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            correctCount = extras.getInt("correctCount");
        }

        textCorrectCount = findViewById(R.id.textCorrectCount);
        textCorrectCount.setText("Your correct rate: " + correctCount + " / 5");
    }
}