package com.example.rafael.flashback;

/**
 * Created by null on 3/16/18.
 */

import org.junit.Test;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;


import com.example.rafael.flashback.utils.CalendarMock;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;



@MediumTest
public class fakeCalendarUnitTest {
    int hour =0;
    int year =2016;
    int day =1;
    int month =1;
    int second =1;
    int minute =1;



    public static CalendarMock cal = new CalendarMock();
    public static CalendarMock cal2 = new CalendarMock();
    public static CalendarMock cal3 = new CalendarMock();

    public void setup(){
        hour = 0;
        year = 2016;
        day = 1;
        month =1;
        second =1;
        minute =1;




    }
    @Test
     public void  TestCalendar()

    {
        cal.setDay(day);
        cal.setMonth(month);
        cal.setYear(year);
        cal.setHour(hour);
        cal.setSecond(second);
        cal.setMinute(minute);
        cal.setMockTime(true);

       // String currtimeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(Cal.getTime());
      //  Log.e("hello", cal.getCurTime());

        assertEquals("20160101-000101", cal.getCurTime());

        cal2.setDay(20);
        cal2.setMonth(12);
        cal2.setYear(2016);
        cal2.setHour(12);
        cal2.setSecond(30);
        cal2.setMinute(20);
//        Log.e("helloworld", cal2.getCurTime());
        assertEquals("20161220-122030", cal2.getCurTime());


    }

    @Test
    public void TestDate()  {
        cal.setDay(day);
        cal.setMonth(month);
        cal.setYear(year);
        cal.setHour(hour);
        cal.setSecond(second);
        cal.setMinute(minute);
        cal.setMockTime(true);
        String currtimeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(cal.fakeDate());
        assertEquals("20160101-000101", currtimeStamp);



    }




}
