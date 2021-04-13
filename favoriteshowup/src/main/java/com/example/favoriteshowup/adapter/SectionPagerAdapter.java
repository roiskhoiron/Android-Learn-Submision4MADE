package com.example.favoriteshowup.adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.favoriteshowup.R;
import com.example.favoriteshowup.fragment.FavoriteMovieFragment;
import com.example.favoriteshowup.fragment.FavoriteTvShowFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionPagerAdapter";
    private final Context mContext;

    public SectionPagerAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Log.d(TAG, "SectionPagerAdapter: running");
        this.mContext = mContext;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_movies,
            R.string.tab_tvshow
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: running");
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FavoriteMovieFragment();
                break;
            case 1:
                fragment = new FavoriteTvShowFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: running");
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle: running");
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
