package com.apps.meirovichomer.triviagame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.apps.meirovichomer.triviagame.fragments.LoadingScreenFragment;
import com.apps.meirovichomer.triviagame.fragments.MainMenuFragment;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);


        // Get shared prefs
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        String userId = prefs.getString(getResources().getString(R.string.prefs_user_id), null);
        String UUID = prefs.getString(getString(R.string.user_uuid), null);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // Check if user already exists.
        if (userId == null || UUID == null) {

            //If null go into LOADING SCREEN FRAGMENT
            LoadingScreenFragment fragment = new LoadingScreenFragment();
            fragmentTransaction.add(R.id.fragmentShardMain, fragment);
            fragmentTransaction.commit();


        } else {

            // ELSE go to MAINMENU FRAGMENT
            MainMenuFragment fragment = new MainMenuFragment();
            fragmentTransaction.add(R.id.fragmentShardMain, fragment);
            fragmentTransaction.commit();

        }

    }


}
