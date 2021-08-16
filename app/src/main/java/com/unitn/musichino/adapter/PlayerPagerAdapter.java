package com.unitn.musichino.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.unitn.musichino.ui.player.FragmentPlayerHome;
import com.unitn.musichino.ui.player.FragmentPlayerLyrics;
import com.unitn.musichino.ui.player.FragmentPlayerSettings;

import org.jetbrains.annotations.NotNull;

public class PlayerPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public PlayerPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragment.instantiate(context, FragmentPlayerSettings.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(context, FragmentPlayerHome.class.getName());
                break;
            case 2:
                fragment = Fragment.instantiate(context, FragmentPlayerLyrics.class.getName());
                break;          }
        return fragment;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
