package com.unitn.musichino.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.player.MultiAudioTrackSelector;
import com.unitn.musichino.player.MultiTrackRenderersFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/*
    Classe principale della gestione della logica del player ExoPlayer tramite utilizzo di Service.
    Contiene tutte le variabili collegate alla riproduzione del player ed incapsula le informazioni
    delle tracce in riproduzione e della playlist in corso.
*/

public class AudioService extends Service {

    // Costante del numero totale di MultiTrackCodecAudioRenderer da creare, impostata a numero statico per la difficolta'
    // nell'individuare il numero di tracce prima di passarle al player.
    private final int RENDERER_COUNT = 10;
    private static final String CHANNEL_ID = "playback_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final CharSequence CHANNEL_NAME = "notification_channel";
    private static final String CHANNEL_DESCRIPTION = "MixMe";

    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private final IBinder mBinder = new LocalBinder();
    private List<AudioModel> items;

    public List<MediaCodecAudioRenderer> renderers;
    public AudioModel currentlyPlaying;


    @Override
    public void onCreate() {
        super.onCreate();
        renderers = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        ((MixMe)this.getApplication()).set_running(false);
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
        return mBinder;
    }

    public SimpleExoPlayer getPlayerInstance() {
        if (player == null) {
            startPlayer();
        }
        return player;
    }

    public SimpleExoPlayer getplayer() {
        return player;
    }

    public AudioModel Playing_Now() {
        return currentlyPlaying;
    }

    /*
        Quando viene eseguito il bind con il servizio
        si tenta di recuperare gli item da impostare al player dall'intent
        e se il player non esiste lo si inizializza
    */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getBundleExtra("bundle");
        if (b != null) {
            items = b.getParcelableArrayList("items");
        }
        if (player == null) {
            startPlayer();
        }
        ((MixMe)this.getApplication()).set_running(true);
        ((MixMe)this.getApplication()).runningService(this);
        return START_STICKY;
    }


    /*
        Metodo principale che fa inizializzare e se presenti degli item dall'intent fa partire in ordine tutta la playlist.
        Vengono inizializzate tutte le componenti custom per creare un'istanza di SimpleExoPlayer; viene aggiunto un listener
        che imposta sulla variabile currentlyPlaying l'AudioModel corrispondente all'audio riprodotto ogni volta che il player cambia set di tracce;
        viene impostato e bindato anche il NotificationManager che creera' la notifica del player popolandolo con i metadati presenti nel MediaItem
        tramite il DefaultMediaDescriptionAdapter. Infine viene creato un canale di comunicazione per far attivare il servizio in Foreground

    */
    private void startPlayer() {
        final Context context = this;
        currentlyPlaying = items.get(0);
        MultiAudioTrackSelector trackSelector = new MultiAudioTrackSelector();
        MultiTrackRenderersFactory renderersFactory = new MultiTrackRenderersFactory(RENDERER_COUNT, context, this);
        player = new SimpleExoPlayer.Builder(context.getApplicationContext(), renderersFactory)
                .setTrackSelector(trackSelector)
                .build();

        player.addListener(new Player.Listener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                currentlyPlaying = items.get(player.getCurrentPeriodIndex());
            }
        });
        List<MediaItem> mediaItems = new ArrayList<>();
        for (AudioModel item : items) {
            Uri uri = Uri.parse(item.getPath());
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(uri)
                    .build();
            mediaItems.add(mediaItem);
        }
        //Log.d("MEDIAITEMS", "set media items size: " + mediaItems.size());
        player.setMediaItems(mediaItems, true);
        player.prepare();
        player.setPlayWhenReady(true);
        DefaultMediaDescriptionAdapter descriptionAdapter = new DefaultMediaDescriptionAdapter(
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

    public void addRenderer(MediaCodecAudioRenderer renderer) {
        renderers.add(renderer);
    }


    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*
        Metodo invocato al posto di startPlayer in caso il player sia gia' stato inizializzato e stia riproducendo.
        Al posto di ricostruire da capo il player, vengono impostati dei nuovi MediaItem nella coda.
    */

    public void changeSong(List<AudioModel> items) {
        List<MediaItem> mediaItems = new ArrayList<>();
        for (AudioModel item : items) {
            Uri uri = Uri.parse(item.getPath());
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(uri)
                    .build();
            mediaItems.add(mediaItem);
        }
        //Log.d("MEDIAITEMS", "set media items size: " + mediaItems.size());
        player.setMediaItems(mediaItems, true);
        player.prepare();
        player.setPlayWhenReady(true);
    }

}
