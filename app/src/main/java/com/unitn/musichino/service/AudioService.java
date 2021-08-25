package com.unitn.musichino.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.MultiAudioTrackSelector;
import com.unitn.musichino.MultiTrackRenderersFactory;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;

import java.util.List;

import androidx.annotation.Nullable;

public class AudioService extends Service {


    private final IBinder mBinder = new LocalBinder();
    private SimpleExoPlayer player;
    private AudioModel item;
    private PlayerNotificationManager playerNotificationManager;
    private MultiAudioTrackSelector trackSelector;
    private DataSource.Factory dataSourceFactory;
    private ProgressiveMediaSource.Factory progressiveMediaSourceFactory;
    public List<MediaCodecAudioRenderer> renderers;
    private final int trackCount = 10;
    private static final String CHANNEL_ID = "playback_channel";
    private static final int NOTIFICATION_ID = 1;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (player != null) {
            playerNotificationManager.setPlayer(null);
            player.release();
            player = null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d("PLAYER", "binding, intent: " +intent.getBundleExtra("bundle").getParcelable("item").toString());
        return mBinder;
    }

    public SimpleExoPlayer getplayerInstance() {
        if (player == null) {
            Log.d("PLAYER", "creating new instance");
            startPlayer();
        }
        return player;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getBundleExtra("bundle");
        if (b != null) {
            item = b.getParcelable("item");
            Log.d("ITEM", "received path: " + item.getPath());
        }
        if (player == null) {
            startPlayer();
        }
        return START_STICKY;
    }


    private void startPlayer() {
        Log.d("STARTEXO", "starting");
        final Context context = this;
        trackSelector = new MultiAudioTrackSelector();
        MultiTrackRenderersFactory renderersFactory = new MultiTrackRenderersFactory(trackCount, context, this);
        player = new SimpleExoPlayer.Builder(context.getApplicationContext(), renderersFactory)
                .setTrackSelector(trackSelector)
                .build();

        player.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                Log.d("ARTWORK", mediaMetadata.artworkDataType + "");
            }
        });
        progressiveMediaSourceFactory = new ProgressiveMediaSource.Factory(dataSourceFactory);
        MediaSource mediaSource = progressiveMediaSourceFactory.createMediaSource(MediaItem.fromUri(item.getPath()));
        player.setPlayWhenReady(true);
        player.setMediaSource(mediaSource, true);
        player.prepare();

        DefaultMediaDescriptionAdapter descriptionAdapter =  new DefaultMediaDescriptionAdapter(
                PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(this, PlayerActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));
                playerNotificationManager = new PlayerNotificationManager.Builder(
                        context,
                        NOTIFICATION_ID,
                        CHANNEL_ID
                )
                        .setMediaDescriptionAdapter(descriptionAdapter)
                        .setNotificationListener(new PlayerNotificationManager.NotificationListener() {

                            @Override
                            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                                stopSelf();
                            }

                            @Override
                            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                                startForeground(notificationId, notification);
                                Log.d("NOTIFICATION", "ID: "+ notificationId + ", notification: " +notification.category);
                            }
                        })
                        .build();
                playerNotificationManager.setPlayer(player);
            }


    public void addRenderer(MediaCodecAudioRenderer renderer){
        renderers.add(renderer);
    }


    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }
}
