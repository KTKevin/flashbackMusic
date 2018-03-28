package com.example.rafael.flashback;
import android.content.Context;
import android.location.Location;

import android.util.Log;
import android.util.Pair;

import com.example.rafael.flashback.utils.GPSTracker;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


/**
 * Created by Kevin Kim and Kaiwen Chen on 2/10/2018.
 * Author: Kevin Kim, Kaiwen Chen, Dianru Liu
 * Last changes: 2/18/2018 22:17 by Dianru Liu
 */

public class FlashBackpriority {
    private Track[] tracklist;//the reference of the tracks passed in this class
    private double[] probabilities;


   private String currtimeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(Calendar.getInstance().getTime());
   private String curryear = currtimeStamp.substring(0,4);
   private int currYear = Integer.parseInt(curryear);
   private String currhour = currtimeStamp.substring(9,11);
   private int currHour = Integer.parseInt(currhour);
   private String currminutes = currtimeStamp.substring(11,13);
   private int currMinutes = Integer.parseInt(currminutes);
   private String currmonth = currtimeStamp.substring(4,6);
   private int currMonth = Integer.parseInt(currmonth);
   private String currday = currtimeStamp.substring(6,8);
   private int currDay = Integer.parseInt(currday);
   private int[] currentTime = {currHour, currMinutes, currMonth, currDay, currYear};
   private int Today = dayofweekUtil(currentTime);
   private int currTOD = currHour * 60 + currMinutes;
    //@todo, learn what to pass in ot GPSTracker object, then make an instance of GPSTracker.
   private int disliked = 0;//record how many tracks are disliked

    public FlashBackpriority(Track[] trackArray, Context context) {
        probabilities = new double[trackArray.length];
        GPSTracker myLoc = new GPSTracker(context);
        tracklist = trackArray;
        //use this to find current location;
        //myLoc.getLocation()


        for (int i = 0; i < tracklist.length; i++) {
            probabilities[i] = 0.1;//even the least-recently-played track has a chance of being played again
            if(trackTime(tracklist[i])==null){
                if(tracklist[i].getPreference() == -1){
                    probabilities[i] = 0.0;
                    disliked ++;
                }
                continue;
            }
            //This is the base probability.
            int timeoftheday = trackTime(tracklist[i])[0]*60 + trackTime(tracklist[i])[1];
            //This is the time of the day last played in minutes.
            int dayoftheweek = dayofweekUtil(trackTime(tracklist[i]));
            // dayoftheweek = 0 - 6 = Sunday - Saturday
            if (tracklist[i].getPreference() == 1) {
                probabilities[i] += .3;
            }
            if(dayoftheweek == Today)
            {
                probabilities[i] += .2;
            }
            if(Math.abs(currTOD - timeoftheday)<=30||Math.abs(currTOD - timeoftheday)>=1410)
            {
                probabilities[i] += .2;
            }
            if (distance(context, tracklist[i]) <=300)
            {
                probabilities[i] += .5;//base increment to all locations in 300 meters
                //within 300 meters, closer location gets higher priority
                probabilities[i] += 1.0 * ((300 - distance(context, tracklist[i]))/300);
            }
            if (tracklist[i].getPreference() == -1){
                probabilities[i] = 0.0;
                disliked ++;
            }
        }
    }

    //@todo, make a function that calculates distance from your location to song's location;
    //for debugging distance function


    public double distance(Context context, Track track ){
        GPSTracker tracker = new GPSTracker(context);

        Location myLoc = tracker.getLocation();
        if(myLoc==null){
            return 99999;//if somehow GPSTracker fails to return, catch null pointer exception
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
        return dis;
    }



    public int[] trackTime(Track track)
    {//format = yyyyMMddHHmmss
        String timeStamp = track.getLastPlayed();
        if(timeStamp == ""){
            return null;
        }
        String hour =  timeStamp.substring(8,10);
        int intHour = Integer.parseInt(hour);
        String minutes = timeStamp.substring(10,12);
        int intMinutes = Integer.parseInt(minutes);
        String month = timeStamp.substring(4,6);
        int intMonth = Integer.parseInt(month);
        String day = timeStamp.substring(6,8);
        int intDay = Integer.parseInt(day);
        String year = timeStamp.substring(0,4);
        int intYear = Integer.parseInt(year);
        int [] times = {intHour, intMinutes, intMonth, intDay, intYear};

        return times;

    }

    public int dayofweekUtil(int[] tracktime){
        int datenumber = tracktime[3];
        int year = tracktime[4];
        int YY = year%100; // YY is the last two digits of the year
        int yearcode = (YY+(YY/4))%7;
        int monthcode = -1;
        if(tracktime[2]==1){
            monthcode = 0;
        }
        if(tracktime[2]==2){
            monthcode = 3;
        }
        if(tracktime[2]==3){
            monthcode = 3;
        }
        if(tracktime[2]==4){
            monthcode = 6;
        }
        if(tracktime[2]==5){
            monthcode = 1;
        }
        if(tracktime[2]==6){
            monthcode = 4;
        }
        if(tracktime[2]==7){
            monthcode = 6;
        }
        if(tracktime[2]==8){
            monthcode = 2;
        }
        if(tracktime[2]==9){
            monthcode = 5;
        }
        if(tracktime[2]==10){
            monthcode = 0;
        }
        if(tracktime[2]==11){
            monthcode = 3;
        }
        if(tracktime[2]==12){
            monthcode = 5;
        }

        int centurycode = 6;
        int leapyear = 0;
        if(year%4==0){
            leapyear = 1;
            if(year%100!=0){
                leapyear = 0;
            }
        }
        if(year%400==0){
            leapyear = 1;
        }
        int theDay = (yearcode + monthcode + centurycode + datenumber - leapyear)%7;
        // theDay = 0 - 6 = Sunday - Saturday
        return theDay;
    }

    public Track pickaTrack() {
        double totalweight = 0;
        for (int i = 0; i < probabilities.length; i++) {
            totalweight += probabilities[i];
        }
        Random r = new Random();
        double randomdouble = 0.0 + totalweight * r.nextDouble();
        for (int i = 0; i < probabilities.length; i++) {
            randomdouble -= probabilities[i];
            if (randomdouble <= 0) {
                probabilities[i] = 0;//chosen track will not be picked again until probabilities is rebuilt;
                return tracklist[i];//this is the desired return;
            }
        }
        //to debug

        return tracklist[0];//this is default return
    }

    public Track[] shuffle(int length){
        if(length>tracklist.length){
            length = tracklist.length;
            //if required length of playlist is greater than the number of tracks
            //set length to the number of tracks
        }
        if(length>(tracklist.length-disliked)){
            length = (tracklist.length-disliked);
        }
        Track[] playlist = new Track[length];
        for(int i = 0; i < length; i++){
            playlist[i] = pickaTrack();
        }
        Log.d("shuffle", "playlist:" + playlist);

        return playlist;
    }


}