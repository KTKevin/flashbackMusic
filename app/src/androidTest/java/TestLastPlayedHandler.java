import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.LastPlayedHandler;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mukmai on 2/16/2018.
 */

public class TestLastPlayedHandler {

    Context context;
    LastPlayedHandler lastPlayedHandler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Track track;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getContext();
        track = new Track("TrackName", 10, "ArtistName", null, "AlbumName", null);
        lastPlayedHandler = new LastPlayedHandler(context);
        sharedPreferences = context.getSharedPreferences("TrackName", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    @Test
    public void TestLoadLastPlayedEmpty() throws Exception {
        track.setLastPlayed("h");
        track.setLastLat(0.1);
        track.setLastLon(0.2);
//        track.setPlayCount(1);
        track.setLocationName("hello");
        lastPlayedHandler.loadLastPlayed(track);
        assertEquals("", track.getLastPlayed());
        assertEquals(0.0, track.getLastLat(), 1e-15);
        assertEquals(0.0, track.getLastLon(), 1e-15);
//        assertEquals(0, track.getPlayCount());
        assertEquals("N/A", track.getLocationName());
    }

    @Test
    public void TestLoadLastPlayedValues() throws Exception{
        editor.putString("lastPlayed", "money");
        editor.putFloat("lastLat", 1.0f);
        editor.putFloat("lastLon", 1.0f);
//        editor.putInt("playCo/unt", 1);
        editor.putString("lastLocation", "power");

        editor.apply();
        lastPlayedHandler.loadLastPlayed(track);
        assertEquals("money", track.getLastPlayed());
        assertEquals(1.0, track.getLastLat(), 1e-15);
        assertEquals(1.0, track.getLastLon(), 1e-15);
//        assertEquals(1, track.getPlayCount());
        assertEquals("power", track.getLocationName());
        editor.clear().commit();
    }


    @Test
    public void TestGetLocationSantaClara() throws Exception {
        track.setLastLon(-122.084095);
        track.setLastLat(37.422005);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-122.084095, track.getLastLon(), 1e-15);
        assertEquals(37.422005, track.getLastLat(), 1e-15);
        assertEquals("1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", track.getLocationName());
    }

    @Test
    public void TestGetLocationSanDiego() throws Exception {
        track.setLastLon(-117.142262);
        track.setLastLat(32.931125);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-117.142262, track.getLastLon(), 1e-15);
        assertEquals(32.931125, track.getLastLat(), 1e-15);
        assertEquals("8426-8430 Menkar Road, San Diego, CA 92126, USA", track.getLocationName());
    }

    @Test
    public void TestGetLocationUCSD() throws Exception {
        track.setLastLon(-117.234035);
        track.setLastLat(32.880074);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-117.234035, track.getLastLon(), 1e-15);
        assertEquals(32.880074, track.getLastLat(), 1e-15);
        assertEquals("Matthews Lane, La Jolla, CA 92093, USA", track.getLocationName());
    }

    @Test
    public void TestGetLocationNewYork() throws Exception {
        track.setLastLon(-73.985667);
        track.setLastLat(40.748444);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-73.985667, track.getLastLon(), 1e-15);
        assertEquals(40.748444, track.getLastLat(), 1e-15);
        assertEquals("14 West 34th Street, New York, NY 10001, USA", track.getLocationName());
    }

    @Test
    public void TestGetLocationBigBen() throws Exception {
        track.setLastLon(-0.124626);
        track.setLastLat(51.500742);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-0.124626, track.getLastLon(), 1e-15);
        assertEquals(51.500742, track.getLastLat(), 1e-15);
        assertEquals("67 Bridge Street, Westminster, London, SW1A 2PW, UK", track.getLocationName());
    }

    @Test
    public void TestGetLocationForbiddenPalace() throws Exception {
        track.setLastLon(116.389863);
        track.setLastLat(39.926034);
        lastPlayedHandler.getLocationName(track);
        assertEquals(116.389863, track.getLastLon(), 1e-15);
        assertEquals(39.926034, track.getLastLat(), 1e-15);
        assertEquals("China, Beijing Shi, Xicheng Qu, Wen Jin Jie, 1号, 北海公园", track.getLocationName());
    }

    @Test
    public void TestGetLocationUCSDMore() throws Exception {
        track.setLastLon(-117.243551);
        track.setLastLat(32.881912);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-117.243551, track.getLastLon(), 1e-15);
        assertEquals(32.881912, track.getLastLat(), 1e-15);
        assertEquals("9355-9559 North Torrey Pines Road, La Jolla, CA 92037, USA", track.getLocationName());
    }

    @Test
    public void TestGetLocationUCSDPool() throws Exception {
        track.setLastLon(-117.231889);
        track.setLastLat(32.880596);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-117.231889, track.getLastLon(), 1e-15);
        assertEquals(32.880596, track.getLastLat(), 1e-15);
        assertEquals("3390 Voigt Drive, San Diego, CA 92121, USA", track.getLocationName());
    }

    @Test
    public void TestGetLocationPartySchool() throws Exception {
        track.setLastLon(-117.071900);
        track.setLastLat(32.775729);
        lastPlayedHandler.getLocationName(track);
        assertEquals(-117.071900, track.getLastLon(), 1e-15);
        assertEquals(32.775729, track.getLastLat(), 1e-15);
        assertEquals("Campanile Mall, San Diego, CA 92115, USA", track.getLocationName());
    }
}
