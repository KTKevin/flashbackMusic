package com.example.rafael.flashback.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.PreferenceHandler;

import java.util.ArrayList;

/**
 * Created by rafael on 3/14/2018.
 */

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {

    private ArrayList<Track> tracks;
    private Context context;

    public UpcomingAdapter(ArrayList<Track> tracks, Context context) {
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public UpcomingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UpcomingAdapter.ViewHolder holder, int position) {

        TextView trackName = holder.itemView.findViewById(R.id.track_name);
        TextView albumName = holder.itemView.findViewById(R.id.album_name);
        trackName.setText(tracks.get(position).getName());
        albumName.setText(tracks.get(position).getAlbum());

        final ImageButton likeButton = holder.itemView.findViewById(R.id.track_fav_button);
        PreferenceHandler handler = new PreferenceHandler(context);
        likeButton.setImageResource(handler.adaptPreferenceToSrcId(tracks.get(position).getPreference()));
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

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }
}