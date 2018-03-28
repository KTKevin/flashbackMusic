package com.example.rafael.flashback;

import android.net.Uri;
import android.util.Log;

import com.example.rafael.flashback.utils.NameGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by mukmai on 2/4/2018.
 */

public class Track  {
    private final String NA = "N/A";

    private String name; // track name
    private long runtime; // runtime in millisecond
    private String runtimeDisplay; // runtime in mm:ss string format
    private String artist; // artist name
    private Uri id; // URI of the track to access the track
    private String album; // album name
    private byte[] albumCover; // album cover image
    private String lastPlayed; // last played date and time in yyyyMMddHHmmss format
    private int preference; // user preference of this track (0: neutral, 1: favourite, -1: disike)
    private double lastLat; // latitude of last played location
    private double lastLon; // longitude of last played location
    private String locationName; // full address of last played location
    private String Person;
    private Date lastPlayedDate; // last played date in Date object
    private String dbId; // hashed version of URI
    private int score; // represents the score this track recieved for vibe mode
    private String lastPlayedUser;

    public Track(String name, long runtime, String artist, Uri id, String album, byte[] albumCover) {
        this.name = name;
        if (runtime >= 0) {
            this.runtime = runtime;
        } else {
            this.runtime = 0;
        }
        this.runtimeDisplay = calcRunTime();
        this.artist = artist;
        this.id = id;
        this.album = album;
        this.albumCover = albumCover;
        this.lastPlayed = "";
        this.preference = 0;
        this.lastLat = 0;
        this.lastLon = 0;
        this.dbId = Integer.toString((name + artist + album).hashCode());
    }

    public Track() {

    }

    public String calcRunTime() {
        long rtCalc = getRuntime()/1000;
        long min = rtCalc/60;
        long sec = rtCalc%60;
        long secTen = sec/10;
        long secOne = sec%10;
        return Long.toString(min) + ":" + Long.toString(secTen) + Long.toString(secOne);
    }

    public byte[] getAlbumcover() { return albumCover; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Uri getId() {
        return id;
    }

    public void setId(Uri id) {
        this.id = id;
    }

    public String getAlbum() { return album; }

    public void setAlbum(String album) { this.album = album; }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public double getLastLat() {
        return lastLat;
    }

    public void setLastLat(double lastLat) {
        this.lastLat = lastLat;
    }

    public double getLastLon() {
        return lastLon;
    }

    public void setLastLon(double lastLon) {
        this.lastLon = lastLon;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preferred) {
        this.preference = preferred;
    }

    public String getLocationName() {
        if (locationName == null || locationName.equals("")) {
            return NA;
        }
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPerson() {
        if (Person == null || Person.equals("")) {
            return NA;
        }
        return Person;
        //NameGenerator NG = new NameGenerator();
        //return NG.proxy("Chen");
    }

    public void setPerson(String Person) {
        this.Person = Person;
    }


    public void setLastPlayedDate(Date date) {
        lastPlayedDate = date;
    }

    public Date getLastPlayedDate()
    {
        return lastPlayedDate;
    }

    public String getDbId()
    {
        return dbId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore()
    {
        return score;
    }
    public void setLastPlayedUser(String userEmail) {
        this.lastPlayedUser = userEmail;
    }

    public String getLastPlayedUser() {
        return lastPlayedUser;
    }
}
