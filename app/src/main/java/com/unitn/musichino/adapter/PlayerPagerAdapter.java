package com.unitn.musichino.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;
/*
    Adapter per il player
    All'interno evoca FragmentPlayerHome, FragmentPlayerSettings e FragmentPlayerLyrics
 */

public class PlayerPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;

    public PlayerPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> mFragments) {
        super(fragmentActivity);
        this.mFragments = mFragments;
    }
    @NonNull @Override public Fragment createFragment(int position) {
        return mFragments.get(position);
    }
    @Override public int getItemCount() {
        return mFragments.size();
    }
}
