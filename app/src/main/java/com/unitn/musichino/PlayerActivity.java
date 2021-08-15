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

import android.content.DialogInterface;
import android.net.sip.SipSession;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;

import java.util.List;
import java.util.Objects;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity
        implements  Player.EventListener {

  private PlayerView playerView;
  private MixMeExoPlayer mixplayer;
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



  private ConcatenatingMediaSource concatenatingMediaSource;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    viewPager = findViewById(R.id.pager);
    pagerAdapter = new ScreenSlidePagerAdapter(this);
    viewPager.setAdapter(pagerAdapter);



    playerView = findViewById(R.id.video_view);
    volumeSeekBar = findViewById(R.id.seekBarVol);
    volumeSeekBar2 = findViewById(R.id.seekBarVol2);
    volumeSeekBar3 = findViewById(R.id.seekBarVol3);
    volumeSeekBar4 = findViewById(R.id.seekBarVol4);
    volumeSeekBar5 = findViewById(R.id.seekBarVol5);
    volumeSeekBar6 = findViewById(R.id.seekBarVol6);
    volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
          float value = i / (float)seekBar.getMax();
          //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
          MediaCodecAudioRenderer renderer = mixplayer.renderers.get(0);
          mixplayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

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
          MediaCodecAudioRenderer renderer = mixplayer.renderers.get(1);
          mixplayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

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
          MediaCodecAudioRenderer renderer = mixplayer.renderers.get(2);
          mixplayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

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
          MediaCodecAudioRenderer renderer = mixplayer.renderers.get(3);
          mixplayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

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
          MediaCodecAudioRenderer renderer = mixplayer.renderers.get(4);
          mixplayer.player.createMessage(renderer).setType(Renderer.MSG_SET_VOLUME).setPayload(value).send();

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
          MediaCodecAudioRenderer renderer = mixplayer.renderers.get(5);
          mixplayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

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

  @Override
  public void onStart() {
    super.onStart();
    mixplayer = new MixMeExoPlayer( this, 1);
  //  mixplayer2 = new MixMeExoPlayer( this, 2);
  //  mixplayer3 = new MixMeExoPlayer( this, 3);
  //  mixplayer4 = new MixMeExoPlayer( this, 4);
  //  mixplayer5 = new MixMeExoPlayer( this, 5);
 //   mixplayer6 = new MixMeExoPlayer( this, 6);
    mixplayer.player.seekTo(currentWindow, playbackPosition);
    playerView.setPlayer(mixplayer.player);
    mixplayer.startPlaying(Uri.parse("asset:///logan3.mp4"));
   // mixplayer2.startPlaying(Uri.parse("asset:///logan3.mp4"));
   // mixplayer3.startPlaying(Uri.parse("asset:///logan3.mp4"));
   // mixplayer4.startPlaying(Uri.parse("asset:///logan3.mp4"));
   // mixplayer5.startPlaying(Uri.parse("asset:///logan3.mp4"));
   // mixplayer6.startPlaying(Uri.parse("asset:///logan3.mp4"));
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
    mixplayer.stopPlaying();
   // mixplayer2.stopPlaying();
   // mixplayer3.stopPlaying();
   // mixplayer4.stopPlaying();
   // mixplayer5.stopPlaying();
   // mixplayer6.stopPlaying();

  }



  @SuppressLint("InlinedApi")
  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

}
