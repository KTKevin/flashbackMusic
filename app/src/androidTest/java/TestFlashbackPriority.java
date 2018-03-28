

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;


import com.example.rafael.flashback.FlashBackpriority;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.LastPlayedHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;



/**
 * Created by nulls on 2/18/2018.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class TestFlashbackPriority {

    private Context instrumentationCtx;
    Context context;
    LastPlayedHandler lastPlayedHandler;
    Track track;
    Track track2;
    Track track3;
    Track track4;
    FlashBackpriority flash;
    int[] timestamps;
    int[] timestamps2;
    int[] timestamps3;
    int[] timestamps4;

    @Before
    public void setup() {
        track = new Track("TrackName", 10, "ArtistName", null, "AlbumName", null);
        lastPlayedHandler = new LastPlayedHandler(context);
        track.setLastLon(-122.084095);
        track.setLastLat(37.422005);
        instrumentationCtx = InstrumentationRegistry.getContext();

        track.setLastPlayed("20180101000000");
        track2 = new Track("tname",10,"name",null,"namea",null);
        track2.setLastLat(0);
        track2.setLastLon(0);
        track2.setLastPlayed("20180102102000");
        track3 = new Track("pname", 10,"markZuckerberg sucks", null, "facebook sucks", null);
        Track[] trackRay = new Track[]{track,track2};
        flash = new FlashBackpriority(trackRay,context);
        timestamps = flash.trackTime(track);
        timestamps = flash.trackTime(track);
        timestamps2 = flash.trackTime(track2);
        track3.setLastPlayed("20000229115900");
        timestamps3 = flash.trackTime(track3);
        track4 = new Track("hello", 9, "i'm tired", null, "whatever", null);
        track4.setLastPlayed("20181231115900");
        timestamps4 = flash.trackTime(track4);
    }

    @Test
    public void testTrackTimes ()
    {
        assertEquals(0, timestamps[0]);
        assertEquals(0,timestamps[1]);
        assertEquals(1,timestamps[2]);
        assertEquals(1, timestamps[3]);
        assertEquals(10, timestamps2[0]);
        assertEquals(20, timestamps2[1]);
        assertEquals(1, timestamps2[2]);
        assertEquals(2, timestamps2[3]);
        assertEquals(11,timestamps3[0] );
        assertEquals(59, timestamps3[1]);
        assertEquals(2, timestamps3[2]);
        assertEquals(29,timestamps3[3]);
        assertEquals(11, timestamps4[0]);
        assertEquals(59, timestamps4[1]);
        assertEquals(12, timestamps4[2]);
        assertEquals(31, timestamps4[3]);
    }

    @Test
    public void testDayOfWeek(){
        assertEquals(1,flash.dayofweekUtil(timestamps));
        assertEquals(2,flash.dayofweekUtil(timestamps2));
        assertEquals(2, flash.dayofweekUtil(timestamps3));
        assertEquals(1, flash.dayofweekUtil(timestamps4));
    }

}
