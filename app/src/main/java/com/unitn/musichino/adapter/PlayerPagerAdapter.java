package com.unitn.musichino.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.unitn.musichino.ui.player.FragmentPlayerHome;
import com.unitn.musichino.ui.player.FragmentPlayerLyrics;
import com.unitn.musichino.ui.player.FragmentPlayerSettings;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
