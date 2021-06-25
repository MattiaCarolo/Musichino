package com.unitn.musichino;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MediaClock;

import androidx.annotation.Nullable;

public class MultiTrackCodecAudioRenderer extends MediaCodecAudioRenderer {

    private int index;

    public MultiTrackCodecAudioRenderer(Context context, int index, MediaCodecSelector mediaCodecSelector, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener, @Nullable AudioCapabilities audioCapabilities, AudioProcessor... audioProcessors) {
        super(context, mediaCodecSelector, eventHandler, eventListener, audioCapabilities, audioProcessors);
        this.index = index;
    }
    @Override
    public MediaClock getMediaClock() {
        Log.i("MYTAG", "getMediaClock:" + String.valueOf(super.getMediaClock().getPositionUs()));
        if (index == 0) {
            return super.getMediaClock();
        }
        return null;
    }
}
