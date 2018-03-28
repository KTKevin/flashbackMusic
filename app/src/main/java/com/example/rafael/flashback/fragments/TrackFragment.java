package com.example.rafael.flashback.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.utils.DownloadScanner;
import com.example.rafael.flashback.utils.Parser;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.adapters.TrackAdapter;

import java.util.ArrayList;

/**
 * Fragment in charge of displaying tracks.
 */

public class TrackFragment extends Fragment {
    public static final String ARG_OBJECT = "Tracks";
    TrackAdapter trackAdapter;
    /**
     * Simple on create method that inflates the tracks view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.track_fragment, container, false);
        Bundle args = getArguments();

        RecyclerView recyclerView = rootView.findViewById(R.id.track_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Parser parser = ((BrowseActivity) getActivity()).getParser();
        //DownloadScanner scanner = ((BrowseActivity) getActivity()).getScanner();

        //TODO: REMOVE PARSE RONLY RELY ON SCANNER
        ArrayList<Track> tracks = parser.getTrackList();
        //tracks.addAll(scanner.getTracks());

        trackAdapter = new TrackAdapter(tracks, getContext());
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    public TrackAdapter getTrackAdapter() {
        return trackAdapter;
    }
}
