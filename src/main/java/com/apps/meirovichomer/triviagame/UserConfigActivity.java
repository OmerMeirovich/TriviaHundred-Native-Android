package com.apps.meirovichomer.triviagame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.meirovichomer.triviagame.retroClasses.setNewNameClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserConfigActivity extends AppCompatActivity {


    // shared prefs, later on instantiate it and give it the sharedPrefs.
    private SharedPreferences prefs;

    private EditText configName;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);
        showEditText();
        onConfigClick();
        adMobConfig();
    }

    private void showEditText() {

        prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        configName = (EditText) findViewById(R.id.configPlayerName_edit);

        String playerName = prefs.getString(getString(R.string.user_name), null);

        configName.setText(playerName);
    }

    private void onConfigClick() {

        configName = (EditText) findViewById(R.id.configPlayerName_edit);
        prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        Button configConfirm = (Button) findViewById(R.id.confirmConfig_btn);
        configConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateEditText()) {
                    setNewNameRetrofit();
                    editor.putString(getString(R.string.user_name), configName.getText().toString());
                    editor.commit();
                    Toast.makeText(UserConfigActivity.this, "שמך נשמר בהצלחה!", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    private void setNewNameRetrofit() {
        RetrofitTriviaInterface mApiService = this.getInterfaceService();
        prefs = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        String userId = prefs.getString(getResources().getString(R.string.prefs_user_id), null);
        String uuid = prefs.getString(getString(R.string.user_uuid), null);
        data mapRequest = new data();
        mapRequest.putParam("id", userId);
        mapRequest.putParam("uuid", uuid);
        mapRequest.putParam("name", configName.getText().toString());
        final Call<setNewNameClass> mService = mApiService.setNewName(mapRequest.getParamsData("setNewName"));
        mService.enqueue(new Callback<setNewNameClass>() {
            @Override
            public void onResponse(@NonNull Call<setNewNameClass> call, @NonNull Response<setNewNameClass> response) {
                setNewNameClass mSessions = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<setNewNameClass> call, @NonNull Throwable t) {

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


    private boolean validateEditText() {

        if (configName.getText().length() < 1) {
            return false;
        } else if (configName.getText().length() > 30) {
            Toast.makeText(this, "שם לא יכול להיות ארוך יותר מ-30 תווים!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void adMobConfig() {

        mAdView = (AdView) findViewById(R.id.config_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


}
