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
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.os.CountDownTimer;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    ProgressDialog pd;
    TextView questionDescText;
    Button optionBtn1;
    Button optionBtn2;
    Button optionBtn3;
    Button optionBtn4;
    Button submitBtn;
    TextView txtTimer;


    ArrayList<Button> optionBtns = new ArrayList<Button>();
    JSONArray questions = null;
    JSONObject currQuestion = null;
    int selectedOptionIndex = 0;
    int currQuestionIndex = 0;
    int correctCount = 0;
    CountDownTimer timer;
    int[] indexArray = new int[5];
    JSONObject currAnswerStats = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.voices);
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
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisRemaining) {
                txtTimer.setText("Seconds remaining: " + millisRemaining / 1000);
            }
            public void onFinish() {
                submitBtn.performClick();
            }
        };

        optionBtn1.setOnClickListener(this);
        optionBtn2.setOnClickListener(this);
        optionBtn3.setOnClickListener(this);
        optionBtn4.setOnClickListener(this);

        optionBtns.add(optionBtn1);
        optionBtns.add(optionBtn2);
        optionBtns.add(optionBtn3);
        optionBtns.add(optionBtn4);

        new GetQuestions().execute("http://172.20.10.13:9999/clicker/questions");
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
                break;
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

                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
                    int statusCode = httpConn.getResponseCode();
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
                int length = questions.length();
                Set<Integer> indexSet = new HashSet<>();
                while(indexSet.size() != 5){
                    // [0, 1)  [0, 19)
                    int randomIndex = (int) (Math.random() * length);
                    indexSet.add(randomIndex);
                }
                int i = 0;
                for (int index: indexSet) {
                    indexArray[i++] = index;
                }

                setQuestion(indexArray[currQuestionIndex]);
                timer.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveCurrAnswerStats() {
        new SaveAnswerStats().execute("http://172.20.10.13:9999/clicker/stats");
    }

    private class SaveAnswerStats extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Content-Length", Integer.toString(currAnswerStats.toString().getBytes().length));
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(currAnswerStats.toString());
                writer.flush();
                writer.close();
                os.close();

                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
                    int statusCode = httpConn.getResponseCode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void setQuestion(int qindex) {
        try {
            currQuestion = questions.getJSONObject(qindex);
            questionDescText.setText(currQuestion.getString("description"));
            JSONArray options = currQuestion.getJSONArray("options");
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
        final MediaPlayer mediaPlayer2 = MediaPlayer.create(this, R.raw.scream);
        mediaPlayer2.start();
        try {
            JSONObject question = questions.getJSONObject(currQuestionIndex);
            int correctAns = question.getInt("correctAns");
            if (correctAns == selectedOptionIndex) {
                correctCount++;
                currAnswerStats.put("isCorrect", true);
            } else {
                currAnswerStats.put("isCorrect", false);
            }
            currAnswerStats.put("selectedOption", selectedOptionIndex);
            currAnswerStats.put("id", currQuestion.getInt("questionId"));
            saveCurrAnswerStats();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (currQuestionIndex == 4) {
            timer.cancel();
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("correctCount", correctCount);
            finish();
            startActivity(intent);
        } else {
            deselectButtons();
            currQuestionIndex++;
            setQuestion(indexArray[currQuestionIndex] );
            restartTimer();
        }
    }

    private void restartTimer() {
        timer.cancel();
        timer.start();
    }


}