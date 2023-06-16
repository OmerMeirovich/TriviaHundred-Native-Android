package com.apps.meirovichomer.triviagame.fragments;

import android.content.Context;
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
import android.widget.EditText;

import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.RetrofitTriviaInterface;
import com.apps.meirovichomer.triviagame.data;
import com.apps.meirovichomer.triviagame.retroClasses.createUserClass;

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadingScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoadingScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingScreenFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText userName;

    // shared prefs
    private SharedPreferences sharedPref;
    // Database key to do queries!.
    protected final String databaseKey = "aZ@9jhs68";

    private OnFragmentInteractionListener mListener;

    public LoadingScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadingScreenFragment.
     */
    public static LoadingScreenFragment newInstance(String param1, String param2) {
        LoadingScreenFragment fragment = new LoadingScreenFragment();
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
        return inflater.inflate(R.layout.fragment_loading_screen, container, false);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onConfirmClick();
    }

    // Confirm name clickEvent.
    private void onConfirmClick() {

        Button confirmSignUp = (Button) getActivity().findViewById(R.id.confirmSignUp_btn);
        userName = (EditText) getActivity().findViewById(R.id.signUpName);

        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (userName.getText().length() > 1) {

                    Context context = getActivity();
                    sharedPref = context.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.user_name), userName.getText().toString());
                    editor.apply();
                    newUserRetrofit();
                }
            }
        });
    }

    // Set UUID (User Unique ID) in sharedPrefs and return it.
    private String setUUID() {

        String uuid = UUID.randomUUID().toString();
        Context context = getActivity();
        sharedPref = context.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.user_uuid), uuid);
        editor.apply();

        return uuid;
    }

    // Get properties from user into the shared prefs.
    private void newUserRetrofit() {
        RetrofitTriviaInterface mApiService = this.getInterfaceService();
        data requestMap = new data();
        requestMap.putParam("uuid", setUUID());
        requestMap.putParam("name", userName.getText().toString());
        Call<createUserClass> mService = mApiService.createUser(requestMap.getParamsData("createUser"));
        mService.enqueue(new Callback<createUserClass>() {
            @Override
            public void onResponse(@NonNull Call<createUserClass> call, @NonNull Response<createUserClass> response) {
                createUserClass mUserProperties = response.body();
                // Ternary operator to prevent nullPointException.
                String userId = mUserProperties != null ? mUserProperties.getRandomId() : "0";
                // status 1 = success , status 0 = failure;
                //Ternary operator to prevent nullPointException.
                String status = mUserProperties != null ? mUserProperties.getStatus() : "0";
                Log.e("ASD", userId);
                Log.e("DSA", status);
                if (Objects.equals(status, "1") && !Objects.equals(userId, "0")) {

                    // User created.
                    Context context = getActivity();
                    sharedPref = context.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.prefs_user_id), userId);
                    editor.apply();
                    // Switch to the main menu fragment.
                    toMainMenuFragment();

                } else {

                    // User was not created.
                    Log.e("ERR", "USER ID NOT IN PROPRTIES TABLE.");
                }
            }

            @Override
            public void onFailure(Call<createUserClass> call, Throwable t) {

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

    private void toMainMenuFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MainMenuFragment frgmentMenu = new MainMenuFragment();
        getFragmentManager().beginTransaction().detach(frgmentMenu).attach(frgmentMenu);
        fragmentTransaction.hide(LoadingScreenFragment.this);
        fragmentTransaction.addToBackStack("xyz");
        fragmentTransaction.add(android.R.id.content, frgmentMenu);
        fragmentTransaction.commit();

    }


}
