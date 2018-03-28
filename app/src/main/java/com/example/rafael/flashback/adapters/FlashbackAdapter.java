package com.example.rafael.flashback.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.fragments.FlashbackFragment;

import java.util.ArrayList;

/**
 * Created by rafael on 2/17/2018.
 */

public class FlashbackAdapter extends RecyclerView.Adapter<FlashbackAdapter.ViewHolder> {
    private ArrayList<Track> tracks;
    private Context context;
    private FlashbackFragment fragment;

    public FlashbackAdapter(ArrayList<Track> tracks, Context context, FlashbackFragment fragment) {
        this.context = context;
        this.tracks = tracks;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_flashback_vh, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 0) {
            Button stopFlashbackBttn = holder.itemView.findViewById(R.id.stop_flashback_bttn);
            stopFlashbackBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.resetFlashbackUI();
                }
            });
        } else {
            TextView trackName = holder.itemView.findViewById(R.id.track_name);
            TextView albumName = holder.itemView.findViewById(R.id.album_name);
            trackName.setText(tracks.get(position).getName());
            albumName.setText(tracks.get(position).getName());
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }
}
