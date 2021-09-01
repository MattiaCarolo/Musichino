/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unitn.musichino;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.adapter.PlayerPagerAdapter;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.ui.player.Settings.FragmentPlayerSettings;
import com.unitn.musichino.ui.player.home.FragmentPlayerHome;
import com.unitn.musichino.ui.player.lyrics.FragmentPlayerLyrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

/*
    Activity di interazione con il player.
    Contiene il binding con il servizio del player, al quale passa gli item ricevuti dall'Intent.
    Completato il binding costruisce tutta la UI del player popolando i fragment tramite PlayerPageAdapter.
    I fragment usati sono FragmentPlayerHome, FragmentPlayerLyrics e FragmentPlayerSettings.
 */
public class PlayerActivity extends AppCompatActivity
        implements Player.EventListener {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ViewPager2 viewPager;
    public AudioService mService;
    private Intent intent;
    private boolean mBound = false;
    List<AudioModel> items;
    private Bundle b;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.LocalBinder binder = (AudioService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            initializePlayer();
            if (mService.currentlyPlaying != null) {
                //Log.d("onServiceConnected", "Currently playing: " + mService.currentlyPlaying.getPath());
                if (items.size() > 0)
                    mService.changeSong(items);
            }
            setUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerView = findViewById(R.id.video_view);
        b = getIntent().getBundleExtra("bundle");

        intent = new Intent(this, AudioService.class);
        if (b != null) {
            items = new ArrayList<>();
            items = b.getParcelableArrayList("items");
            Bundle serviceBundle = new Bundle();
            serviceBundle.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
            intent.putExtra("bundle", serviceBundle);
        }
        try {
            Util.startForegroundService(this, intent);
        } catch (Exception ex) {
            Log.d("Exception service", ex.toString());
        }

        playerView.setUseController(true);
        playerView.setControllerAutoShow(true);
        playerView.showController();
        playerView.setControllerHideOnTouch(false);
        playerView.setControllerShowTimeoutMs(-1);

        viewPager = findViewById(R.id.pgr_MediaPlayer);
    }


    private void initializePlayer() {
        if (mBound) {
            player = mService.getPlayerInstance();
            playerView.setPlayer(player);
        }
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        initializePlayer();
    }

    public void setUI() {
        if(b.getBoolean("pld_Playlist_item")){
            int pos = b.getInt("pld_Position");
            player.seekTo(pos,0);
        }
        Objects.requireNonNull(playerView.getSubtitleView()).setVisibility(View.INVISIBLE);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(Fragment.instantiate(this, FragmentPlayerSettings.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentPlayerHome.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentPlayerLyrics.class.getName()));
        FragmentStateAdapter pagerAdapter = new PlayerPagerAdapter(this, fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

}
