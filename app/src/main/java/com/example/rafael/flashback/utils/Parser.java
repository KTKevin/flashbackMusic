package com.example.rafael.flashback.utils;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.example.rafael.flashback.Album;
import com.example.rafael.flashback.R;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.LastPlayedHandler;
import com.example.rafael.flashback.handlers.PreferenceHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mukmai on 2/4/2018.
 */

public class Parser {
    private MediaMetadataRetriever mmr;
    private ArrayList<Album> albumList; // list of albums
    private ArrayList<Track> trackList; // list of tracks
    private Map<String, Track> dbIdToTrackMap; // hash map from hashed URI to track
    private LastPlayedHandler lastPlayedHandler;
    private PreferenceHandler preferenceHandler;

    public ArrayList<Track> getTrackList() {
        return trackList;
    }

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public Parser() {
        mmr = new MediaMetadataRetriever();
        albumList = new ArrayList<>();
        trackList = new ArrayList<>();
        dbIdToTrackMap = new HashMap<String, Track>();
    }

    // just for testing
    public void setAlbumList(ArrayList<Album> albumList) {
        this.albumList = albumList;
    }

    public void parse(Activity activity) {
        mmr = new MediaMetadataRetriever();
        albumList = new ArrayList<>();
        trackList = new ArrayList<>();
        lastPlayedHandler = new LastPlayedHandler(activity.getApplicationContext());
        preferenceHandler = new PreferenceHandler(activity.getApplicationContext());
        dbIdToTrackMap = new HashMap<String, Track>();

        Field[] fields = R.raw.class.getFields();
        ArrayList<String> filenames = new ArrayList<String>();
        for(int i = 0; i < fields.length; i++) {
            filenames.add(fields[i].getName());
        }

        // use for debug
        for(int i = 0; i < filenames.size(); i++) {
            Log.d("filenames", filenames.get(i));
        }

        for(int i = 0; i < filenames.size(); i++) {
            // add ".mp3" in case the file does not read
            Uri mp3 = Uri.parse("android.resource://" + activity.getPackageName() + "/raw/" + filenames.get(i));
            mmr.setDataSource(activity, mp3);

            byte[] albumCover = mmr.getEmbeddedPicture();
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String runtimeString = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long runtime = Long.parseLong(runtimeString);

            Track curTrack = new Track(name, runtime, artist, mp3, albumName, albumCover);
            lastPlayedHandler.loadLastPlayed(curTrack);
            preferenceHandler.loadPreference(curTrack);

            // insert track object to corresponding album object
            if (!albumName.isEmpty()) {
                Album searchResult = searchAlbum(albumName);
                if (searchResult != null) {
                    searchResult.addTrack(curTrack);
                } else {
                    Album curAlbum = new Album(albumName, curTrack, albumCover);
                    albumList.add(curAlbum);
                }
            }
            trackList.add(curTrack);
            dbIdToTrackMap.put(curTrack.getDbId(), curTrack);
        }
    }

    public void parseDownload(Activity activity){
        lastPlayedHandler = new LastPlayedHandler(activity.getApplicationContext());
        preferenceHandler = new PreferenceHandler(activity.getApplicationContext());
        DownloadScanner ds = new DownloadScanner();
        ArrayList<Track> tracks = ds.getTracks();
        for (int i = 0; i < tracks.size(); i++) {
            lastPlayedHandler.loadLastPlayed(tracks.get(i));
            preferenceHandler.loadPreference(tracks.get(i));
            trackList.add(tracks.get(i));
            String albumName = tracks.get(i).getAlbum();
            if (albumName!=null) {
                Album searchResult = searchAlbum(albumName);
                if (searchResult != null) {
                    searchResult.addTrack(tracks.get(i));
                } else {
                    Album curAlbum = new Album(albumName, tracks.get(i), tracks.get(i).getAlbumcover());
                    albumList.add(curAlbum);
                }
            }
        }
    }

    public Map<String, Track> getIdToTracksMap(Activity activity) {
        trackList = new ArrayList<>();
        parseDownload(activity);
        Map<String, Track> map = new HashMap<>();
        for(Track track: trackList) {
            map.put(track.getDbId(), track);
        }
        return map;
    }

    // Helper function for finding album
    public Album searchAlbum(String albumName) {
        for(int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(albumName)) {
                return albumList.get(i);
            }
        }
        return null;
    }

    public Map<String, Track> getDbIdToTrackMap() {
        return dbIdToTrackMap;
    }

}
