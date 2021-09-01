package com.unitn.musichino.player;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.unitn.musichino.service.AudioService;

import java.util.ArrayList;

/*
    Classe che estende DefaultRenderersFactory di ExoPlayer.
    Il costruttore salva l'istanza del player e riceve il numero di renderer da creare.
    Poiche' e' molto difficile sapere il numero di tracce del file prima di passarlo a MultiAudioTrackSelector,
    abbiamo fissato il numero di audio renderer da creare a 10.
    Viene modificato anche buildAudioRenderers in modo da creare il numero necessario di MultiTrackCodecAudioRenderer.
    Vengono poi aggiunti sia all'output standard che alla reference dentro il servizio del player per permettere una comunicazione con essi
*/

public class MultiTrackRenderersFactory extends DefaultRenderersFactory {
    private int audioTrackCnt;
    private AudioService playerService;

    public MultiTrackRenderersFactory(int count, Context context, AudioService playerService) {
        super(context);
        this.audioTrackCnt = count;
        this.playerService = playerService;
    }

    @Override
    protected void buildAudioRenderers(Context context, int extensionRendererMode, MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, AudioSink audioSink, Handler eventHandler, AudioRendererEventListener eventListener, ArrayList<Renderer> out) {
        for (int i = 0; i < audioTrackCnt; i++) {
            MultiTrackCodecAudioRenderer renderer = new MultiTrackCodecAudioRenderer(context, i, MediaCodecSelector.DEFAULT, eventHandler, eventListener, AudioCapabilities.getCapabilities(context));
            playerService.addRenderer(renderer);
            out.add(renderer);
        }
    }
}
