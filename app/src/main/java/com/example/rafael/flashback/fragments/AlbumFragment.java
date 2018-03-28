package com.example.rafael.flashback.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.flashback.Album;
import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.utils.DownloadScanner;
import com.example.rafael.flashback.utils.Parser;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.adapters.AlbumAdapter;

import java.util.ArrayList;


/**
 * Fragment in charge of displaying the albums within our app
 */
public class AlbumFragment extends Fragment {

    public static final String ARG_OBJECT = "Albums";
    private AlbumAdapter albumAdapter;

    /**
     * Simple on create method that inflates the albums view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.album_fragment, container, false);
        Bundle args = getArguments();
        RecyclerView recyclerView = rootView.findViewById(R.id.album_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Parser parser = ((BrowseActivity) getActivity()).getParser();
        albumAdapter = new AlbumAdapter(parser.getAlbumList(), getContext());
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    /*
    public Album getAlbum(int pos) {
        return albums.get(pos);
    }
    */

    public AlbumAdapter getAlbumAdapter() {
        return albumAdapter;
    }
}