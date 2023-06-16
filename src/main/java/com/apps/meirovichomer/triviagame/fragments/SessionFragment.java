package com.apps.meirovichomer.triviagame.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.meirovichomer.triviagame.LoadingGameDialog;
import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.RetrofitTriviaInterface;
import com.apps.meirovichomer.triviagame.data;
import com.apps.meirovichomer.triviagame.retroClasses.QuestionsClass;
import com.apps.meirovichomer.triviagame.retroClasses.questionClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SessionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SessionFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /***********************************************
     *          Session Variables                  *
     * **********************************************/
    // adView for adMob!
    private AdView mAdView;

    // current session questions
    private QuestionsClass questions;

    // Timer question
    private CountDownTimer currentTimer;
    // Timer for moving between questions
    private CountDownTimer answerTimerCount;
    // Loading timer
    private CountDownTimer loadingCountDown;


    // Score counter, correctAnswers and seconds left.
    private Integer scoreCounter = 0;

    // question counter counts the current question of a user.
    private Integer questionCounter = 0;
    // number of times the user has failed to answer a question.
    private Integer strikeCounter = 0;


    // Number of questions in the given questions array.
    private Integer numberOfQuestion = 5;


    /**
     * @secondsLeft & @TIMER_SECONDS should always be synchronized. TIMER_SECONDS = secondsLeft * 1000.
     */
    // Seconds left in a question.
    private Integer secondsLeft = 21;

    // fixed score amount for correct answer.
    final int CORRECT_SCORE = 200;
    final int TIMER_SECONDS = 21000;
    final int ANSWER_DURATION = 1500;

    //buttons
    private Button firstButton;
    private Button secondButton;
    private Button thirdButton;
    private Button fourthButton;

    private LoadingGameDialog loadDialog;

    // Text view
    TextView timerText;

    //Pop-up adMob
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    /**
     * @FINISHED_FLG - If failed 3 questions or answered all questions flag is true.
     * @PAUSED_FRAG_FLG - If fragment is paused flag true.
     * @ANSWER_PAUSED_FLG - If answer_button clicked flag true.
     * @FIRSTRUN_FLG - If it is the first fragment run flag true.
     */
    private boolean FINISHED_FLG = false;
    private boolean PAUSED_FRAG_FLG = false;
    private boolean ANSWER_PAUSED_FLG = false;
    private boolean FIRSTRUN_FLG = false;
    private boolean QUESTION_TIMER_FLG = false;

    // shared prefs, later on instantiate it and give it the sharedPrefs.
    private SharedPreferences prefs;

    /**
     * END OF SESSION VARIAVLES
     */

    private OnFragmentInteractionListener mListener;

    public SessionFragment() {
        // Required empty public constructor
    }

    public static SessionFragment newInstance(String param1, String param2) {
        SessionFragment fragment = new SessionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        FIRSTRUN_FLG = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Cancel all the timers to avoid crashes.
        PAUSED_FRAG_FLG = true;
        if (currentTimer != null) {
            currentTimer.cancel();
            currentTimer = null;
        }
        if (answerTimerCount != null) {
            answerTimerCount.cancel();
            answerTimerCount = null;
            ANSWER_PAUSED_FLG = true;
        }
        // If loading is up, finish activity.
        if (loadingCountDown != null) {
            loadingCountDown.cancel();
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start timer again from secondsLeft when fragment was paused.
        if (PAUSED_FRAG_FLG && !(ANSWER_PAUSED_FLG)) {
            timerHandler(secondsLeft * 1000);
            PAUSED_FRAG_FLG = false;
        } else if (ANSWER_PAUSED_FLG) {
            ANSWER_PAUSED_FLG = false;
            nextQuestionHandler();
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // AdMob configs.
        adMobConfig();
        if (FIRSTRUN_FLG) {
            // getQuestionsRetrofit will take the data from the server.
            getQuestionsRetrofit();
            // Show the loading panda dialog.
            loadDialog = new LoadingGameDialog(getActivity());
            loadDialog.setCancelable(false);
            loadDialog.show();
        }
        // button click events.
        answersOnClick();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Get the data from the server using retrofit.
    private void getQuestionsRetrofit() {
        RetrofitTriviaInterface mApiService = this.getInterfaceService();
        // Get shared prefs
        data mapRequest = new data();
        mapRequest.putParam("category_id", "10");
        Call<QuestionsClass> mService = mApiService.getRandomTen(mapRequest.getParamsData("getRandomQuestions"));
        mService.enqueue(new Callback<QuestionsClass>() {
            @Override
            public void onResponse(@NonNull Call<QuestionsClass> call, @NonNull Response<QuestionsClass> response) {
                QuestionsClass responseArray = response.body();
                questions = responseArray;
                numberOfQuestion = responseArray.getQuestions().size();
                initializeQuestion();
            }

            @Override
            public void onFailure(@NonNull Call<QuestionsClass> call, @NonNull Throwable t) {

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

    // Config adMob banner.
    private void adMobConfig() {

        mAdView = (AdView) getActivity().findViewById(R.id.session_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    // First question initialize.
    private void initializeQuestion() {

        // get the current question.
        questionClass questionObj = questions.getQuestions().get(questionCounter);

        // answersToArray returns a shuffled array.
        String[] answers = answersToArray(questionObj);
        setQuestion(questionObj.getQuestion(), answers);

        if (FIRSTRUN_FLG) {
            FIRSTRUN_FLG = false;
            loadingTimer();
        } else {
            timerHandler(TIMER_SECONDS);
        }
    }

    // get questionObject, take the answers into an array, shuffle the array and return.
    private String[] answersToArray(questionClass questionObject) {


        String[] answersArray = new String[4];
        answersArray[0] = questionObject.getAnswerOne();
        answersArray[1] = questionObject.getAnswerTwo();
        answersArray[2] = questionObject.getAnswerThree();
        answersArray[3] = questionObject.getCorrectAnswer();

        Collections.shuffle(Arrays.asList(answersArray));

        return answersArray;
    }

    // function sets the question and answers in the fragment_session layout.
    private void setQuestion(String question, String[] answers) {

        TextView questionText = (TextView) getActivity().findViewById(R.id.question_text);
        firstButton = (Button) getActivity().findViewById(R.id.option_one);
        secondButton = (Button) getActivity().findViewById(R.id.option_two);
        thirdButton = (Button) getActivity().findViewById(R.id.option_three);
        fourthButton = (Button) getActivity().findViewById(R.id.option_four);

        questionText.setText(question);
        firstButton.setText(answers[0]);
        secondButton.setText(answers[1]);
        thirdButton.setText(answers[2]);
        fourthButton.setText(answers[3]);

    }

    // All the option_buttons click events.
    private void answersOnClick() {

        firstButton = (Button) getActivity().findViewById(R.id.option_one);
        secondButton = (Button) getActivity().findViewById(R.id.option_two);
        thirdButton = (Button) getActivity().findViewById(R.id.option_three);
        fourthButton = (Button) getActivity().findViewById(R.id.option_four);

        firstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonsUnclickable();
                currentTimer.cancel();
                currentTimer = null;
                int timeLeft = secondsLeft;
                // If the clicked answer was wrong.
                if (!(isCorrectAnswer(firstButton.getText().toString()))) {
                    strikeCounter++;
                    firstButton.setBackground(getResources().getDrawable(R.drawable.false_button));
                    colorCorrectButton();
                    answerTimer();
                } else {
                    scoreCounter += CORRECT_SCORE + (timeLeft * 2);
                    firstButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
                    answerTimer();
                }
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonsUnclickable();
                currentTimer.cancel();
                currentTimer = null;
                int timeLeft = secondsLeft;
                // If the clicked answer was wrong.
                if (!(isCorrectAnswer(secondButton.getText().toString()))) {
                    strikeCounter++;
                    secondButton.setBackground(getResources().getDrawable(R.drawable.false_button));
                    colorCorrectButton();
                    answerTimer();
                } else {
                    scoreCounter += CORRECT_SCORE + (timeLeft * 2);
                    secondButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
                    answerTimer();
                }
            }
        });

        thirdButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonsUnclickable();
                currentTimer.cancel();
                currentTimer = null;
                int timeLeft = secondsLeft;
                // If the clicked answer was wrong.
                if (!(isCorrectAnswer(thirdButton.getText().toString()))) {
                    strikeCounter++;
                    thirdButton.setBackground(getResources().getDrawable(R.drawable.false_button));
                    colorCorrectButton();
                    answerTimer();
                } else {
                    scoreCounter += CORRECT_SCORE + (timeLeft * 2);
                    thirdButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
                    answerTimer();
                }
            }
        });

        fourthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonsUnclickable();
                currentTimer.cancel();
                currentTimer = null;
                int timeLeft = secondsLeft;
                // If the clicked answer was wrong.
                if (!(isCorrectAnswer(fourthButton.getText().toString()))) {
                    strikeCounter++;
                    fourthButton.setBackground(getResources().getDrawable(R.drawable.false_button));
                    colorCorrectButton();
                    answerTimer();
                } else {
                    scoreCounter += CORRECT_SCORE + (timeLeft * 2);
                    fourthButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
                    answerTimer();
                }
            }
        });
    }

    // Make buttons UnClickable to prevent double-click crushes.
    private void buttonsUnclickable() {

        firstButton = (Button) getActivity().findViewById(R.id.option_one);
        secondButton = (Button) getActivity().findViewById(R.id.option_two);
        thirdButton = (Button) getActivity().findViewById(R.id.option_three);
        fourthButton = (Button) getActivity().findViewById(R.id.option_four);

        firstButton.setClickable(false);
        secondButton.setClickable(false);
        thirdButton.setClickable(false);
        fourthButton.setClickable(false);

    }

    // Change the button background of the correct answer to the green button design.
    private void colorCorrectButton() {

        firstButton = (Button) getActivity().findViewById(R.id.option_one);
        secondButton = (Button) getActivity().findViewById(R.id.option_two);
        thirdButton = (Button) getActivity().findViewById(R.id.option_three);
        fourthButton = (Button) getActivity().findViewById(R.id.option_four);

        if (isCorrectAnswer(firstButton.getText().toString())) {
            firstButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
        } else if (isCorrectAnswer(secondButton.getText().toString())) {
            secondButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
        } else if (isCorrectAnswer(thirdButton.getText().toString())) {
            thirdButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
        } else if (isCorrectAnswer(fourthButton.getText().toString())) {
            fourthButton.setBackground(getResources().getDrawable(R.drawable.correct_button));
        }
    }

    // Reset the colors of all buttons to the regular button design.
    private void resetButtonsColor() {

        firstButton = (Button) getActivity().findViewById(R.id.option_one);
        secondButton = (Button) getActivity().findViewById(R.id.option_two);
        thirdButton = (Button) getActivity().findViewById(R.id.option_three);
        fourthButton = (Button) getActivity().findViewById(R.id.option_four);

        firstButton.setBackgroundResource(R.drawable.regular_btn);
        secondButton.setBackgroundResource(R.drawable.regular_btn);
        thirdButton.setBackgroundResource(R.drawable.regular_btn);
        fourthButton.setBackgroundResource(R.drawable.regular_btn);
    }

    // Check if an answer given equals the correct answer.
    private boolean isCorrectAnswer(String answerToCheck) {
        return Objects.equals(questions.getQuestions().get(questionCounter).getCorrectAnswer(), answerToCheck);
    }

    // This function will handle the session after first question initialize.
    private void nextQuestionHandler() {

        // Cancel the timer that was used to pause answers.
        // Question Counter increase by one for next question.
        questionCounter++;
        secondsLeft = 21;

        // check if user has 3 strikes.
        if (strikeCounter == 3) {
            FINISHED_FLG = true;
            setSharedPrefsScore();
            toFinishSessionFragment();
        }
        //check that there are more questions in the database.
        if (isQuestionCounterFull()) {
            FINISHED_FLG = true;
            setSharedPrefsScore();
            toFinishSessionFragment();
        }

        // If failed/finished the triviaSession don't initialize next question.
        if (!FINISHED_FLG) {
            colorStrike();
            viewQuestionCounter();
            buttonsClickable();
            resetButtonsColor();
            initializeQuestion();
            isPopUpAdValid();
        }
    }

    // Check if questionCounter has reached the maximum amount of questions.
    private boolean isQuestionCounterFull() {
        return questionCounter.equals(numberOfQuestion);
    }

    // Make buttons clickable.
    private void buttonsClickable() {

        firstButton = (Button) getActivity().findViewById(R.id.option_one);
        secondButton = (Button) getActivity().findViewById(R.id.option_two);
        thirdButton = (Button) getActivity().findViewById(R.id.option_three);
        fourthButton = (Button) getActivity().findViewById(R.id.option_four);

        firstButton.setClickable(true);
        secondButton.setClickable(true);
        thirdButton.setClickable(true);
        fourthButton.setClickable(true);

    }

    // Timer for the questions, When times up, give the user a strike and move to the next question.
    private void timerHandler(final long timerSeconds) {

        timerText = (TextView) getActivity().findViewById(R.id.timer_view);
        currentTimer = new CountDownTimer(timerSeconds, 1000) {


            public void onTick(long millisUntilFinished) {
                if (!QUESTION_TIMER_FLG) {
                    QUESTION_TIMER_FLG = true;
                }
                timerText.setText(" " + millisUntilFinished / 1000);
                secondsLeft = (int) (millisUntilFinished / 1000);
            }

            public void onFinish() {
                if (!FINISHED_FLG) {
                    timerText.setText("0");
                    colorCorrectButton();
                    strikeCounter++;
                    currentTimer.cancel();
                    currentTimer = null;
                    answerTimer();
                    QUESTION_TIMER_FLG = false;
                }
            }
        }.start();
    }

    // Timer for when an answer was clicked, pause the game for a certain amount of time.
    private void answerTimer() {

        answerTimerCount = new CountDownTimer(ANSWER_DURATION, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                nextQuestionHandler();
                answerTimerCount.cancel();
                answerTimerCount = null;
            }
        }.start();
    }

    // The panda loading timer, Only start playing when loading is finished.
    private void loadingTimer() {

        loadingCountDown = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                setViewVisible();
                timerHandler(TIMER_SECONDS);
            }
        }.start();
    }

    // Show the number of questions answered.
    private void viewQuestionCounter() {

        TextView questionTxt = (TextView) getActivity().findViewById(R.id.question_counter_text);

        questionTxt.setText(questionCounter.toString());
    }

    // If a user has made a mistake, change a gray strike to red.
    private void colorStrike() {

        ImageView strikeOne = (ImageView) getActivity().findViewById(R.id.strike_one_image);
        ImageView strikeTwo = (ImageView) getActivity().findViewById(R.id.strike_two_image);
        ImageView strikeThree = (ImageView) getActivity().findViewById(R.id.strike_three_image);

        if (strikeCounter == 1) {
            strikeOne.setImageResource(R.drawable.strike_on);
        }

        if (strikeCounter == 2) {
            strikeTwo.setImageResource(R.drawable.strike_on);
        }

        if (strikeCounter == 3) {
            strikeThree.setImageResource(R.drawable.strike_on);
        }
    }

    // Set the current level failed and score of a user.
    private void setSharedPrefsScore() {

        Context context = getActivity();
        prefs = context.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(getString(R.string.score_points), scoreCounter.toString());
        editor.putString(getString(R.string.questions_answered), questionCounter.toString());
        editor.apply();

    }

    // Set all the items in the layout to visible.
    private void setViewVisible() {
        loadingCountDown = null;

        Button bt1 = (Button) getActivity().findViewById(R.id.option_one);
        Button bt2 = (Button) getActivity().findViewById(R.id.option_two);
        Button bt3 = (Button) getActivity().findViewById(R.id.option_three);
        Button bt4 = (Button) getActivity().findViewById(R.id.option_four);

        ConstraintLayout questionFrame = (ConstraintLayout) getActivity().findViewById(R.id.questionFrame);

        bt1.setVisibility(View.VISIBLE);
        bt2.setVisibility(View.VISIBLE);
        bt3.setVisibility(View.VISIBLE);
        bt4.setVisibility(View.VISIBLE);
        questionFrame.setVisibility(View.VISIBLE);
        loadDialog.loadTimer.cancel();
        loadDialog.dismiss();

    }

    // Move to the finish fragment.
    private void toFinishSessionFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FinishSessionFragment finishFragment = new FinishSessionFragment();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.hide(SessionFragment.this);
        fragmentTransaction.add(android.R.id.content, finishFragment);
        fragmentTransaction.commit();
    }

    // Check if got to a specific numbers of questions and then display an ad.
    private void isPopUpAdValid() {

        // Every 15 questions display ad.
        if (questionCounter % 15 == 0) {
            popUpAd();
        }

    }

    // config and call display popUpAd.
    private void popUpAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(getActivity());
        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-2474037887194071/6684630063");

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
                PAUSED_FRAG_FLG = true;
                ANSWER_PAUSED_FLG = false;
                currentTimer.cancel();
                currentTimer = null;
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
                if (PAUSED_FRAG_FLG && !(ANSWER_PAUSED_FLG)) {
                    timerHandler(secondsLeft * 1000);
                    PAUSED_FRAG_FLG = false;
                }
            }

        });
    }

    // display pop-upAd
    private void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

}
