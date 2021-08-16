package com.unitn.musichino;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class MultiTrackRenderersFactory extends DefaultRenderersFactory {
    private int audioTrackCnt;
    private MixMeExoPlayer player;

    public MultiTrackRenderersFactory(int count, Context context, MixMeExoPlayer player) {
        super(context);
        this.audioTrackCnt = count;
        this.player = player;
    }

    public void setAudioTrackCnt(int audioTrackCnt) {
        this.audioTrackCnt = audioTrackCnt;
    }

    @Override
    protected void buildAudioRenderers(Context context, int extensionRendererMode, MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, AudioSink audioSink, Handler eventHandler, AudioRendererEventListener eventListener, ArrayList<Renderer> out) {
        for (int i = 0; i < audioTrackCnt; i++) {
            MultiTrackCodecAudioRenderer renderer = new MultiTrackCodecAudioRenderer(context, i, MediaCodecSelector.DEFAULT, eventHandler, eventListener, AudioCapabilities.getCapabilities(context));
            player.addRenderer(renderer);
            out.add(renderer);
        }
    }
}
