package com.apps.meirovichomer.triviagame.leaderboardFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.meirovichomer.triviagame.DividerItemDecoration;
import com.apps.meirovichomer.triviagame.R;
import com.apps.meirovichomer.triviagame.RetrofitTriviaInterface;
import com.apps.meirovichomer.triviagame.data;
import com.apps.meirovichomer.triviagame.recyclerAdatpers.fameAdapter;
import com.apps.meirovichomer.triviagame.retroClasses.TopSessionClass;
import com.apps.meirovichomer.triviagame.retroClasses.topSessionsClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HallOfFameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HallOfFameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HallOfFameFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // RecyclerView
    private RecyclerView recyclerView;
    private fameAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    public HallOfFameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HallOfFameFragment.
     */
    public static HallOfFameFragment newInstance(String param1, String param2) {
        HallOfFameFragment fragment = new HallOfFameFragment();
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
        return inflater.inflate(R.layout.fragment_hall_of_fame, container, false);
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
        fameSessionRetrofit();
    }

    // Get properties from user into the shared prefs.
    private void fameSessionRetrofit() {
        RetrofitTriviaInterface mApiService = this.getInterfaceService();
        data mapRequest = new data();
        final Call<TopSessionClass> mService = mApiService.getTopFive(mapRequest.getParamsData("getTopFive"));
        mService.enqueue(new Callback<TopSessionClass>() {
            @Override
            public void onResponse(@NonNull Call<TopSessionClass> call, @NonNull Response<TopSessionClass> response) {
                TopSessionClass mSessions = response.body();
                populateRecycleView(mSessions.getSessions());
            }

            @Override
            public void onFailure(@NonNull Call<TopSessionClass> call, @NonNull Throwable t) {

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

    private void populateRecycleView(ArrayList<topSessionsClass> allSessions) {

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.fame_recycler_view);
        mAdapter = new fameAdapter(allSessions);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


}
