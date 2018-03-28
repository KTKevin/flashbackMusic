package com.example.rafael.flashback.handlers;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.adapters.PeopleAdapter;
import com.example.rafael.flashback.handlers.DatabaseHandler;
import com.example.rafael.flashback.utils.DownloadScanner;
import com.example.rafael.flashback.utils.GPSTracker;
import com.example.rafael.flashback.utils.Parser;
import com.example.rafael.flashback.utils.TrackComparator;
import com.google.api.services.people.v1.People;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

import static com.example.rafael.flashback.utils.CalendarMock.getMockTime;
import static com.example.rafael.flashback.utils.CalendarMock.fakeDate;

/**
 * Created by rafael on 3/4/2018.
 */

public class VibeModeHandler {
    private DatabaseHandler handler;
    private BrowseActivity activity;
    private PeopleAdapter user;
    private Parser parser;

    public VibeModeHandler(BrowseActivity activity, PeopleAdapter user) {
        this.activity = activity;
        this.user = user;
        handler = activity.getDbHandler();
        parser = activity.getParser();
    }

    public Track[] determinePlaylist(ArrayList<DataSnapshot> data) {
        //First we call our DB to get what tracks exist for all users
        Log.d("vibe", "vibe "+ Integer.toString(data.size()));
        //We call our scanner to get the tracks the user currently has downloaded
        Map<String, Track> idToTrackMap = parser.getIdToTracksMap(activity);
        ArrayList<Track> totalTracks = new ArrayList<>();
        //We iterate over each track in our db and initiate downloads for them
        for (DataSnapshot trackData : data) {

            if (!idToTrackMap.containsKey(trackData.getKey())) {
                Track track = new Track();
                populateTrackData(track, trackData);
                Log.d("VIBEMODEGETTER", "determinePlaylist: not found: " + trackData.getKey());
                handler.getTrackFromStorage(trackData.getKey(), track);
                totalTracks.add(track);
            } else {
                //We populate the track data from that of our database
                Track track = idToTrackMap.get(trackData.getKey());
                Log.d("VIBEMODEGETTER", "determinePlaylist: exist: " + track.getName());
                populateTrackData(track, trackData);
                totalTracks.add(track);
            }
        }

        for (Track track: totalTracks) {
            determineScore(track);
        }

        PriorityQueue<Track> trackPQ = new PriorityQueue<>(new TrackComparator());
        for (Track track: totalTracks) {
            trackPQ.add(track);
        }
        return trackPQ.toArray(new Track[trackPQ.size()]);

    }

    private void populateTrackData(Track track, DataSnapshot trackData) {
        Double lastLat = (Double) trackData.child("lastLat").getValue();
        Double lastLon = (Double) trackData.child("lastLon").getValue();
        HashMap<String, Object> dateMap = (HashMap<String, Object>) trackData.child("date").getValue();
        if (dateMap == null)
            return;
        int year = ((Long) dateMap.get("year")).intValue() + 1900;
        int month = ((Long)dateMap.get("month")).intValue();
        int date = ((Long) dateMap.get("date")).intValue();
        int hours = ((Long) dateMap.get("hours")).intValue();
        int mins = ((Long) dateMap.get("minutes")).intValue();
        int seconds = ((Long) dateMap.get("seconds")).intValue();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hours, mins, seconds);
        String lastPlayedUserEmail = (String) trackData.child("lastUser").getValue();
        String trackName = (String) trackData.child("trackName").getValue();
        String albumName = (String) trackData.child("trackAlbum").getValue();
        String artistName = (String) trackData.child("artistName").getValue();

        track.setAlbum(albumName);
        track.setName(trackName);
        track.setArtist(artistName);
        track.setLastPlayedUser(lastPlayedUserEmail);
        if(getMockTime()) {
            track.setLastPlayedDate(fakeDate());
        } else {
            track.setLastPlayedDate(calendar.getTime());
        }
        track.setLastLon(lastLon);
        track.setLastLat(lastLat);
    }
    private void determineScore(Track track) {
        int trackScore = 0;
        if (withinDistance(track))
            trackScore++;
        if (playedWithinLastWeek(track))
            trackScore++;
        if (playedByFriend(track))
            trackScore++;
        track.setScore(trackScore);
    }
    private boolean withinDistance(Track track ){
        GPSTracker tracker = new GPSTracker(activity);

        Location myLoc = tracker.getLocation();
        if(myLoc==null){
            return false;//if somehow GPSTracker fails to return, catch null pointer exception
        }
        double lat = myLoc.getLatitude();
        double lon = myLoc.getLongitude();
        double trackLong = track.getLastLon();
        double trackLat = track.getLastLat();
        double rlat1 = Math.toRadians(lat);
        double rlat2 = Math.toRadians(trackLat);
        double rlambda = Math.toRadians(trackLong-lon);
        double R = 6371e3;
        //double dis is the distance in meters
        double dis = Math.acos(Math.sin(rlat1)*Math.sin(rlat2)+Math.cos(rlat1)*Math.cos(rlat2)*Math.cos(rlambda))*R;
        //double p2Norm = Math.sqrt(Math.pow((trackLat- lat),2) +Math.pow((trackLong -lon),2));
        //for dubugging
        Log.d("LastLocation", "distance:" + dis);
        return dis < 300;
    }

    private boolean playedWithinLastWeek(Track track) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        long currTime;
        if(getMockTime()) {
            currTime = fakeDate().getTime();
        } else {
            currTime = calendar.getTime().getTime();
        }
        long lastPlayedTime = track.getLastPlayedDate().getTime();
        long periodSeconds = (currTime - lastPlayedTime) / 1000;
        long elapsedDays = periodSeconds / 60 / 60 / 24;
        return false ? elapsedDays > 7 : true;
    }

    private boolean playedByFriend(Track track) {
        return user.getEmails().contains(track.getLastPlayedUser());
    }
}
