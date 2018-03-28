package com.example.rafael.flashback.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.flashback.Album;
import com.example.rafael.flashback.fragments.AlbumTracksFragment;
import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>  {

    private ArrayList<Album> albums;
    private Context context;

    public AlbumAdapter(ArrayList<Album> albums, Context context) {
        this.albums = albums;
        this.context = context;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView albumName = holder.itemView.findViewById(R.id.album_name);
        TextView numOfTracks = holder.itemView.findViewById(R.id.num_of_tracks);

        albumName.setText(albums.get(position).getName());
        numOfTracks.setText(Integer.toString(albums.get(position).getNumberOfTracks()));

        final ImageButton albumPlayButton = holder.itemView.findViewById(R.id.album_play_button);
        albumPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context != null && !((BrowseActivity) context).isVibeMode()) {
                    ((BrowseActivity) context).playTrack(albums.get(position).getList().get(0), albums.get(position).getList(), false);
                }
            }
        });

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (context != null) {
                ((BrowseActivity) context).playTrack(albums.get(position).getList().get(0), albums.get(position).getList());
            }

            // Create a an Album Tracks Fragment and send over the album that was selected
            AlbumTracksFragment tracksFragment = new AlbumTracksFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("album_num", position);
            tracksFragment.setArguments(bundle);

            FragmentManager fm = ((BrowseActivity) context).getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.browse, tracksFragment).addToBackStack("album_frag").commit();
            notifyDataSetChanged();
            }
        });
        */
    }
    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public Album getAlbums(int index) {
        return albums.get(index);
    }
}
