package com.example.android_clicker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.CountDownTimer;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    ProgressDialog pd;
    TextView txtJson;
    TextView questionDescText;
    Button optionBtn1;
    Button optionBtn2;
    Button optionBtn3;
    Button optionBtn4;
    Button submitBtn;
    TextView txtTimer;


    ArrayList<Button> optionBtns = new ArrayList<Button>();
    JSONArray questions = null;
    int selectedOptionIndex = 0;
    int currQuestionIndex = 0;
    int correctCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.jeopardythemesong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        questionDescText = findViewById(R.id.questionDescText);

        optionBtn1 = findViewById(R.id.optionBtn1);
        optionBtn2 = findViewById(R.id.optionBtn2);
        optionBtn3 = findViewById(R.id.optionBtn3);
        optionBtn4 = findViewById(R.id.optionBtn4);
        submitBtn = findViewById(R.id.submitBtn);

        txtTimer = (TextView)  findViewById(R.id.txtTimer);
        // Count down from 60 sec. onTick() every second. Values in milliseconds
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisRemaining) {
                txtTimer.setText("Seconds remaining: " + millisRemaining / 1000);
            }
            public void onFinish() {

                submitBtn.performClick();
                start();
            }
        }.start();

        optionBtn1.setOnClickListener(this);
        optionBtn2.setOnClickListener(this);
        optionBtn3.setOnClickListener(this);
        optionBtn4.setOnClickListener(this);

        optionBtns.add(optionBtn1);
        optionBtns.add(optionBtn2);
        optionBtns.add(optionBtn3);
        optionBtns.add(optionBtn4);

        new GetQuestions().execute("http://192.168.1.102:9999/clicker/questions");
    }


    @Override
    public void onClick(View view) {
        deselectButtons();
        switch (view.getId()) {
            case R.id.optionBtn1:
                optionBtn1.setSelected(true);
                this.selectedOptionIndex = 1;
                break;
            case R.id.optionBtn2:
                optionBtn2.setSelected(true);
                this.selectedOptionIndex = 2;
                break;
            case R.id.optionBtn3:
                optionBtn3.setSelected(true);
                this.selectedOptionIndex = 3;
                break;
            case R.id.optionBtn4:
                optionBtn4.setSelected(true);
                this.selectedOptionIndex = 4;
                break;

            default:
                this.selectedOptionIndex = 0;


        }
    }

    public void deselectButtons() {
        for (int i=0; i<optionBtns.size(); i++) {
            optionBtns.get(i).setSelected(false);
        }
    }

    private class GetQuestions extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(QuizActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream in = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            try {
                questions = new JSONArray(result);
                setQuestion(currQuestionIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setQuestion(int currQuestionIndex) {
        try {
            JSONObject question = questions.getJSONObject(currQuestionIndex);
            questionDescText.setText(question.getString("description"));
            JSONArray options = question.getJSONArray("options");
            optionBtn1.setText(options.getJSONObject(0).getString("description"));
            optionBtn2.setText(options.getJSONObject(1).getString("description"));
            optionBtn3.setText(options.getJSONObject(2).getString("description"));
            optionBtn4.setText(options.getJSONObject(3).getString("description"));
            if (currQuestionIndex < 4) {
                submitBtn.setText(R.string.next);
            } else {
                submitBtn.setText(R.string.submit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void nextQuestion(View view) {
        if (currQuestionIndex == 4) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("correctCount", correctCount);
            finish();
            startActivity(intent);
        } else {
            try {
                JSONObject question = questions.getJSONObject(currQuestionIndex);
                int correctAns = question.getInt("correctAns");
                if (correctAns == selectedOptionIndex)
                    correctCount++;
                deselectButtons();
                currQuestionIndex++;
                setQuestion(currQuestionIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}