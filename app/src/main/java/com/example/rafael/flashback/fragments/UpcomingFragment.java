package com.example.rafael.flashback.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.adapters.AlbumAdapter;
import com.example.rafael.flashback.adapters.UpcomingAdapter;

import java.util.ArrayList;

/**
 * Created by rafael on 3/14/2018.
 */

public class UpcomingFragment extends Fragment {

    public static final String ARG_OBJECT = "Upcoming";
    private UpcomingAdapter upcomingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.upcoming_fragment, container, false);
        Bundle args = getArguments();

        RecyclerView recyclerView = rootView.findViewById(R.id.upcoming_tracks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //TODO: Needs to query browse activity or something to now what tracks are upcoming
        upcomingAdapter = new UpcomingAdapter(new ArrayList<Track>(), getContext());
        recyclerView.setAdapter(upcomingAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    public UpcomingAdapter getUpcomingAdapter() {
        return upcomingAdapter;
    }
}
