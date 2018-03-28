package com.example.rafael.flashback.handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.rafael.flashback.utils.GPSTracker;
import com.example.rafael.flashback.Track;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.rafael.flashback.utils.CalendarMock.getMockTime;
import static com.example.rafael.flashback.utils.CalendarMock.fakeDate;


/**
 * Created by mukmai on 2/10/2018.
 */

public class LastPlayedHandler {

    private Context context;

    public LastPlayedHandler(Context con) {
        this.context = con;
    }

    public void loadLastPlayed(Track track) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(track.getName(), context.MODE_PRIVATE);
        track.setLastPlayed(sharedPreferences.getString("lastPlayed",""));
        track.setLastLat(sharedPreferences.getFloat("lastLat", 0));
        track.setLastLon(sharedPreferences.getFloat("lastLon", 0));
        track.setLocationName(sharedPreferences.getString("lastLocation", ""));
    }

    public void changeLastPlayed(Track track) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(track.getName(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        GPSTracker g = new GPSTracker(context);
        Location location = g.getLocation();
        if (location != null) {
            editor.putFloat("lastLat", (float)location.getLatitude());
            editor.putFloat("lastLon", (float)location.getLongitude());
            track.setLastLat((float)location.getLatitude());
            track.setLastLon((float)location.getLongitude());
            getLocationName(track);
            editor.putString("lastLocation", track.getLocationName());
            if(getMockTime()) {
                track.setLastPlayedDate(fakeDate());
            } else {
                track.setLastPlayedDate(calendar.getTime());
            }
            // TODO: DETERMINE WHAT IS BEST

            //for debugging GPSTracker
//            Log.d("LastLocation", "lat: " + location.getLatitude() + " lon: " + location.getLongitude());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String lastPlayed;
        if(getMockTime()) {
            lastPlayed = sdf.format(fakeDate());
        } else {
            lastPlayed = sdf.format(calendar.getTime());
        }
        editor.putString("lastPlayed", lastPlayed);
        editor.apply();

        track.setLastPlayed(lastPlayed);
    }

    public void getLocationName(Track track) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(track.getLastLat(), track.getLastLon(), 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append(", ");
                }
                stringBuilder.append(address.getAddressLine(address.getMaxAddressLineIndex()));
                result = stringBuilder.toString();
            }
        } catch (IOException e) {
            Log.e("LocationAddress", "Unable connect to Geocoder", e);
        } finally {
            track.setLocationName(result);
        }
    }
}
