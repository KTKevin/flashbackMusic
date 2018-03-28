import android.net.Uri;

import com.example.rafael.flashback.Album;
import com.example.rafael.flashback.Track;
import com.example.rafael.flashback.handlers.FilterHandler;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

/**
 * Created by mukmai on 3/11/2018.
 */

public class TestFilterHandler {

    ArrayList<Track> trackList;
    ArrayList<String> stringList, sortedStringList;
    int[] favorList = {0,1,-1,-1,1,0};
    int[] sortedFavorList = {1,1,0,0,-1,-1};
    FilterHandler filterHandler;

    @Before
    public void setup() throws Exception {
        filterHandler = new FilterHandler();
        trackList = new ArrayList<>();
        stringList = new ArrayList<>();
        stringList.add("DDD"); stringList.add("TAA"); stringList.add("BBB"); stringList.add("BBA"); stringList.add("AAA"); stringList.add("DDD");
        sortedStringList = new ArrayList<>();
        sortedStringList.add("DDD"); sortedStringList.add("TAA"); sortedStringList.add("BBB"); sortedStringList.add("BBA"); sortedStringList.add("AAA"); sortedStringList.add("DDD");
        for (int i = 0; i < stringList.size(); i++) {
            trackList.add(new Track(stringList.get(i), 100, stringList.get(i), null, stringList.get(i), null));
            trackList.get(i).setPreference(favorList[i]);
        }
        sortedStringList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    @Test
    public void filterAlbum() throws Exception {
        ArrayList<Track> result = filterHandler.filterList(trackList, "album");
        for (int i = 0; i < trackList.size(); i++) {
            assertEquals(sortedStringList.get(i), result.get(i).getAlbum());
            assertEquals(stringList.get(i), trackList.get(i).getAlbum());
        }
    }

    @Test
    public void filterTitle() throws Exception {
        ArrayList<Track> result = filterHandler.filterList(trackList, "title");
        for (int i = 0; i < trackList.size(); i++) {
            assertEquals(sortedStringList.get(i), result.get(i).getName());
            assertEquals(stringList.get(i), trackList.get(i).getName());
        }
    }

    @Test
    public void filterArtist() throws Exception {
        ArrayList<Track> result = filterHandler.filterList(trackList, "artist");
        for (int i = 0; i < trackList.size(); i++) {
            assertEquals(sortedStringList.get(i), result.get(i).getArtist());
            assertEquals(stringList.get(i), trackList.get(i).getArtist());
        }
    }

    @Test
    public void filterFavorite() throws Exception {
        ArrayList<Track> result = filterHandler.filterList(trackList, "favorite");
        for (int i = 0; i < trackList.size(); i++) {
            assertEquals(sortedFavorList[i], result.get(i).getPreference());
            assertEquals(favorList[i], trackList.get(i).getPreference());
        }
    }

    @Test
    public void filterFail() throws Exception {
        ArrayList<Track> result = filterHandler.filterList(trackList, "fail");
        for (int i = 0; i < trackList.size(); i++) {
            assertEquals(trackList.get(i).getAlbum(), result.get(i).getAlbum());
        }
    }
}
