package com.apps.meirovichomer.triviagame;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.meirovichomer.triviagame.retroClasses.suggestQuestionClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuggestQuestionActivity extends AppCompatActivity {

    AdView mAdView;

    private EditText questionSuggest;
    private EditText correctSuggest;
    private EditText optionOneSuggest;
    private EditText optionTwoSuggest;
    private EditText optionThreeSuggest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_question);
        adMobConfig();
        buttonSendQuestion();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    private boolean validateFields() {

        questionSuggest = (EditText) findViewById(R.id.question_suggest);
        correctSuggest = (EditText) findViewById(R.id.correct_suggest);
        optionOneSuggest = (EditText) findViewById(R.id.optionOne_suggest);
        optionTwoSuggest = (EditText) findViewById(R.id.optionTwo_suggest);
        optionThreeSuggest = (EditText) findViewById(R.id.optionThree_suggest);

        int questionLength = questionSuggest.getText().length();
        int correctLength = correctSuggest.getText().length();
        int optionOne = optionOneSuggest.getText().length();
        int optionTwo = optionTwoSuggest.getText().length();
        int optionThree = optionThreeSuggest.getText().length();

        boolean validated = true;

        if (questionLength < 1 || questionLength > 50) {
            validated = false;
        } else if (correctLength < 1 || correctLength > 20) {
            validated = false;
        } else if (optionOne < 1 || optionOne > 20) {
            validated = false;
        } else if (optionTwo < 1 || optionTwo > 20) {
            validated = false;
        } else if (optionThree < 1 || optionThree > 20) {
            validated = false;
        }

        return validated;
    }

    private void buttonSendQuestion() {

        Button sendQuestion = (Button) findViewById(R.id.sendSuggestion_btn);

        sendQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (validateFields()) {
                    sendSuggestionRetrofit();
                } else {
                    Toast.makeText(SuggestQuestionActivity.this, "אנא וודא כי כל השדות מלאים", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendSuggestionRetrofit() {
        RetrofitTriviaInterface mApiService = this.getInterfaceService();

        questionSuggest = (EditText) findViewById(R.id.question_suggest);
        correctSuggest = (EditText) findViewById(R.id.correct_suggest);
        optionOneSuggest = (EditText) findViewById(R.id.optionOne_suggest);
        optionTwoSuggest = (EditText) findViewById(R.id.optionTwo_suggest);
        optionThreeSuggest = (EditText) findViewById(R.id.optionThree_suggest);

        data mapRequest = new data();

        mapRequest.putParam("question", questionSuggest.getText().toString());
        mapRequest.putParam("correct", correctSuggest.getText().toString());
        mapRequest.putParam("option_one", optionOneSuggest.getText().toString());
        mapRequest.putParam("option_two", optionTwoSuggest.getText().toString());
        mapRequest.putParam("option_three", optionThreeSuggest.getText().toString());

        Call<suggestQuestionClass> mService = mApiService.suggestQuestion(mapRequest.getParamsData(""));
        mService.enqueue(new Callback<suggestQuestionClass>() {
            @Override
            public void onResponse(@NonNull Call<suggestQuestionClass> call, @NonNull Response<suggestQuestionClass> response) {
                suggestQuestionClass responseArray = response.body();
                Toast.makeText(SuggestQuestionActivity.this, "success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<suggestQuestionClass> call, @NonNull Throwable t) {

                Log.e("ERR", "ERROR HAS OCCURED IN RETUREND RESPONSE PROPRTEIES");

            }

        });
    }

    private RetrofitTriviaInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meirovich-ghost.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RetrofitTriviaInterface mInterfaceService = retrofit.create(RetrofitTriviaInterface.class);
        return mInterfaceService;
    }

    private void adMobConfig() {

        mAdView = (AdView) findViewById(R.id.suggest_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
