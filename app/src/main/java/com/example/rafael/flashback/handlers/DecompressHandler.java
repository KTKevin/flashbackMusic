package com.example.rafael.flashback.handlers;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.rafael.flashback.Track;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * created by russell brobst
 */
public class DecompressHandler {
    private String zipFileName;
    private ArrayList<File> zipFile;
    private String location;
    private String filename;
    private ArrayList<Uri> musicFile;

    public DecompressHandler(String filename) {
        zipFile = new ArrayList<File>();
        musicFile = new ArrayList<>();
        this.filename = filename;
        Log.d("Files", Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getAbsolutePath());
        File directory = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);

        File file = new File(directory, this.filename);
        String extension[] = this.filename.split(Pattern.quote("."));
        String ex = extension[1];
        if(ex.equals("zip")) {
            zipFileName = this.filename;
            zipFile.add(file);
        }
        location = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getAbsolutePath();

        _dirChecker("");
    }

    public ArrayList<Uri> getMusicFile(){
        return musicFile;
    }

    public void unzip() throws IOException {
        int BUFFER_SIZE = 1024;
        int size;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            if ( !location.endsWith(File.separator) ) {
                location += File.separator;
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(location + zipFileName), BUFFER_SIZE));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                        try {
                            while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                    Log.d("FILE UNZIPPED",new File(path).toString());
                    musicFile.add(Uri.parse(new File(path).toString()));
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e("ZIP", "Unzip exception", e);
        }

        Log.d("ZIP COMPLETED", ":)");
        zipFile.get(0).delete();
        Log.d("FILE DELETED", zipFile.get(0).toString());

    }

    private void _dirChecker(String dir) {
        File f = new File(location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}