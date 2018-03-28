package com.example.rafael.flashback.handlers;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.adapters.PeopleAdapter;
import com.example.rafael.flashback.fragments.VibeModeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.example.rafael.flashback.utils.CalendarMock.getMockTime;
import static com.example.rafael.flashback.utils.CalendarMock.fakeDate;

/**
 * Created by rafael on 2/14/2018.
 * Last changed at 20:30 2/18/2018 by Dianru Liu
 */

public class DatabaseHandler {
    private DatabaseReference database;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private PeopleAdapter user;
    private String userId;
    private Activity activity;

    public DatabaseHandler(Activity activity, PeopleAdapter user) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyBeQQkWDHHVSYiIpQnZHTwlGZi_J--YQtw")
                .setApplicationId("1:420148833376:android:9f0f35f112ec4fb2")
                .setDatabaseUrl("https://flashback-61de1.firebaseio.com")
                .build();
        // Get database reference
        FirebaseApp app = FirebaseApp.initializeApp(activity, options, "app");
        database = FirebaseDatabase.getInstance(app).getReference();

        storage = FirebaseStorage.getInstance(app, "gs://flashback-61de1.appspot.com/");
        storageReference = storage.getReference();
        // Use firebase's instance id to act as our unique user id
        userId = FirebaseInstanceId.getInstance(app).getId();
        this.activity = activity;
        this.user = user;
    }

    public void postTrackDataToDB(final Track track) {
        final DatabaseReference tracks = database.child("tracks");

        // Create an event listener that executes once
        tracks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Add track to database if not already in it
                if (!dataSnapshot.child(track.getDbId()).exists()) {
                    tracks.child(track.getDbId());
                }
                DatabaseReference dbTrack = tracks.child(track.getDbId());

                dbTrack.child("lastUser").setValue(user.getUserEmail());
                dbTrack.child("lastLat").setValue((float) track.getLastLat());
                dbTrack.child("lastLon").setValue((float) track.getLastLon());
                dbTrack.child("date").setValue(track.getLastPlayedDate());
                dbTrack.child("trackName").setValue(track.getName());
                dbTrack.child("trackAlbum").setValue(track.getAlbum());
                dbTrack.child("trackArtist").setValue(track.getArtist());

                //TODO: Add this field after we implement downloadable tracks
               // dbTrack.child("url").setValue(track.getDownloadUrl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Track> getTrackDataFromDB() {
        try {
            BrowseActivity activity = (BrowseActivity) this.activity;
            // Get tracks from parser
            final Map<String, Track> dbIdToTrack = activity.getParser().getDbIdToTrackMap();

            // Get data from DB
            final DatabaseReference dbTracks = database.child("tracks");
            dbTracks.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot trackSnapshot : dataSnapshot.getChildren()) {
                        Log.d("user", trackSnapshot.getKey());
                        Track track = dbIdToTrack.get(trackSnapshot.getKey());
                        Log.d("db", track.getDbId());
                        Double lastLat = (Double) trackSnapshot.child("lastLat").getValue();
                        Double lastLon = (Double) trackSnapshot.child("lastLon").getValue();
                        track.setLastLat(lastLat);
                        track.setLastLon(lastLon);
                        HashMap<String, Object> dateMap = (HashMap<String, Object>) trackSnapshot.child("date").getValue();
                        if (dateMap == null)
                            return;
                        int year = ((Long) dateMap.get("year")).intValue() + 1900;
                        int month = ((Long)dateMap.get("month")).intValue();
                        int date = ((Long) dateMap.get("date")).intValue();
                        int hours = ((Long) dateMap.get("hours")).intValue();
                        int mins = ((Long) dateMap.get("minutes")).intValue();
                        int seconds = ((Long) dateMap.get("seconds")).intValue();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, date, hours, mins, seconds);
                        String lastPlayed;
                        if(getMockTime()) {
                            lastPlayed = sdf.format(fakeDate());
                        } else {
                            lastPlayed = sdf.format(calendar.getTime());
                        }

                        track.setLastPlayed(lastPlayed);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return new ArrayList<Track>(Arrays.asList(dbIdToTrack.values().toArray(new Track[0])));
        } catch(Exception e) {

        }
        return new ArrayList<Track>();
    }

    public void determinePlaylist(final VibeModeHandler handler, final VibeModeFragment fragment) {
        final ArrayList<DataSnapshot> data = new ArrayList<>();
        // Get data from DB
        final DatabaseReference dbTracks = database.child("tracks");
        dbTracks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot trackSnapshot : dataSnapshot.getChildren()) {
                    data.add(trackSnapshot);
                }
                Track[] tracks = handler.determinePlaylist(data);
                fragment.updateTracks(tracks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void postTrackToStorage(Uri uri) {
        Uri file = Uri.fromFile(new File(uri.toString()));
        //TODO: This is how we differentiate tracks need to see if works
        String id = determineId(uri);
        Log.d("id", id);
        StorageReference tracks = storageReference.child("track").child(id);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("id", id)
                .build();
        UploadTask task = tracks.putFile(file, metadata);

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("songUpload", "failed to upload song");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("songUpload", "upload song successful");
            }
        });
    }

    public String determineId(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.getPath());
        String title = retriever.extractMetadata(7);
        String artist = retriever.extractMetadata(2);
        String album = retriever.extractMetadata(1);
        if(title==null){
            title = "default title";
        }
        if(artist==null){
            artist = "default artist";
        }
        if(album==null){
            album = "default album";
        }
       return Integer.toString((title + artist + album).hashCode());
    }

    public void getTrackFromStorage(String id, Track track) {
       StorageReference trackRef = storageReference.child("track/" + id);
       try {
           final File localFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), track.getName() + ".mp3");
           localFile.createNewFile();
           trackRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                   Log.d("DbDownloadSong", "download success");
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Log.d("DbDownloadSong", "download failed");

               }
           });
       } catch(Exception e) {

       }
    }
}
