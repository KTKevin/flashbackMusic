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
import android.widget.Toast;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.adapters.FlashbackAdapter;
import com.example.rafael.flashback.adapters.VibeModeAdapter;
import com.example.rafael.flashback.handlers.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by konoha on 3/12/18.
 */

public class VibeModeFragment extends Fragment {

    RecyclerView recyclerView;
    VibeModeAdapter vibeModeAdapter;
    //RelativeLayout startFlashbackLayout;
    BrowseActivity activity;
    ArrayList<Track> tracks;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vibemode_fragment, container, false);
        Bundle args = getArguments();

        recyclerView = rootView.findViewById(R.id.playlist_vibemode);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //startFlashbackLayout = rootView.findViewById(R.id.start_flashback_layout);
        //TODO: Make call to flashback algo to retrieve list of tracks
        vibeModeAdapter = new VibeModeAdapter(new ArrayList<Track>(), getContext(), this);
        recyclerView.setAdapter(vibeModeAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        activity = (BrowseActivity) getActivity();
        DatabaseHandler dbHandler = activity.getDbHandler();
        dbHandler.determinePlaylist(activity.getVibeModeHandler(), this);

        return rootView;
    }

    public void updateTracks(Track[] tracks) {
        ArrayList<Track> newTracks = new ArrayList<>(Arrays.asList(tracks));
        this.tracks = newTracks;
        vibeModeAdapter.setTracks(this.tracks);
        vibeModeAdapter.notifyDataSetChanged();
        if (this.tracks.size() != 0) {
            activity.playTrack(this.tracks.get(0), this.tracks, true);
            Toast.makeText(getContext(), "generate playlist in vibe mode", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "empty playlist in vibe mode", Toast.LENGTH_SHORT).show();
        }
    }
}
