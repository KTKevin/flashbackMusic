package com.example.rafael.flashback.adapters;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.rafael.flashback.fragments.AlbumFragment;
import com.example.rafael.flashback.fragments.DownloadFragment;
import com.example.rafael.flashback.fragments.FlashbackFragment;
import com.example.rafael.flashback.fragments.TimeFragment;
import com.example.rafael.flashback.fragments.TrackFragment;
import com.example.rafael.flashback.fragments.UpcomingFragment;
import com.example.rafael.flashback.fragments.VibeModeFragment;

/**
 * Adapter in charge of switching out between our track and album fragments.
 */

public class BrowsePagerAdapter extends FragmentPagerAdapter {

    public String[] fragTitles = new String[] {"Tracks", "Albums" /*, "Flashback"*/, "Download" /*, "VibeMode"*/, "Upcoming", "Time"};
    public SparseArray<Fragment> frags = new SparseArray<Fragment>();

    public BrowsePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        frags.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        frags.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        Bundle args = new Bundle();
        switch (i) {
            case 0:
                fragment = new TrackFragment();
                args.putInt(TrackFragment.ARG_OBJECT, i + 1);
                break;
            case 1:
                fragment = new AlbumFragment();
                args.putInt(AlbumFragment.ARG_OBJECT, i + 1);
                break;
            case 2:
                fragment = new DownloadFragment();
                break;
            case 3:
                fragment = new UpcomingFragment();
                break;
            case 4:
                fragment = new TimeFragment();
                break;
            default:
                fragment = null;
                break;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragTitles.length;
    }

    @Override
    public String getPageTitle(int position) {
        return fragTitles[position];
    }

}
