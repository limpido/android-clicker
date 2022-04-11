package com.example.android_clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textCorrectCount;
    int correctCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ImageView imageView = findViewById(R.id.endImage);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            correctCount = extras.getInt("correctCount");
        }

        switch(correctCount){

            case 1:
                imageView.setImageResource(R.drawable.hand1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.hand2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.hand3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.hand4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.hand5);
                break;
            default:
                imageView.setImageResource(R.drawable.hand0);
        }


        textCorrectCount = findViewById(R.id.textCorrectCount);
        textCorrectCount.setText("Your correct rate: " + correctCount + " / 5");
    }
}