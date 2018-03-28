package com.example.rafael.flashback.utils;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.rafael.flashback.Track;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wescer on 2018/3/11.
 * Settings of Scan path is on line 45, could be edited.
 * Usage:
 * 1. create a new DownloadScanner
 * 2. call .getTracks()(returns a Track [])
 */

public class DownloadScanner {
    private ArrayList<Track> tracklist = new ArrayList<>();
    int index = 0;
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public ArrayList<Track> getTracks(){
        scan();
        return tracklist;
    }

    public void scan(){
        if(isExternalStorageReadable()) {
            try {
                String[] ext = {".mp3"};
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Log.d("Filepath",""+path.getAbsolutePath());
                searchMP3(path,ext);
                Log.d("tracklist","length = "+tracklist.size());
            }catch(Exception e){}
        }
    }

    public void searchMP3(File file, String[] ext){
        Log.d("Filepath", "isDirectory: "+file.isDirectory());
            if(file.isDirectory()){
                File[] listFile = file.listFiles();
                Log.d("Filelist", "length: "+listFile.length);
                if(listFile!=null){
                    for(int i = 0; i < listFile.length; i++){
                        searchMP3(listFile[i],ext);
                    }
                }
            }else {
                Log.d("track", "index: "+index);
                String fileAbsolutePath = file.getAbsolutePath();
                Log.d("track", "currtrack: "+fileAbsolutePath);
                for(int i = 0; i < ext.length; i++){
                    Log.d("track", "endwith mp3 "+fileAbsolutePath.endsWith(ext[i]));
                    if(fileAbsolutePath.endsWith(ext[i])){
                        Log.d("track", "index2: "+index);
                        tracklist.add(createTrack(fileAbsolutePath));
                        Log.d("track", "listlength: "+tracklist.size());
                        index++;
                        Log.d("track", "index: "+index);
                        break;
                    }
                }
            }
    }

    public Track createTrack(String filepath){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filepath);
        String title = retriever.extractMetadata(7);
        long runtime = Long.parseLong(retriever.extractMetadata(9));
        String artist = retriever.extractMetadata(2);
        String album = retriever.extractMetadata(1);
        byte[] cover = retriever.getEmbeddedPicture();
        Uri id = Uri.fromFile(new File(filepath));
        if(title==null){
            title = "default title";
        }
        if(artist==null){
            artist = "default artist";
        }
        if(album==null){
            album = "default album";
        }
        Track newtrack = new Track(title, runtime, artist, id, album, cover);
        Log.d("track", "newtrack: "+newtrack.getId());
        return newtrack;
    }
}
