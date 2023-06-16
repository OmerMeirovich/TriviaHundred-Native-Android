package com.apps.meirovichomer.triviagame.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apps.meirovichomer.triviagame.LeaderboardActivity;
import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.SuggestQuestionActivity;
import com.apps.meirovichomer.triviagame.TriviaSessionActivity;
import com.apps.meirovichomer.triviagame.UserConfigActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // adView for adMob!
    private AdView mAdView;

    private OnFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
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
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adMobConfig();
        clickEvents();

    }

    // All the buttons click events.
    private void clickEvents() {
        toTriviaSession();
        toLeaderboard();
        toUserConfig();
        toSuggestQuestion();
    }

    // Handle onClick TriviaSessionActivity button.
    private void toTriviaSession() {

        Button toSession = (Button) getActivity().findViewById(R.id.toTriviaSession);
        toSession.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TriviaSessionActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent, options.toBundle());
            }
        });
    }

    // Handle onClick TriviaSessionActivity button.
    private void toLeaderboard() {

        Button toLeaderboards = (Button) getActivity().findViewById(R.id.toLeaderboard);
        toLeaderboards.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent, options.toBundle());
            }
        });
    }

    private void toUserConfig() {

        Button toConfig = (Button) getActivity().findViewById(R.id.toPlayerConfig_btn);

        toConfig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserConfigActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent, options.toBundle());

            }
        });

    }

    private void toSuggestQuestion() {

        Button toSuggest = (Button) getActivity().findViewById(R.id.toSuggestQuestion);

        toSuggest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuggestQuestionActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter_from_right, R.anim.exit_to_left);
                startActivity(intent, options.toBundle());

            }
        });

    }

    private void adMobConfig() {

        mAdView = (AdView) getActivity().findViewById(R.id.mainMenu_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}
