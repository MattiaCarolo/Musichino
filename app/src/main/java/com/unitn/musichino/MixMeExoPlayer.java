package com.unitn.musichino;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MixMeExoPlayer
            implements Player.EventListener{
    private final Context context;


    private DataSource.Factory dataSourceFactory;
    public SimpleExoPlayer player;
    public MultiAudioTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    public List<MediaCodecAudioRenderer> renderers;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ProgressiveMediaSource.Factory progressiveMediaSourceFactory;
    private int trackIndex = 1;
    private ConcatenatingMediaSource concatenatingMediaSource;
    private int currentMediaPlayerIndex = 0;
    private boolean hasLyrics = false;
    private boolean lyricsOn = false;
    Equalizer mEqualizer;

    public MixMeExoPlayer(Context context, int index) {
        this.context = context;
        this.currentMediaPlayerIndex = index;
        System.out.println("Created player index: " + index);
        dataSourceFactory = new DefaultDataSourceFactory(context.getApplicationContext());
        renderers = new ArrayList<>();
       // initPlayer();

    }


    private void initPlayer(int trackCount) {
        if(player==null) {
             trackSelector = new MultiAudioTrackSelector();
            MultiTrackRenderersFactory renderersFactory = new MultiTrackRenderersFactory(trackCount, context, this);
            player = new SimpleExoPlayer.Builder(context.getApplicationContext(), renderersFactory)
                    .setTrackSelector(trackSelector)
                    .build();
            player.setPlayWhenReady(playWhenReady);
            trackIndex++;
            player.addListener(this);

        }
        concatenatingMediaSource = new ConcatenatingMediaSource();



    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }


        public void startPlaying(Uri uri) throws IOException {
            progressiveMediaSourceFactory = new ProgressiveMediaSource.Factory(dataSourceFactory);
            MediaSource mediaSource = progressiveMediaSourceFactory.createMediaSource(MediaItem.fromUri(uri));
            MediaExtractor extractor = new MediaExtractor();
            final AssetFileDescriptor afd=context.getAssets().openFd(uri.getLastPathSegment());
            extractor.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            int numTracks = extractor.getTrackCount();
            Log.d("extractorNumTrak", ""+numTracks);
            int numAudioTracks = 0;
            extractor.release();
            initPlayer(numTracks);
            player.setMediaSource(mediaSource, true);
            player.prepare();
            player.getAudioSessionId();
        }


        public void addToQ(Uri uri) {
            if (player == null) return;
            MediaSource mediaSource = progressiveMediaSourceFactory.createMediaSource(MediaItem.fromUri(uri));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }

        public void setAudioTrack(int track, final DefaultTrackSelector trackSelector) {
            System.out.println("setAudioTrack: "  + currentMediaPlayerIndex);
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();
            DefaultTrackSelector.ParametersBuilder builder = parameters.buildUpon();
            for (int rendererIndex = 0; rendererIndex < mappedTrackInfo.getRendererCount(); rendererIndex++) {
                int trackType = mappedTrackInfo.getRendererType(rendererIndex);
                if (trackType == C.TRACK_TYPE_AUDIO) {
                    builder.clearSelectionOverrides(rendererIndex).setRendererDisabled(rendererIndex, false);
                    int groupIndex = currentMediaPlayerIndex-1;
                    int [] tracks = {0};
                    DefaultTrackSelector.SelectionOverride override = new DefaultTrackSelector.SelectionOverride(groupIndex,tracks);
                    builder.setSelectionOverride(rendererIndex, mappedTrackInfo.getTrackGroups(rendererIndex), override);
                }
            }
            trackSelector.setParameters(builder);

            currentMediaPlayerIndex = track;
        }

    public void stopPlaying() {
        if (player != null) {
            player.stop(true);
            concatenatingMediaSource.clear();
        }
    }

    public void addRenderer(MediaCodecAudioRenderer renderer){
        renderers.add(renderer);
    }

    public long getCurrentPosition() {
        if (player != null)
            return player.getCurrentPosition();
        return 0;
    }
        @Override
        public void onPlaybackStateChanged(@Player.State int state){
          //  if(state == Player.STATE_READY){
              //  setAudioTrack(3, trackSelector);
          //  }
        }

    public void toggleLyrics(Context context){
        // TODO find a way to toggle lyrics
    }



    }



