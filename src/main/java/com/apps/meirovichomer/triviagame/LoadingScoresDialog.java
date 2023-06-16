package com.apps.meirovichomer.triviagame;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;

/**
 * Created by meiro on 11/5/2017.
 */

public class LoadingScoresDialog extends Dialog implements android.view.View.OnClickListener  {


    public Activity c;
    public Dialog d;
    public CountDownTimer loadScoreTimer;
    public ConstraintLayout dialogLayout;

    public LoadingScoresDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_game_dialog);
        loadAnimation();
    }

    private void loadAnimation() {

        dialogLayout = (ConstraintLayout) findViewById(R.id.loading_dialog_layout);
        loadScoreTimer = new CountDownTimer(6000, 500) {
            int count = 0;

            @Override
            public void onTick(long l) {

                if (count == 4) {
                    count = 0;
                } else if (count == 0) {
                    dialogLayout.setBackgroundResource(R.drawable.loading_score_zero);
                } else if (count == 1) {
                    dialogLayout.setBackgroundResource(R.drawable.loading_score_one);
                } else if (count == 2) {
                    dialogLayout.setBackgroundResource(R.drawable.loading_score_two);
                } else if (count == 3) {
                    dialogLayout.setBackgroundResource(R.drawable.loading_score_three);
                }
                count++;
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
