package com.apps.meirovichomer.triviagame.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.RetrofitTriviaInterface;
import com.apps.meirovichomer.triviagame.TriviaSessionActivity;
import com.apps.meirovichomer.triviagame.data;
import com.apps.meirovichomer.triviagame.retroClasses.createSessionClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FinishSessionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Shared prefs value will be held in those variables-
    private String userId;
    private String uuid;
    private String score;
    private String levelId;

    //adMob view
    private AdView mAdView;

    private boolean AD_ON_FLAG = true;


    //Pop-up adMob
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;


    // shared prefs, later on instantiate it and give it the sharedPrefs.
    private SharedPreferences prefs;

    private OnFragmentInteractionListener mListener;

    public FinishSessionFragment() {
        // Required empty public constructor
    }

    public static FinishSessionFragment newInstance(String param1, String param2) {
        FinishSessionFragment fragment = new FinishSessionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_session, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTextResults();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        popUpAd();
        onButtonsClick();
        adMobConfig();
        createSessionRetrofit();
    }

    //Create a session call and insert the score and level of the session.
    private void createSessionRetrofit() {
        RetrofitTriviaInterface mApiService = this.getInterfaceService();
        // Get shared prefs
        Context context = getActivity();
        prefs = context.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        userId = prefs.getString(getResources().getString(R.string.prefs_user_id), null);
        uuid = prefs.getString(getResources().getString(R.string.user_uuid), null);
        score = prefs.getString(getResources().getString(R.string.score_points), null);
        levelId = prefs.getString(getResources().getString(R.string.questions_answered), null);

        data mapRequest = new data();

        mapRequest.putParam("id", userId);
        mapRequest.putParam("uuid", uuid);
        mapRequest.putParam("score", score);
        mapRequest.putParam("level", levelId);

        Call<createSessionClass> mService = mApiService.createSession(mapRequest.getParamsData("createSession"));
        mService.enqueue(new Callback<createSessionClass>() {
            @Override
            public void onResponse(@NonNull Call<createSessionClass> call, @NonNull Response<createSessionClass> response) {
                createSessionClass responseArray = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<createSessionClass> call, @NonNull Throwable t) {

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

    // Set the score and level of the usr.
    private void setTextResults() {

        TextView scoreView = (TextView) getActivity().findViewById(R.id.score_textView);
        TextView questions = (TextView) getActivity().findViewById(R.id.questions_textView);

        Context context = getActivity();
        prefs = context.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        score = prefs.getString(getResources().getString(R.string.score_points), "0");
        levelId = prefs.getString(getResources().getString(R.string.questions_answered), "0");

        scoreView.setText(score);
        questions.setText(levelId);
    }

    // Config adMob banner.
    private void adMobConfig() {

        mAdView = (AdView) getActivity().findViewById(R.id.finishSession_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void onButtonsClick() {

        Button confirmBtn = (Button) getActivity().findViewById(R.id.confirmFinish_btn);
        Button playAgainbtn = (Button) getActivity().findViewById(R.id.playAgain_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!AD_ON_FLAG) {
                    fragmentSwitch(1);
                }
            }
        });

        playAgainbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!AD_ON_FLAG) {
                    fragmentSwitch(2);
                }
            }
        });

    }

    // Move to the finish fragment.
    private void fragmentSwitch(Integer action) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (action) {

            // to main menu
            case 1:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.hide(FinishSessionFragment.this);
                fragmentTransaction.commit();
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                break;
            // to session activity
            case 2:
                fragmentTransaction.hide(FinishSessionFragment.this);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                Intent intent = new Intent(getActivity(), TriviaSessionActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent, options.toBundle());
                break;
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
            AD_ON_FLAG = false;
        }
    }


}
