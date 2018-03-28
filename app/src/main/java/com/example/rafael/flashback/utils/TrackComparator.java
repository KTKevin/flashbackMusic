package com.example.rafael.flashback.utils;

/**
 * Created by rafael on 3/4/2018.
 */

import com.example.rafael.flashback.Track;

import java.util.Comparator;

public class TrackComparator implements Comparator<Track> {

    @Override
    public int compare(Track track1, Track track2) {
        if (track1.getScore() > track2.getScore()) {
            return 1;
        } else if (track1.getScore() < track2.getScore()) {
            return -1;
        }

        return 0;
    }
}
