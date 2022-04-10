package com.example.android_clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class QuizActivity extends AppCompatActivity {
    ProgressDialog pd;
    TextView txtJson;
    TextView questionDescText;
    Button optionBtn1;
    Button optionBtn2;
    Button optionBtn3;
    Button optionBtn4;
    private TextView txtTimer;
    int currQuestionIndex = 0;
    JSONArray questions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.jeopardythemesong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        setContentView(R.layout.activity_quiz);

        txtJson = (TextView) findViewById(R.id.textJson);
        questionDescText = (TextView) findViewById(R.id.questionDescText);
        optionBtn1 = (Button) findViewById(R.id.optionBtn1);
        optionBtn2 = (Button) findViewById(R.id.optionBtn2);
        optionBtn3 = (Button) findViewById(R.id.optionBtn3);
        optionBtn4 = (Button) findViewById(R.id.optionBtn4);

        new GetQuestions().execute("http://192.168.1.102:9999/clicker/questions");
    }

    private class GetQuestions extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(null, "onPreExecute!!!");

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
//                txtJson.setText(questions.toString());
                SetQuestion(0, questions);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void SetQuestion(int currQuestionIndex, JSONArray questions) {
        try {
            JSONObject question = questions.getJSONObject(currQuestionIndex);
            Log.d(null, question.toString());
            questionDescText.setText(question.getString("description"));
            JSONArray options = question.getJSONArray("options");
            optionBtn1.setText(options.getJSONObject(0).getString("description"));
            optionBtn2.setText(options.getJSONObject(1).getString("description"));
            optionBtn3.setText(options.getJSONObject(2).getString("description"));
            optionBtn4.setText(options.getJSONObject(3).getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}