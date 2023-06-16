package com.apps.meirovichomer.triviagame.recyclerAdatpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.retroClasses.topSessionsClass;

import java.util.ArrayList;

/**
 * Created by meiro on 10/18/2017.
 */

public class lastDayAdapter extends RecyclerView.Adapter<lastDayAdapter.MyViewHolder> {
    private ArrayList<topSessionsClass> allSessions;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView score, player_name;
        public ImageView rankImage;

        public MyViewHolder(View view) {
            super(view);
            score = (TextView) view.findViewById(R.id.score_last_day_row);
            player_name = (TextView) view.findViewById(R.id.name_last_day_row);
            rankImage = (ImageView)view.findViewById(R.id.rank_last_day_image);
        }
    }


    public lastDayAdapter(ArrayList<topSessionsClass> allSessions) {
        this.allSessions = allSessions;
    }

    @Override
    public lastDayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_day_list_row, parent, false);

        return new lastDayAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(lastDayAdapter.MyViewHolder holder, int position) {
        topSessionsClass sessionHolder = allSessions.get(position);
        holder.score.setText("ניקוד: " + sessionHolder.getScore());
        holder.player_name.setText(sessionHolder.getPlayer_name());
        holder.rankImage.setImageResource(getImageId(position));
    }

    @Override
    public int getItemCount() {
        return allSessions.size();
    }

    private int getImageId(Integer position) {

        if (position == 0) {
            return R.drawable.trophy_one;
        }
        if (position == 1) {
            return R.drawable.trophy_two;
        }
        if (position == 2) {
            return R.drawable.trophy_three;
        }
        if (position == 3) {
            return R.drawable.medal_four;
        }
        if (position == 4) {
            return R.drawable.medal_five;
        }
        if (position == 5) {
            return R.drawable.medal_six;
        }
        if (position == 6) {
            return R.drawable.medal_seven;
        }
        if (position == 7) {
            return R.drawable.medal_eight;
        }
        if (position == 8) {
            return R.drawable.medal_nine;
        }
        if (position == 9) {
            return R.drawable.medal_ten;
        } else {
            return R.drawable.medal_tag;
        }
    }

}
