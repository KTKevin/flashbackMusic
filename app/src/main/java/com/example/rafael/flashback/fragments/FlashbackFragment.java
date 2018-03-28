package com.example.rafael.flashback.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.adapters.FlashbackAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rafael on 2/17/2018.
 */

public class FlashbackFragment extends Fragment {
    RecyclerView recyclerView;
    FlashbackAdapter flashbackAdapter;
    RelativeLayout startFlashbackLayout;
    BrowseActivity activity;
    ArrayList<Track> tracks;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flashback_fragment, container, false);
        Bundle args = getArguments();
        recyclerView = rootView.findViewById(R.id.playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        startFlashbackLayout = rootView.findViewById(R.id.start_flashback_layout);
        //TODO: Make call to flashback algo to retrieve list of tracks
        flashbackAdapter = new FlashbackAdapter(new ArrayList<Track>(), getContext(), this);
        recyclerView.setAdapter(flashbackAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        activity = (BrowseActivity) getActivity();
        startFlashbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setFlashback(true);
                //TODO call to flashback alog to retrieve list of tracks
                updateTracks();
                flashbackAdapter.setTracks(tracks);
                flashbackAdapter.notifyDataSetChanged();

                //TODO: start playing music
                startFlashbackLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    public void updateTracks() {
        /*
        ArrayList<Track> newTracks = new ArrayList<>(Arrays.asList(activity.determinePlaylist()));
        if (!newTracks.equals(tracks)) {
            //TODO: Hacky fix need to add dummy track in front so that it isnt lost to the stop button
            newTracks.add(0, new Track("", 0, "", Uri.EMPTY, "", new byte[0]));
            tracks = newTracks;
            flashbackAdapter.setTracks(tracks);
            flashbackAdapter.notifyDataSetChanged();
            activity.playTrack(tracks.get(1), tracks, true);
        } else if (tracks.size() != 2){
            tracks.remove(0);
            activity.playTrack(tracks.get(1), tracks, true);
        } else {
            activity.resetPlayer();
        }
        */
    }

    public void resetFlashbackUI() {
        activity.setFlashback(false);
        activity.resetPlayer();
        activity.resetPlayerUI();
        recyclerView.setVisibility(View.GONE);
        startFlashbackLayout.setVisibility(View.VISIBLE);
    }
}
