package com.example.rafael.flashback.fragments;
import com.example.rafael.flashback.BrowseActivity;
import com.example.rafael.flashback.handlers.DatabaseHandler;
import com.example.rafael.flashback.handlers.DecompressHandler;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafael.flashback.R;
import com.example.rafael.flashback.handlers.DownloadURLHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;


/**
 * Created by russell
 *
 * Some code borrowed from:
 * https://www.androidtutorialpoint.com/networking/android-download-manager-tutorial-download-file-using-download-manager-internet/
 */

public class DownloadFragment extends Fragment {
    private Button DownloadMusic;
    private Button DownloadStatus;
    private Button CancelDownload;
    private EditText URL_Input;
    private boolean cancel = false;

    private DownloadManager downloadManager;
    private String url;
    private String ex;
    private String filename;
    private long Music_DownloadId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.download_fragment, container, false);

        //Download Music from URL
        DownloadMusic = (Button) rootView.findViewById(R.id.downloadTrackBttn);
        DownloadMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL_Input =  (EditText) rootView.findViewById(R.id.urlInput);
                url = URL_Input.getText().toString();
                Uri music_uri = Uri.parse(url);
                Music_DownloadId = DownloadData(music_uri, rootView);
            }
        });

        //Check Download status
        DownloadStatus = (Button) rootView.findViewById(R.id.checkTrackBttn);
        DownloadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_Music_Status(Music_DownloadId);
            }
        });
        DownloadStatus.setEnabled(false);

        //Cancel Current Download
        CancelDownload = (Button) rootView.findViewById(R.id.cancelTrackBttn);
        CancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel = true;
                downloadManager.remove(Music_DownloadId);
            }
        });
        CancelDownload.setEnabled(false);

        //set filter to only when download is complete and register broadcast receiver
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getContext().registerReceiver(downloadReceiver, filter);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Check_Music_Status(long Music_DownloadId) {

        DownloadManager.Query MusicDownloadQuery = new DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        MusicDownloadQuery.setFilterById(Music_DownloadId);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(MusicDownloadQuery);
        if(cursor.moveToFirst()){
            DownloadStatus(cursor, Music_DownloadId);
        }

    }

    private void DownloadStatus(Cursor cursor, long DownloadId){

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        Log.d("Download", "Checking Status");

        String statusText = "";
        String reasonText = "";

        switch(status){
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch(reason){
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch(reason){
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                break;
        }


            Toast toast = Toast.makeText(getContext(),
                    "Music Download Status:" + "\n" + statusText + "\n" +
                            reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

    }

    private long DownloadData (Uri uri, View v) {

        long downloadReference;

        downloadManager = (DownloadManager)getContext().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        filename = getFileName(uri);
        String extension[] = filename.split(Pattern.quote("."));
        ex = extension[1];

        Toast toast = Toast.makeText(getContext(), "Downloading: " + filename,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
        Log.d("Download", "Downloading: " + Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getAbsolutePath());

        //Setting title of request
        request.setTitle(filename);

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory

        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, filename);

        //Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);

        Button DownloadStatus = (Button) v.findViewById(R.id.checkTrackBttn);
        DownloadStatus.setEnabled(true);
        Button CancelDownload = (Button) v.findViewById(R.id.cancelTrackBttn);
        CancelDownload.setEnabled(true);
        Button Download = (Button) v.findViewById(R.id.downloadTrackBttn);
        Download.setEnabled(false);

        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @SuppressLint("StaticFieldLeak")
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadMusic.setEnabled(true);
            CancelDownload.setEnabled(false);
            DownloadStatus.setEnabled(false);
            if (cancel) {
                cancel = false;
                Toast toast = Toast.makeText(getContext(),
                        "Music Download Canceled", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                Log.d("Download", "Cancel");
            } else {
                Toast toast = Toast.makeText(getContext(),
                        "Music Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                Log.d("Download", "Complete");

                final DatabaseHandler handler = ((BrowseActivity) getActivity()).getDbHandler();
                String location = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getAbsolutePath();
                if ( !location.endsWith(File.separator) ) {
                    location += File.separator;
                }

                if (ex.equals("zip")) {
                    final DecompressHandler decompressHandler = new DecompressHandler(filename);
                    Log.d("Download", "Zip");
                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(Void... files) {
                            try {
                                decompressHandler.unzip();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        protected void onPostExecute(Void result) {
                            ArrayList<Uri> musicFile = decompressHandler.getMusicFile();
                            Log.d("URI HELP", "Amount of music in list: " + Integer.toString(musicFile.size()));
                            for (Uri uri : musicFile) {handler.postTrackToStorage(uri);}
                        }
                    }.execute();
                } else {
                    Log.d("Download", "Music");
                    Uri uriForfile = (Uri.parse(new File(location + filename).toString()));
                    handler.postTrackToStorage(uriForfile);
                }
            }
        }
    };

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            DownloadManager.Query MusicDownloadQuery = new DownloadManager.Query();
            //set the query filter to our previously Enqueued download
            MusicDownloadQuery.setFilterById(Music_DownloadId);
            Cursor cursor = downloadManager.query(MusicDownloadQuery);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        Log.d("Download", "File Name Got");
        return result;
    }
}
