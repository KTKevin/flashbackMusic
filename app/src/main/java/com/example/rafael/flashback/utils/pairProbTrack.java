package com.example.rafael.flashback.utils;

/**
 * Created by nulls on 2/17/2018.
 */

import android.support.annotation.NonNull;

import com.example.rafael.flashback.Track;

import java.util.Random;

/**
 * Created by nulls on 2/17/2018.
 */

public class pairProbTrack implements Comparable<pairProbTrack> {
    private Track track;
    private double probability;
    private int priority;
    public pairProbTrack(Track argTrack, double argProb){
        track = argTrack;
        probability = argProb;

    }
    public double getProb()
    {
        return probability;
    }
    public Track getTrack()
    {
        return track;
    }
    public void setPriority(){
        Random ran = new Random();

        if (probability == 0)
        {
            priority= 0;
        }
        else if (probability <= .5)
        {
            priority = ran.nextInt(5);
        }
        else if(probability <= .9)
        {
            priority = ran.nextInt(9);
        }
        else
        {
            priority = 10;
        }


    }
    public int getPriority(){
        return priority;
    }


    @Override
    public int compareTo(pairProbTrack pairProbTrack) {
        if (this.getPriority() > pairProbTrack.getPriority())
        {
            return -1;
        }
        else if (this.getPriority() == pairProbTrack.getPriority())
        {
            return 0;
        }
        else
        {
            return 1;
        }



    }
}

