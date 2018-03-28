package com.example.rafael.flashback.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.flashback.Album;
import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.adapters.TrackAdapter;
import com.example.rafael.flashback.fragments.AlbumFragment;


public class AlbumTracksFragment extends Fragment {
    public static final String ARG_OBJECT = "Albums";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.album_tracks_fragment, container, false);
        Bundle args = getArguments();
        RecyclerView recyclerView = rootView.findViewById(R.id.album_track_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageButton backBttn = rootView.findViewById(R.id.back_button);
        ImageButton playAlbumBttn = rootView.findViewById(R.id.play_album_bttn);
        TextView albumName = rootView.findViewById(R.id.album_title);

        //TODO: Can refactor and use browseactiviy getParser()
        final BrowseActivity activity = (BrowseActivity) getContext();
        AlbumFragment albumFragment = (AlbumFragment) activity.getCurrentFragment();
        int albumPos = args.getInt("album_num");

        final Album album = albumFragment.getAlbumAdapter().getAlbums(albumPos);

        //Set Album Name
        albumName.setText(album.getName());

        //Display Tracks in album
        TrackAdapter trackAdapter = new TrackAdapter(album.getList(), getContext());
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Add event listener to back button
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Better to have calling fragment pop its self
                ((TrackFragment)activity.adapter.frags.get(0)).trackAdapter.notifyDataSetChanged();
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        //Add event listener to play album
        playAlbumBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.isFlashbackOn())
                    activity.playTrack(album.getList().get(0), album.getList(), false);
            }
        });

        return rootView;

    }
}