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

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Util;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.adapter.PlayerPagerAdapter;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.ui.player.home.FragmentPlayerHome;
import com.unitn.musichino.ui.player.lyrics.FragmentPlayerLyrics;
import com.unitn.musichino.ui.player.Settings.FragmentPlayerSettings;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity
        implements  Player.EventListener {

  private static final String CHANNEL_ID = "playback_channel";
  private static final int NOTIFICATION_ID = 1;
  private PlayerView playerView;
  private SimpleExoPlayer player;
  private MixMeExoPlayer mixMePlayer;
  private List<SeekBar> seekBars;
  private SeekBar volumeSeekBar;
  private SeekBar volumeSeekBar2;
  private SeekBar volumeSeekBar3;
  private SeekBar volumeSeekBar4;
  private SeekBar volumeSeekBar5;
  private SeekBar volumeSeekBar6;
  private static final int NUM_PAGES = 5;
  private ViewPager2 viewPager;
  private FragmentStateAdapter pagerAdapter;
  FragmentContainerView equalizerFrame;
  Button equalizerButton;
  Context context;
  Equalizer mEqualizer;
  String fileName;
  PlayerNotificationManager playerNotificationManager;
  private String mUrl, mTitle, mSummary, mImage;
  public AudioService mService;
  private Intent intent;
  private String shareableLink;
  private boolean mBound = false;
  AudioModel item;

  private ConcatenatingMediaSource concatenatingMediaSource;


  private ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      AudioService.LocalBinder binder = (AudioService.LocalBinder) iBinder;
      mService = binder.getService();
      mBound = true;
      initializePlayer();
      if(mService.currentlyPlaying != null){
        Log.d("onServiceConnected", "Currently playing: " + mService.currentlyPlaying.getPath());
        mService.changeSong(Uri.parse(item.getPath()));
      }
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
    Bundle b = getIntent().getBundleExtra("bundle");
    if (b != null) {
      item = b.getParcelable("item");
      shareableLink = b.getString("share_key");
      mUrl = item.getPath();
      mTitle = item.getName();
      intent = new Intent(this, AudioService.class);
      Bundle serviceBundle = new Bundle();
      serviceBundle.putParcelable("item", item);
      intent.putExtra("bundle", serviceBundle);

      try{
        Util.startForegroundService(this, intent);
      }catch(Exception ex){
          Log.d("Exception service", ex.toString());
      }

      playerView.setUseController(true);
      playerView.showController();
      playerView.setControllerAutoShow(true);
      playerView.setControllerHideOnTouch(false);
    }

    Log.d("fileName: ", mUrl);
    viewPager = findViewById(R.id.pgr_MediaPlayer);
    if(mBound){
      Log.d("BOUND", "bindato");
    }
  }


  private void initializePlayer() {
    if (mBound) {
      player = mService.getplayerInstance();
      playerView.setPlayer(player);
    }
  }

  public SimpleExoPlayer getPlayer(){
    return player;
  }

  @Override
  public void onStart() {
    super.onStart();
    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    initializePlayer();
    playerView.getSubtitleView().setVisibility(View.INVISIBLE);
    List<Fragment> fragments = new ArrayList<>();
    fragments.add(Fragment.instantiate(this, FragmentPlayerSettings.class.getName()));
    fragments.add(Fragment.instantiate(this, FragmentPlayerHome.class.getName()));
    fragments.add(Fragment.instantiate(this, FragmentPlayerLyrics.class.getName()));
    pagerAdapter = new PlayerPagerAdapter(this,fragments);
    viewPager.setAdapter(pagerAdapter);
  //  setUI();
  }

  /*
      mixMePlayer.player.setAudioDebugListener(new AudioRendererEventListener() {

        @Override
        public void onAudioSessionId(int audioSessionId) {
          SharedPreferences preferences = context.getSharedPreferences("equalizer", 0);
          mEqualizer = new Equalizer(1000, audioSessionId);
          mEqualizer.setEnabled(true);
          //That's it, this will initialize the Equalizer and set it to the //default preset
          int current = preferences.getInt("position", 0);
          if (current == 0) {
            for (short seek_id = 0; seek_id < mEqualizer.getNumberOfBands(); seek_id++) {
              int progressBar = preferences.getInt("seek_" + seek_id, 1500);
              short equalizerBandIndex = (short) (seek_id);
              final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
              Log.i("seek_" + seek_id, ":" + progressBar);
              if (progressBar != 1500) {
                mEqualizer.setBandLevel(equalizerBandIndex,
                        (short) (progressBar + lowerEqualizerBandLevel));
              } else {
                //First time default 1500Hz
                mEqualizer.setBandLevel(equalizerBandIndex,
                        (short) (progressBar + lowerEqualizerBandLevel));
              }
            }
          } else {
            mEqualizer.usePreset((short) (current - 1));
          }    }
      });

    }
  */
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
   // unbindService(mConnection);
   // mBound = false;
    super.onStop();
  }

 @Override
 public void onDestroy(){
    super.onDestroy();
    //mService.getplayerInstance().release();
    //playerNotificationManager.setPlayer(null);
   unbindService(mConnection);
 }

}
