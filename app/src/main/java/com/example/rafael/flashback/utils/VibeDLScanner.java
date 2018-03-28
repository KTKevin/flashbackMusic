package com.example.rafael.flashback.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.example.rafael.flashback.Track;

import java.io.File;

/**
 * Created by Wescer on 2018/3/11.
 * Usage:
 * 1.Create a new VideDLScanner
 * 2.Call getTracks(Context mc, Uri[] a), returns a Track[]
 */

public class VibeDLScanner {
    private Track[] tracklist;
    private Uri[] Uris;
    private Context context;

    public Track[] getTracks(Context mc, Uri[] a){
        Uris = a;
        context = mc;
        scan();
        return tracklist;
    }
    public void scan(){
        for(int i = 0; i<Uris.length; i++){
            tracklist[0] = createTrack(Uris[0]);
        }
    }

    public Track createTrack(Uri a){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, a);
        String title = retriever.extractMetadata(7);
        long runtime = Long.parseLong(retriever.extractMetadata(9));
        String artist = retriever.extractMetadata(2);
        String album = retriever.extractMetadata(1);
        byte[] cover = retriever.getEmbeddedPicture();
        Track newtrack = new Track(title, runtime, artist, a, album, cover);
        return newtrack;
    }
}
