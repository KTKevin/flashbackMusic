package com.example.rafael.flashback.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by null on 3/16/18.
 */

public class CalendarMock extends Calendar {
    static private boolean mockTime = false;
    static private int hour;
    static private int minute;
    static private int second;
    static private int year;
    static private int month;
    static private int day;






    public static void setHour(int h){
        hour = h;

    }
    public static void setMinute(int m){
        minute = m;
    }
    public static void setSecond(int s){
        second = s;
    }
    public static void setDay(int d){
        day = d;
    }
    public static void setMonth(int m){
        month = m;
    }
    public static void setYear(int y){
        year = y;
    }
    public static void setMockTime(boolean m){
        mockTime = m;
    }
    public static boolean getMockTime(){
        return mockTime;
    }


    @Override
    protected void computeTime() {

    }

    @Override
    protected void computeFields() {

    }

    @Override
    public void add(int field, int amount) {

    }

    @Override
    public void roll(int field, boolean up) {

    }

    @Override
    public int getMinimum(int field) {
        return 0;
    }

    @Override
    public int getMaximum(int field) {
        return 0;
    }

    @Override
    public int getGreatestMinimum(int field) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int field) {
        return 0;
    }


    public static String getCurTime(){
        StringBuilder str = new StringBuilder();
        String strHour;
        String strMin;
        String strDay;
        String strSec;
        String strMonth;
          if(hour <10) {
               strHour = String.format("0%1d", hour);
          }
          else{
               strHour = Integer.toString(hour);
          }
          if(minute <10) {
               strMin = String.format("0%1d", minute);
          }
          else{
               strMin = Integer.toString(minute);
          }

        String strYear = Integer.toString(year);
          if(day <10) {
              strDay = String.format("0%1d", day);
          }
          else
          {
              strDay = Integer.toString(day);
          }
          if(second <10) {
              strSec = String.format("0%1d", second);
          }
          else{
              strSec = Integer.toString(second);
          }
          if(month <10) {
               strMonth = String.format("0%1d", month);
          }
          else{
              strMonth = Integer.toString(month);
          }
        str.append(strYear);
        str.append(strMonth);
        str.append(strDay);
        str.append("-");
        str.append(strHour);
        str.append(strMin);
        str.append(strSec);
        String retVal = str.toString();
        retVal.replaceAll("\\s+", "");


        return retVal;


    }
    public static Date fakeDate() {

        DateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
        Date date = null;
        try {
            date = formatter.parse(getCurTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

}
