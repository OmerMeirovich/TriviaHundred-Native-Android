package com.apps.meirovichomer.triviagame;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apps.meirovichomer.triviagame.leaderboardFragments.HallOfFameFragment;
import com.apps.meirovichomer.triviagame.leaderboardFragments.LastDayFragment;
import com.apps.meirovichomer.triviagame.leaderboardFragments.LastWeekFragment;
import com.apps.meirovichomer.triviagame.leaderboardFragments.PersonalRecordFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class LeaderboardActivity extends AppCompatActivity implements PersonalRecordFragment.OnFragmentInteractionListener, HallOfFameFragment.OnFragmentInteractionListener, LastDayFragment.OnFragmentInteractionListener, LastWeekFragment.OnFragmentInteractionListener {

    ProgressBar loadlist;

    //adMob view
    private AdView mAdView;

    // Loading timer
    private CountDownTimer loadingCountDown;

    private LoadingScoresDialog loadScoreDialog;


    // Handle onOptionMenuItem click
    private CountDownTimer clickedTimer;


    //Pop-up adMob
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    private boolean CLICKED_FLG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        loadPersonalFragment();
        adMobConfig();
        // Show the loading panda dialog.
        loadScoreDialog = new LoadingScoresDialog(this);
        loadScoreDialog.setCancelable(false);
        loadScoreDialog.show();
        loadingTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leaderboard_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PersonalRecordFragment fragmentPersonal = new PersonalRecordFragment();
        HallOfFameFragment fragmentFame = new HallOfFameFragment();
        LastDayFragment lastDay = new LastDayFragment();
        LastWeekFragment lastWeek = new LastWeekFragment();

        TextView infoTxt = (TextView) findViewById(R.id.listInfo_txt);

        switch (item.getItemId()) {
            case R.id.action_personal:
                if (!CLICKED_FLG) {
                    CLICKED_FLG = true;
                    fragmentTransaction.replace(R.id.fragmentLeaderboards, fragmentPersonal);
                    fragmentTransaction.commit();
                    clickedCountdown();
                    infoTxt.setText("התוצאות הטובות ביותר שלך");
                }
                return true;

            case R.id.action_all_time:
                if (!CLICKED_FLG) {
                    CLICKED_FLG = true;
                    fragmentTransaction.replace(R.id.fragmentLeaderboards, fragmentFame);
                    fragmentTransaction.commit();
                    clickedCountdown();
                    infoTxt.setText("התוצאות הטובות ביותר בכל הזמנים");
                }
                return true;

            case R.id.action_one_day:
                if (!CLICKED_FLG) {
                    CLICKED_FLG = true;
                    fragmentTransaction.replace(R.id.fragmentLeaderboards, lastDay);
                    fragmentTransaction.commit();
                    clickedCountdown();
                    infoTxt.setText("התוצאות הטובות ביותר ביממה האחרונה");
                }
                return true;

            case R.id.action_one_week:
                if (!CLICKED_FLG) {
                    CLICKED_FLG = true;
                    fragmentTransaction.replace(R.id.fragmentLeaderboards, lastWeek);
                    fragmentTransaction.commit();
                    clickedCountdown();
                    infoTxt.setText("התוצאות הטובות ביותר בשבוע האחרון");
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setToolbar() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.leaderboard_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    private void adMobConfig() {

        mAdView = (AdView) findViewById(R.id.mainMenu_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    // config and call display popUpAd.
    private void popUpAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(LeaderboardActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-2474037887194071/6684630063");

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
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

    private void loadPersonalFragment() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //If null go into LOADING SCREEN FRAGMENT
        PersonalRecordFragment fragment = new PersonalRecordFragment();
        fragmentTransaction.add(R.id.fragmentLeaderboards, fragment);
        fragmentTransaction.commit();

        setToolbar();
    }

    // Timer prevents double-click crush.
    private void clickedCountdown() {

        loadlist = (ProgressBar) findViewById(R.id.loadBoard_progress);
        loadlist.setVisibility(View.VISIBLE);

        clickedTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                CLICKED_FLG = false;
                loadlist.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CLICKED_FLG) {
            clickedTimer.cancel();
        }
    }

    // The panda loading timer, Only start playing when loading is finished.
    private void loadingTimer() {

        loadingCountDown = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                loadScoreDialog.dismiss();
                popUpAd();
            }
        }.start();
    }
}
