package com.apps.meirovichomer.triviagame;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.apps.meirovichomer.triviagame.fragments.FinishSessionFragment;
import com.apps.meirovichomer.triviagame.fragments.SessionFragment;

public class TriviaSessionActivity extends AppCompatActivity implements SessionFragment.OnFragmentInteractionListener, FinishSessionFragment.OnFragmentInteractionListener {

    // shared prefs, later on instantiate it and give it the sharedPrefs.
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_session);
        newSessionRetrofit();
        toSessionFragment();
    }

    // Set users score&questions to 0 so new session will start fresh.
    private void newSessionRetrofit() {

        prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.score_points), "0");
        editor.putString(getString(R.string.questions_answered), "0");
        editor.apply();
    }

    private void toSessionFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SessionFragment fragment = new SessionFragment();
        fragmentTransaction.add(R.id.fragmentShardSession, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }


}
