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
import android.app.Notification;
import android.app.PendingIntent;
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

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Util;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.service.AudioService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;
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
  SubtitleView subtitleView;
  private MixMeExoPlayer mixMePlayer;
  private List<SeekBar> seekBars;
  private int totalAudioTracks = 6;                     // TODO dynamic audio track count
  private boolean playWhenReady = true;
  private int currentWindow = 0;
  private long playbackPosition = 0;
  private int trackIndex = 1;
  private int currentAudioTrack = 0;
  private Button selectTracksButton;
  private boolean isShowingTrackSelectionDialog;
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
  private AudioService mService;
  private Intent intent;
  private String shareableLink;
  private boolean mBound = false;

  private ConcatenatingMediaSource concatenatingMediaSource;


  private ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      AudioService.LocalBinder binder = (AudioService.LocalBinder) iBinder;
      mService = binder.getService();
      mBound = true;
      initializePlayer();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      mBound = false;
    }
  };



  @SuppressLint("MissingSuperCall")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    playerView = findViewById(R.id.video_view);
    Bundle b = getIntent().getBundleExtra("bundle");
    if (b != null) {
      AudioModel item = b.getParcelable("item");
      shareableLink = b.getString("share_key");
    //  mImage = item.getImage();
      mUrl = item.getPath();
      mTitle = item.getName();
   //   mSummary = item.getArtist();
      intent = new Intent(this, AudioService.class);
      Bundle serviceBundle = new Bundle();
      serviceBundle.putParcelable("item", item);
      intent.putExtra("bundle", serviceBundle);
      Util.startForegroundService(this, intent);
      playerView.setUseController(true);
      playerView.showController();
      playerView.setControllerAutoShow(true);
      playerView.setControllerHideOnTouch(false);
    }

   // Intent intent = getIntent();
  //  fileName =  intent.getStringExtra("fileName"); //  + "asset:///"
    Log.d("fileName: ", mUrl);
    viewPager = findViewById(R.id.pager);
   // pagerAdapter = new ScreenSlidePagerAdapter(this);
   // viewPager.setAdapter(pagerAdapter);



  //  mixMePlayer = new MixMeExoPlayer( this, 1);
  //  playerView = findViewById(R.id.video_view);
    subtitleView = findViewById(R.id.exo_subtitles_view);
    volumeSeekBar = findViewById(R.id.seekBarVol);
    volumeSeekBar2 = findViewById(R.id.seekBarVol2);
    volumeSeekBar3 = findViewById(R.id.seekBarVol3);
    volumeSeekBar4 = findViewById(R.id.seekBarVol4);
    volumeSeekBar5 = findViewById(R.id.seekBarVol5);
    volumeSeekBar6 = findViewById(R.id.seekBarVol6);



    /*
    equalizerButton = findViewById(R.id.equalizerButton);
    equalizerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (savedInstanceState == null) {
          getSupportFragmentManager().beginTransaction()
                  .setReorderingAllowed(true)
                  .add(R.id.equalizerFrame, EqualizerFragment.class, null)
                  .commit();
        }
        else{
          Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.equalizerLayout);
          getSupportFragmentManager().beginTransaction()
                  .setReorderingAllowed(true)
                  .remove(fragment)
                  .commit();
        }
      }
    });

     */
    // EQ




    volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(0);
          mixMePlayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    volumeSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(1);
          mixMePlayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    volumeSeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(2);
          mixMePlayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    volumeSeekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(3);
          mixMePlayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    volumeSeekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(4);
          mixMePlayer.player.createMessage(renderer).setType(Renderer.MSG_SET_VOLUME).setPayload(value).send();

        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    volumeSeekBar6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(5);
          mixMePlayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    //selectTracksButton = findViewById(R.id.select_tracks_button);
   // selectTracksButton.setOnClickListener( this);

  }



  private void initializePlayer() {
    if (mBound) {
      SimpleExoPlayer player = mService.getplayerInstance();
      playerView.setPlayer(player);

    }
  }

  @Override
  public void onStart() {
    super.onStart();
    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    initializePlayer();
  //  setUI();
  }
/*
  @Override
  public void onClick(View view) {
    if (view == selectTracksButton
        && !isShowingTrackSelectionDialog
        && TrackSelectionDialog.willHaveContent(mixplayer.trackSelector)) {
      isShowingTrackSelectionDialog = true;
      TrackSelectionDialog trackSelectionDialog =
          TrackSelectionDialog.createForTrackSelector(
              mixplayer.trackSelector,
              /* onDismissListener=  new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dismissedDialog) {
                      isShowingTrackSelectionDialog = false;
                    }
                  });
      trackSelectionDialog.show(getSupportFragmentManager(), /* tag=  null);
    }
  }

  private void updateButtonVisibility() {
    selectTracksButton.setEnabled(
            mixplayer.player != null && TrackSelectionDialog.willHaveContent(mixplayer.trackSelector));
  }

*/
/*
  @Override
  public void onStart() {
    super.onStart();
    if(mixMePlayer.player == null) {
      try {
        mixMePlayer.startPlaying(Uri.parse(fileName));
        //  mixMePlayer.player.seekTo(currentWindow, playbackPosition);
        playerView.setPlayer(mixMePlayer.player);
        playerView.getSubtitleView().setVisibility(View.INVISIBLE);
        subtitleView.setFixedTextSize(Dimension.DP, 69);

        mixMePlayer.player.addListener(new Player.Listener() {
            @Override
            public void onCues(List<Cue> cues) {
                if (cues.size() > 1) {
                    Log.d("CUES", "There are more than one cues available.");
                }

                if (subtitleView != null && !cues.isEmpty()) {
                    // Since an srt file will only ever have one cue we can afford to get only the first one.
                    subtitleView.setCues(cues);

                }
                else {
                    Log.d("CUES", "No cues found.");
                }
            }

        });

        playerNotificationManager.setPlayer(mixMePlayer.player);
      } catch (IOException | InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

 */
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
    unbindService(mConnection);
    mBound = false;
    super.onStop();
  }

 @Override
 public void onDestroy(){
    super.onDestroy();
    mixMePlayer.player.release();
   playerNotificationManager.setPlayer(null);
 }

}
