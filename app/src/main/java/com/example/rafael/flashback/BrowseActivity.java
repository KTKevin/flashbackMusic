package com.example.rafael.flashback;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rafael.flashback.adapters.BrowsePagerAdapter;
import com.example.rafael.flashback.adapters.FlashbackAdapter;
import com.example.rafael.flashback.adapters.PeopleAdapter;
import com.example.rafael.flashback.adapters.TrackAdapter;
import com.example.rafael.flashback.fragments.AlbumFragment;
import com.example.rafael.flashback.fragments.DownloadFragment;
import com.example.rafael.flashback.fragments.TrackFragment;
import com.example.rafael.flashback.fragments.UpcomingFragment;
import com.example.rafael.flashback.fragments.VibeModeFragment;
import com.example.rafael.flashback.handlers.DatabaseHandler;
import com.example.rafael.flashback.handlers.FilterHandler;
import com.example.rafael.flashback.handlers.LastPlayedHandler;
import com.example.rafael.flashback.handlers.VibeModeHandler;
import com.example.rafael.flashback.utils.DownloadScanner;
import com.example.rafael.flashback.utils.NameGenerator;
import com.example.rafael.flashback.utils.Parser;
import com.google.api.services.people.v1.People;
import com.google.firebase.database.DatabaseReference;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;

/**
 * Activity in charge of browsing tracks and albums.
 */

public class BrowseActivity extends AppCompatActivity  {

    // UI layouts
    private Toolbar toolbar;

    private FrameLayout frameLayout;
    private Fragment vibeModeFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabLayout.Tab trackFragmentTab;
    private TabLayout.Tab albumFragmentTab;
    private TabLayout.Tab downloadFragmentTab;
    private TabLayout.Tab upcomingFragmentTab;
    private TrackFragment trackFragment;
    private AlbumFragment albumFragment;
    private DownloadFragment downloadFragment;
    private UpcomingFragment upcomingFragment;

    // player panel
    private MediaPlayer player;
    private ImageButton barPlayButton;
    private ImageButton barNextButton;
    private ImageButton openPlayerButton;

    private DatabaseHandler dbHandler;
    private FlashBackpriority fbPriority;
    private LastPlayedHandler lastPlayedHandler;
    private VibeModeHandler vibeModeHandler;
    private Parser parser;
    private DownloadScanner scanner;
    private ImageView coverArt;
    private TextView lastPlayedLocTV, lastPlayedDateTV, lastPlayedTimeTV, lastPlayedPersonTV;
    private boolean isFlashbackOn;
    private boolean isVibeMode;
    public BrowsePagerAdapter adapter;
    public ArrayList<Track> upcomingTracks;
    public PeopleAdapter peopleAdapter;
    private String defaultDateStr, defaultTimeStr, defaultLocStr, defaultPerStr;
    TextView trackInfoTV;

    public boolean stuck = false;
    public int pIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);
        Bundle data = getIntent().getExtras();
        peopleAdapter = (PeopleAdapter) data.getParcelable("user");
        Log.d("user", peopleAdapter.toString());

        ActivityCompat.requestPermissions(BrowseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

        // Create a new database handler
        dbHandler = new DatabaseHandler(this, peopleAdapter);
        lastPlayedHandler = new LastPlayedHandler(this);
        // Create the global parser that will be used by the track and album fragment
        parser = new Parser();
        //parser.parse(this);
        parser.parseDownload(this);
        scanner = new DownloadScanner();

        vibeModeHandler = new VibeModeHandler(this, peopleAdapter);
        //Declare the ui items related to the maximized player
        final LinearLayout maxPlayerLayout = findViewById(R.id.maximized_player);
        //final FrameLayout playerSizeLayout = findViewById(R.id.player_size);
        //final FrameLayout barPlayerLayout = findViewById(R.id.bar_player);


        // title and menu layout
        initToolbar();

        // tabs and 3 fragments in normal mode
        initViewPager();  // need to be ahead
        initTabLayout();


        // player panel
        initPlayer();
   }

   public void refresh(){
        //parser.parse(this);
        parser.parseDownload(this);
   }
    private void initPlayer() {
        // init Views
        trackInfoTV = findViewById(R.id.track_info);
        coverArt = findViewById(R.id.cover_art);
        lastPlayedDateTV = findViewById(R.id.last_played_date);
        lastPlayedTimeTV = findViewById(R.id.last_played_time);
        lastPlayedLocTV =  findViewById(R.id.last_played_loc);
        lastPlayedPersonTV = findViewById(R.id.last_played_person);

        //Set default strings for last played date
        defaultDateStr = String.format(getString(R.string.date), getString(R.string.na));
        defaultTimeStr = String.format(getString(R.string.time), getString(R.string.na));
        defaultLocStr = String.format(getString(R.string.loc), getString(R.string.na));
        defaultPerStr = String.format(getString(R.string.per), getString(R.string.na));
        lastPlayedDateTV.setText(defaultDateStr);
        lastPlayedTimeTV.setText(defaultTimeStr);
        lastPlayedLocTV.setText(defaultLocStr);
        lastPlayedPersonTV.setText(defaultPerStr);

        // SlidingUpPanel
        final SlidingUpPanelLayout slider = findViewById(R.id.sliding_layout);
        slider.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {}
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    openPlayerButton.setImageResource(R.drawable.ic_down_icon);
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    openPlayerButton.setImageResource(R.drawable.ic_up_icon);
                } else {
                    Log.d("slidingPanel", "onPanelStateChanged: unhandled panel state");
                }
            }
        });
        openPlayerButton = findViewById(R.id.up_button);
        openPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slider.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else if (slider.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    Log.d("slidingPanel", "onClick: unhandled panel state");
                }
            }
        });

        // playButton
        barPlayButton = findViewById(R.id.bar_play_button);
        barPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player == null) return;
                if (player.isPlaying()) {
                    pausePlayer();
                    barPlayButton.setImageResource(R.drawable.ic_play_icon);
                } else {
                    playPlayer();
                    barPlayButton.setImageResource(R.drawable.ic_pause_icon);
                }
            }
        });

        // nextButton
        barNextButton = findViewById(R.id.bar_next_button);
        barNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player == null) return;
                // skip the current song
                stopPlayer();
                playPlayer();
            }
        });

    }

    public void initViewPager() {
        // Add a viewpager with its adapter to manage switching fragments
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new BrowsePagerAdapter(getSupportFragmentManager());
        //adapter.setActivity(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        /*
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 viewPager.setCurrentItem(tab.getPosition());
             }
             @Override
             public void onTabUnselected(TabLayout.Tab tab) {}
             @Override
             public void onTabReselected(TabLayout.Tab tab) {}
        });
        */
    }
    public void refreshViewPager(){
        adapter = new BrowsePagerAdapter(getSupportFragmentManager());
        //adapter.setActivity(this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    public void initTabLayout() {
        // Set up tabs and corresponding names
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //trackFragmentTab = tabLayout.getTabAt(0);
        //albumFragmentTab = tabLayout.getTabAt(1);
        //downloadFragmentTab = tabLayout.getTabAt(2);
        //upcomingFragmentTab = tabLayout.getTabAt(3);

        //tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        //tabLayout.addTab(tabLayout.newTab().setText("Albums"));
        //tabLayout.addTab(tabLayout.newTab().setText("Flashback"));
        //tabLayout.addTab(tabLayout.newTab().setText("Download"));
        //tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        //tabLayout.addTab(tabLayout.newTab().setText("VibeMode"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    public void playTrack(final Track track, final ArrayList<Track> tracks, final boolean flashback) {
        Log.d("UPCOMINGTRACKS", "playTrack: PLAYING");
        pIndex = 0;
        stuck = false;
        if (player == null) {
            player = new MediaPlayer();
        }
        resetPlayer();
        upcomingTracks = new ArrayList<Track>();
        if (tracks != null) {
            for (int i = 1; i < tracks.size(); i++) {
                if (tracks.get(i).getPreference() != -1) {
                    upcomingTracks.add(tracks.get(i));
                }
            }
            for (; pIndex < tracks.size(); pIndex++) {
                if (tracks.get(pIndex).getId() != null) {
                    break;
                }
            }
        }
        Log.d("UPCOMINGTRACKS", "playTrack: PINDEX" + pIndex);

        // update fragment to show the updated list
        upcomingFragment = (UpcomingFragment) getSupportFragmentManager().getFragments().get(3);
        upcomingFragment.getUpcomingAdapter().setTracks(upcomingTracks);
        upcomingFragment.getUpcomingAdapter().notifyDataSetChanged();

        // Debug for checking upcoming tracks
        for (int i = 0; i < upcomingTracks.size(); i++) {
            Log.d("UPCOMINGTRACKS", i + ": " + upcomingTracks.get(i).getName());
        }

        try {
            if (tracks != null) {
                if (pIndex == tracks.size()) {
                    stuck = true;
                    Log.d("UPCOMINGTRACKS", "playTrack: STUCK");
                } else {
                    player.setDataSource(this, tracks.get(pIndex).getId());
                    player.prepare();
                    displayTrackInfo(tracks.get(pIndex));
                    playPlayer();
                    lastPlayedHandler.changeLastPlayed(tracks.get(pIndex));
                    dbHandler.postTrackDataToDB(tracks.get(pIndex));
                }
            } else {
                player.setDataSource(this, track.getId());
                player.prepare();
                displayTrackInfo(track);
                playPlayer();
                lastPlayedHandler.changeLastPlayed(track);
                dbHandler.postTrackDataToDB(track);
            }


            //TODO: MOVE THIS BACK THIS SHOULD HAPPEN AFTER TRACK HAS PLAYED

        } catch (Exception e) {
            Log.d("PLAYERERROR", e.toString());
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (tracks != null && tracks.size() > 1) {

                    ArrayList<Track> temp = new ArrayList<Track>();
                    for (int i = 0; i < tracks.size(); i++) {
                        if (tracks.get(i).getPreference() != -1 && i != pIndex) {
                            temp.add(tracks.get(i));
                        }
                    }
                    if (temp != null && temp.size() >= 1) {
                        playTrack(temp.get(0), temp, flashback);
                    } else {
                        resetPlayerUI();
                    }
                } else {
                    resetPlayerUI();
                }
            }
        });


   }

   public void displayTrackInfo(Track track) {
        trackInfoTV.setText(track.getName() + " - " + track.getAlbum());
        if (track.getLastPlayed() != null && !track.getLastPlayed().equals("")) {
            String lastPlayed = track.getLastPlayed();
            String time = lastPlayed.substring(8, 10) + ":" + lastPlayed.substring(10, 12) + ":" + lastPlayed.substring(12, 14);
            String dateStr = lastPlayed.substring(4, 6) + "/" + lastPlayed.substring(6,8) + "/" + lastPlayed.substring(0, 4);
            lastPlayedDateTV.setText(String.format(getString(R.string.date), dateStr));
            lastPlayedTimeTV.setText(String.format(getString(R.string.time), time));
            lastPlayedLocTV.setText(String.format(getString(R.string.loc), track.getLocationName()));
            if (track.getLastPlayedUser().equals(peopleAdapter.getUserEmail())) {
                lastPlayedPersonTV.setText(Html.fromHtml(getString(R.string.per) + "<i>" + track.getPerson() + "</i>"));
                //lastPlayedPersonTV.setText(String.format(getString(R.string.per), track.getPerson()));
            } else if (isFriend(track.getLastPlayedUser()) != null) {
                lastPlayedPersonTV.setText(String.format(getString(R.string.per), isFriend(track.getLastPlayedUser())));
            } else if (track.getLastPlayedUser() != null){
                NameGenerator nameGenerator = new NameGenerator();
                lastPlayedPersonTV.setText(String.format(getString(R.string.per), nameGenerator.proxy(track.getLastPlayedUser())));
            } else {
                lastPlayedPersonTV.setText(String.format(getString(R.string.per), "N/A"));
            }

        }

        byte[] coverData = track.getAlbumcover();
        if (coverData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(coverData, 0, coverData.length);
            coverArt.setImageBitmap(bitmap);
        }

   }

   public String isFriend(String email) {
        if (peopleAdapter.getEmails() != null) {
            for (int i = 0; i < peopleAdapter.getEmails().size(); i++) {
                if (email.equals(peopleAdapter.getEmails().get(i))) {
                    return peopleAdapter.getNames().get(i);
                }
            }
        }
        return null;
   }

   public void resetPlayerUI() {
        trackInfoTV.setText("No Song Playing");
        barPlayButton.setImageResource(player.isPlaying()?
                R.drawable.ic_pause_icon : R.drawable.ic_play_icon);
        lastPlayedDateTV.setText(defaultDateStr);
        lastPlayedTimeTV.setText(defaultTimeStr);
        lastPlayedLocTV.setText(defaultLocStr);
        lastPlayedPersonTV.setText(defaultPerStr);
        coverArt.setImageResource(android.R.color.transparent);
   }

   public void playPlayer() {
        player.start();
   }

   public void pausePlayer() {
        if (player.isPlaying()) {
            player.pause();
        }
   }

   public  void stopPlayer(){
        if(player.isPlaying()){
            player.stop();
        }
   }
   public void resetPlayer() {
        // Reset the player to play new song
        if (player.isPlaying()) {
            player.stop();
            player.reset();
        }
        else if (!player.isPlaying() && player.getCurrentPosition() > 1) {
            player.stop();
            player.reset();
            //barPlayButton.setVisibility(View.INVISIBLE);
            //barPauseButton.setVisibility(View.VISIBLE);
            barPlayButton.setImageResource(R.drawable.ic_pause_icon);
        }
   }
   @Override
    public void onDestroy() {
        super.onDestroy();
        //player.release();
   }


   public Fragment getCurrentFragment() {
        int currItem = viewPager.getCurrentItem();
        return (Fragment) adapter.instantiateItem(viewPager, currItem);
   }

   public Parser getParser() {
        return parser;
    }

   public VibeModeHandler getVibeModeHandler() {
       return vibeModeHandler;
   }

   public void setFlashback(boolean isOn) {
        isFlashbackOn = isOn;
   }

   public boolean isFlashbackOn() {
        return isFlashbackOn;
   }


    public boolean isVibeMode() {
        return isVibeMode;
    }

    public void setVibeMode(boolean vibeMode) {
        isVibeMode = vibeMode;
    }

    /**************************************************************************
     *                                  toolbar
     * initToolbar()
     * onCreateOptionsMenu(Menu menu)
     * onOptionsItemSelected(MenuItem item)
     * switchBrowseMode(boolean isVibeMode)
     **************************************************************************/

    //helper
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_browse);
        toolbar.setTitle(isVibeMode? "Vibe Mode" : "Normal Mode");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FilterHandler filter = new FilterHandler();
        TrackAdapter trackAdapter;
        String msg;

        switch (item.getItemId()) {
             case R.id.action_vibemode:
                 setVibeMode(!isVibeMode);
                 item.setChecked(isVibeMode);
                 item.setIcon(isVibeMode?
                          R.drawable.ic_surround_sound_black_24dp :
                          R.drawable.ic_surround_sound_white_24dp);
                 // switch the mode, replacing the fragment in the frameLayout
                 switchBrowseMode(isVibeMode);
                 msg = isVibeMode? "Vibe Mode is Switched On" : "Vibe Mode is Switched Off";
                 break;
             case R.id.menu_sort_by_title:
                 trackFragment = (TrackFragment) getSupportFragmentManager().getFragments().get(0);
                 trackAdapter = trackFragment.getTrackAdapter();
                 trackAdapter.updateData(filter.filterList(trackAdapter.getTracks(), "title"));
                 msg = "sort by title";
                 break;
             case R.id.menu_sort_by_album:
                 trackFragment = (TrackFragment) getSupportFragmentManager().getFragments().get(0);
                 trackAdapter = trackFragment.getTrackAdapter();
                 trackAdapter.updateData(filter.filterList(trackAdapter.getTracks(), "album"));
                 msg = "sort by album";
                 break;
             case R.id.menu_sort_by_artist:
                 trackFragment = (TrackFragment) getSupportFragmentManager().getFragments().get(0);
                 trackAdapter = trackFragment.getTrackAdapter();
                 trackAdapter.updateData(filter.filterList(trackAdapter.getTracks(), "artist"));
                 msg = "sort by artist";
                 break;
             case R.id.menu_sort_by_favorite:
                 trackFragment = (TrackFragment) getSupportFragmentManager().getFragments().get(0);
                 trackAdapter = trackFragment.getTrackAdapter();
                 trackAdapter.updateData(filter.filterList(trackAdapter.getTracks(), "favorite"));
                 msg = "sort by favorite status";
                 break;
             case R.id.action_setting:
                 msg = "setting???";
                 break;
             default:
                 // If we got here, the user's action was not recognized.
                 // Invoke the superclass to handle it.
                 return super.onOptionsItemSelected(item);
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return true;
    }

    //helper
    private void switchBrowseMode(boolean isVibeMode) {
        // switch from normal mode to vibe mode
        if (isVibeMode) {
             ViewGroup.LayoutParams params = tabLayout.getLayoutParams();
             params.height = 0;
             //tabLayout.setVisibility(View.INVISIBLE);
             tabLayout.setEnabled(false);
             tabLayout.setClickable(false);

             vibeModeFragment = new VibeModeFragment();
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
             frameLayout = (FrameLayout) findViewById(R.id.frame_browse);
             frameLayout.removeAllViews();
             transaction.add(R.id.frame_browse, vibeModeFragment);
             transaction.commit();

             toolbar.setTitle("Vibe Mode");
        }
        // switch from vibe mode to normal mode
        else {
             ViewGroup.LayoutParams params = tabLayout.getLayoutParams();
             params.height = params.WRAP_CONTENT;
             //tabLayout.setVisibility(View.VISIBLE);
             tabLayout.setEnabled(true);
             tabLayout.setClickable(true);
             frameLayout = (FrameLayout) findViewById(R.id.frame_browse);
             frameLayout.addView(viewPager);
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
             transaction.detach(vibeModeFragment);
             transaction.commit();

             // update fragments to reflect changes from vibe mode
             resetFragmentUI();

             toolbar.setTitle("Normal Mode");
        }

        // refresh the player when switching modes
        if (player == null) {
            player = new MediaPlayer();
        }
        resetPlayer();
        resetPlayerUI();
    }

    private void resetFragmentUI() {
        Log.d("kaiwen: ", "in resetFragmentUI");
        parser.parseDownload(this);
        trackFragment = (TrackFragment) getSupportFragmentManager().getFragments().get(0);
        for (Track track: parser.getTrackList()) {
            Log.d("kaiwen: ", track.getName());
        }
        trackFragment.getTrackAdapter().notifyDataSetChanged();
        albumFragment = (AlbumFragment) getSupportFragmentManager().getFragments().get(1);
        albumFragment.getAlbumAdapter().notifyDataSetChanged();
    }

    public DatabaseHandler getDbHandler() {
        return dbHandler;
    }

    public DownloadScanner getScanner() {
        return scanner;
    }

}
