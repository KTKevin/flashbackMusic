package com.example.rafael.flashback.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.PreferenceHandler;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private ArrayList<Track> tracks;
    private Context context;

    public TrackAdapter(ArrayList<Track> tracks, Context context) {
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, final int position) {
      TextView trackName = holder.itemView.findViewById(R.id.track_name);
      TextView albumName = holder.itemView.findViewById(R.id.album_name);
      trackName.setText(tracks.get(position).getName());
      albumName.setText(tracks.get(position).getAlbum());

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

      //Add listener to play song
       holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              if (context != null && !((BrowseActivity) context).isVibeMode()) {
                  ((BrowseActivity) context).playTrack(tracks.get(position), null, false);
              }
          }
       });
       /*
       holder.itemView.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               likeButton.setImageResource(R.drawable.abc_ic_ab_back_material);
           }
       });
       */
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

    // helper called in BrowseActivity's menu (sort)
    public void updateData(ArrayList<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
        notifyDataSetChanged();
    }
}
