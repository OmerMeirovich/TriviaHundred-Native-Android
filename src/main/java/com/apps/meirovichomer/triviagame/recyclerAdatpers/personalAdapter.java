package com.apps.meirovichomer.triviagame.recyclerAdatpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.retroClasses.sessionInfoClass;

import java.util.ArrayList;


public class personalAdapter extends RecyclerView.Adapter<personalAdapter.MyViewHolder> {


    private ArrayList<sessionInfoClass> allSessions;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView score, level, date;

        public MyViewHolder(View view) {
            super(view);
            score = (TextView) view.findViewById(R.id.score_row);
            level = (TextView) view.findViewById(R.id.level_row);
            date = (TextView) view.findViewById(R.id.date_row);
        }
    }


    public personalAdapter(ArrayList<sessionInfoClass> allSessions) {
        this.allSessions = allSessions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personal_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        sessionInfoClass sessionHolder = allSessions.get(position);
        holder.score.setText(sessionHolder.getScore());
        holder.level.setText(sessionHolder.getLevel());
        holder.date.setText(sessionHolder.getSessionTime());
    }

    @Override
    public int getItemCount() {
        return allSessions.size();
    }
}
