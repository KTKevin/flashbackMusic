package com.example.rafael.flashback.handlers;

import android.util.Log;

import com.example.rafael.flashback.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mukmai on 3/11/2018.
 */

public class FilterHandler {

    public FilterHandler() {}

    public static Comparator<Track> albumComparator = new Comparator<Track>() {
        @Override
        public int compare(Track o1, Track o2) {
            String album1 = o1.getAlbum();
            String album2 = o2.getAlbum();
            return album1.compareTo(album2);
        }
    };

    public static Comparator<Track> titleComparator = new Comparator<Track>() {
        @Override
        public int compare(Track o1, Track o2) {
            String title1 = o1.getName();
            String title2 = o2.getName();
            return title1.compareTo(title2);
        }
    };

    public static Comparator<Track> artistComparator = new Comparator<Track>() {
        @Override
        public int compare(Track o1, Track o2) {
            String artist1 = o1.getArtist();
            String artist2 = o2.getArtist();
            return artist1.compareTo(artist2);
        }
    };

    public static Comparator<Track> favoriteComparator = new Comparator<Track>() {
        @Override
        public int compare(Track o1, Track o2) {
            int favor1 = o1.getPreference();
            int favor2 = o2.getPreference();
            return favor2 - favor1;
        }
    };

    public ArrayList<Track> filterList(ArrayList<Track> tracks, String option) {
        ArrayList<Track> result = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            result.add(tracks.get(i));
        }
        switch (option) {
            case "album":
                Collections.sort(result, albumComparator);
                break;
            case "title":
                Collections.sort(result, titleComparator);
                break;
            case "artist":
                Collections.sort(result, artistComparator);
                break;
            case "favorite":
                Collections.sort(result, favoriteComparator);
                break;
            default:
                Log.d("FILTER TRACKS", "Unknown filter option: " + option);
        }
        return result;
    }
}
