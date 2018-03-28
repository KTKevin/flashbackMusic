package com.example.rafael.flashback;

import java.util.ArrayList;

/**
 * Created by mukmai on 2/4/2018.
 */

public class Album {
    private ArrayList<Track> list; // list of track in this album
    private byte[] albumCover; // album cover image
    private String name; // album name
    private long runtime; // album total runtime
    private String runtimeDisplay; // album total runtime in hh:mm:ss string format

    public Album(String n, Track t, byte[] alCov) {
        this.name = n;
        this.runtime = 0;
        this.runtimeDisplay = "";
        this.list = new ArrayList<Track>();
        this.albumCover = alCov;
        addTrack(t);
    }

    public String calcRunTime() {
        long rtCalc = getRuntime()/1000;
        long hr = rtCalc/60/60;
        long min = rtCalc/60%60;
        long minTen = min/10;
        long minOne = min%10;
        long sec = rtCalc%60;
        long secTen = sec/10;
        long secOne = sec%10;
        return Long.toString(hr) + ":" + Long.toString(minTen) + Long.toString(minOne) + ":" + Long.toString(secTen) + Long.toString(secOne);
    }

    public void addTrack(Track t) {
        this.list.add(t);
        this.runtime += t.getRuntime();
        this.runtimeDisplay = calcRunTime();
    }

    public byte[] getAlbumcover() {
        return albumCover;
    }

    public ArrayList<Track> getList() {
        return list;
    }

    public void setList(ArrayList<Track> list) {
        this.list = list;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public String getRuntimeDisplay() {
        return runtimeDisplay;
    }

    public void setRuntimeDisplay(String runtimeDisplay) {
        this.runtimeDisplay = runtimeDisplay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfTracks() {
        return list.size();
    }
}
