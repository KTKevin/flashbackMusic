package com.example.rafael.flashback.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.fragments.FlashbackFragment;
import com.example.rafael.flashback.fragments.VibeModeFragment;
import com.example.rafael.flashback.handlers.PreferenceHandler;

import java.util.ArrayList;


/**
 * Created by konoha on 3/12/18.
 */

public class VibeModeAdapter extends RecyclerView.Adapter<VibeModeAdapter.ViewHolder> {

    private ArrayList<Track> tracks;
    private Context context;
    private VibeModeFragment fragment;

    public VibeModeAdapter(ArrayList<Track> tracks, Context context, VibeModeFragment fragment) {
        this.context = context;
        this.tracks = tracks;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        /*if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_flashback_vh, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        }*/
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /*if (position == 0) {
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
        }*/
        TextView trackName = holder.itemView.findViewById(R.id.track_name);
        TextView albumName = holder.itemView.findViewById(R.id.album_name);
        trackName.setText(tracks.get(position).getName());
        albumName.setText(tracks.get(position).getName());

        final ImageButton likeButton = holder.itemView.findViewById(R.id.track_fav_button);
        PreferenceHandler handler = new PreferenceHandler(context);
        likeButton.setImageResource(handler.adaptPreferenceToSrcId(tracks.get(position).getPreference()));
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceHandler handler = new PreferenceHandler(context);
                handler.changePreference(tracks.get(position));
                likeButton.setImageResource(handler.adaptPreferenceToSrcId(tracks.get(position).getPreference()));
            }
        });

        if(tracks.get(position).getId() == null) {
            holder.itemView.setAlpha((float) 0.8);
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
