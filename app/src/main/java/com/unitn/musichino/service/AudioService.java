package com.unitn.musichino.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
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
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.MultiAudioTrackSelector;
import com.unitn.musichino.MultiTrackRenderersFactory;
import com.unitn.musichino.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class AudioService extends Service {


    private final IBinder mBinder = new LocalBinder();
    private SimpleExoPlayer player;
    private List<AudioModel> items;
    private PlayerNotificationManager playerNotificationManager;
    private MultiAudioTrackSelector trackSelector;
    private DataSource.Factory dataSourceFactory;
    private ProgressiveMediaSource.Factory progressiveMediaSourceFactory;
    public List<MediaCodecAudioRenderer> renderers;
    private final int trackCount = 10;
    private static final String CHANNEL_ID = "playback_channel";
    private static final int NOTIFICATION_ID = 1;
    public static final int IMPORTANCE_DEFAULT = 3;
    public AudioModel currentlyPlaying;
    Equalizer mEqualizer;


    @Override
    public void onCreate() {
        super.onCreate();
        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext());
        renderers = new ArrayList<>();
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

//        Log.d("PLAYER", "binding, intent: " +intent.getBundleExtra("bundle").getParcelable("item").toString());
        return mBinder;
    }

    public SimpleExoPlayer getplayerInstance() {
        if (player == null) {
            startPlayer();
        }
        return player;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getBundleExtra("bundle");
        if (b != null) {
            items = b.getParcelableArrayList("items");
        }
        if (player == null) {
            startPlayer();
        }
        return START_STICKY;
    }


    private void startPlayer() {
        final Context context = this;
        currentlyPlaying = items.get(0);
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

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                currentlyPlaying = items.get(player.getCurrentPeriodIndex());
            }
        });
        player.addListener(new Player.Listener() {



        });
        progressiveMediaSourceFactory = new ProgressiveMediaSource.Factory(dataSourceFactory);
        List<MediaItem> mediaItems = new ArrayList<>();
        for(AudioModel item : items) {
            Uri uri = Uri.parse(item.getPath());
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(uri)
                    .build();
            mediaItems.add(mediaItem);
        }
        Log.d("MEDIAITEMS", "set media items size: "+mediaItems.size());
        player.setMediaItems(mediaItems, true);
        player.prepare();
        player.setPlayWhenReady(true);
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
                        .setChannelDescriptionResourceId(2)
                        .setChannelImportance(0)
                        .setChannelNameResourceId(0)
                        .setNotificationListener(new PlayerNotificationManager.NotificationListener() {

                            @Override
                            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                                stopSelf();
                            }

                            @Override
                            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                                startForeground(notificationId, notification);
                               // Log.d("NOTIFICATION", "ID: "+ notificationId + ", notification: " +notification.category);
                            }
                        })
                        .build();
                playerNotificationManager.setPlayer(player);
                createNotificationChannel();




    }

    public void addRenderer(MediaCodecAudioRenderer renderer){
        renderers.add(renderer);
    }


    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification_channel";
            String description = "Musichino";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void changeSong(List<AudioModel> items){
            List<MediaItem> mediaItems = new ArrayList<>();
            for(AudioModel item : items) {
                Uri uri = Uri.parse(item.getPath());
                MediaItem mediaItem = new MediaItem.Builder()
                        .setUri(uri)
                        .build();
                mediaItems.add(mediaItem);
            }
            Log.d("MEDIAITEMS", "set media items size: "+mediaItems.size());
            player.setMediaItems(mediaItems, true);
            player.prepare();
            player.setPlayWhenReady(true);
    }

}
