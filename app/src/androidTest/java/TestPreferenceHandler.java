import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.LastPlayedHandler;
import com.example.rafael.flashback.handlers.PreferenceHandler;

import org.junit.Test;
import org.junit.Before;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

/**
 * Created by mukmai on 2/16/2018.
 */

public class TestPreferenceHandler {

    Track track;
    Context context;
    PreferenceHandler preferenceHandler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Before
    public void setup() throws Exception{
        context = InstrumentationRegistry.getContext();
        track = new Track("TrackName", 10, "ArtistName", null, "AlbumName", null);
        preferenceHandler = new PreferenceHandler(context);
        sharedPreferences = context.getSharedPreferences("TrackName", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Test
    public void loadPreference_new_track() throws Exception{
        editor.clear();
        editor.apply();
        preferenceHandler.loadPreference(track);
        assertEquals(0, track.getPreference());
    }

    @Test
    public void loadPreference_existing_track() throws Exception{
        editor.putInt("preference", 1);
        editor.apply();
        preferenceHandler.loadPreference(track);
        assertEquals(1, track.getPreference());
    }

    @Test
    public void changePreference_neu_fav() throws Exception{
        editor.putInt("preference", 0);
        editor.apply();
        preferenceHandler.loadPreference(track);
        assertEquals(0, track.getPreference());
        assertEquals(0, sharedPreferences.getInt("preference",0));
        preferenceHandler.changePreference(track);
        assertEquals(1, track.getPreference());
        assertEquals(1, sharedPreferences.getInt("preference",0));
    }

    @Test
    public void changePreference_fav_dis() throws Exception{
        editor.putInt("preference", 1);
        editor.apply();
        preferenceHandler.loadPreference(track);
        assertEquals(1, track.getPreference());
        assertEquals(1, sharedPreferences.getInt("preference",0));
        preferenceHandler.changePreference(track);
        assertEquals(-1, track.getPreference());
        assertEquals(-1, sharedPreferences.getInt("preference",0));
    }

    @Test
    public void changePreference_dis_neu() throws Exception{
        editor.putInt("preference", -1);
        editor.apply();
        preferenceHandler.loadPreference(track);
        assertEquals(-1, track.getPreference());
        assertEquals(-1, sharedPreferences.getInt("preference",0));
        preferenceHandler.changePreference(track);
        assertEquals(0, track.getPreference());
        assertEquals(0, sharedPreferences.getInt("preference",0));
    }
}
