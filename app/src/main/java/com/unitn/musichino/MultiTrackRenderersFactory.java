package com.unitn.musichino;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.video.DecoderVideoRenderer;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class MultiTrackRenderersFactory extends DefaultRenderersFactory {
    private int audioTrackCnt;
    private MixMeExoPlayer player;
    MediaCodecSelector selector;

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

    @Override
    protected void buildVideoRenderers(Context context, int extensionRendererMode, MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, Handler eventHandler, VideoRendererEventListener eventListener, long allowedVideoJoiningTimeMs, ArrayList<Renderer> out) {
        //super.buildVideoRenderers(context, extensionRendererMode, mediaCodecSelector, enableDecoderFallback, eventHandler, eventListener, allowedVideoJoiningTimeMs, out);
        //MediaCodecVideoRenderer renderer = new MediaCodecVideoRenderer(context, mediaCodecSelector);
        //out.add(renderer);
        selector = mediaCodecSelector;
    }

    @Override
    protected void buildTextRenderers(Context context, TextOutput output, Looper outputLooper, int extensionRendererMode, ArrayList<Renderer> out) {
        super.buildTextRenderers(context, output, outputLooper, extensionRendererMode, out);
        //TextRenderer renderer = new TextRenderer(output, outputLooper);
        //out.add(renderer);
    }

    @Override
    protected void buildMetadataRenderers(Context context, MetadataOutput output, Looper outputLooper, int extensionRendererMode, ArrayList<Renderer> out) {
       // super.buildMetadataRenderers(context, output, outputLooper, extensionRendererMode, out);
        MetadataRenderer renderer = new MetadataRenderer(output, outputLooper);
        out.add(renderer);
        //MediaCodecVideoRenderer renderer = new MediaCodecVideoRenderer(context, selector);
        //out.add(renderer);
    }
}
