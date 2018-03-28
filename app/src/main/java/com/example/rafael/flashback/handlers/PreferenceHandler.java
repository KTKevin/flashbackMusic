package com.example.rafael.flashback.handlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;

/**
 * Created by mukmai on 2/14/2018.
 */

public class  PreferenceHandler {

    Context context;

    public PreferenceHandler(Context context) {
        this.context = context;
    }

    public void changePreference(Track track) {
        int new_pref = track.getPreference();
        switch(track.getPreference()) {
            case 0: new_pref = 1; break;
            case 1: new_pref = -1; break;
            case -1: new_pref = 0; break;
        }
        track.setPreference(new_pref);
        SharedPreferences sharedPreferences = context.getSharedPreferences(track.getName(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("preference", new_pref);
        editor.apply();
    }

    public int adaptPreferenceToSrcId(int score) {
        int Src = 0;
        switch(score) {
            case 0: Src = R.drawable.ic_add_black_24dp; break;
            case 1: Src = R.drawable.ic_done_black_24dp; break;
            case -1: Src = R.drawable.ic_clear_black_24dp; break;
        }
        return Src;
    }

    public void loadPreference(Track track) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(track.getName(), context.MODE_PRIVATE);
        track.setPreference(sharedPreferences.getInt("preference",0));
    }
}
