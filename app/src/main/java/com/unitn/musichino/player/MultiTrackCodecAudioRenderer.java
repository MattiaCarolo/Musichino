package com.unitn.musichino.player;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.util.MediaClock;

import androidx.annotation.Nullable;

/*
    Classe che estende MediaCodecAudioRenderer di ExoPlayer.
    Viene modificato il costruttore per salvare l'indice del MediaCodecAudioRenderer usato per le comunicazioni.
    Viene modificato getMediaClock per mantenere la sincronizzazione tra i renderer, che seguiranno tutti
    il media clock della prima traccia.
*/

public class MultiTrackCodecAudioRenderer extends MediaCodecAudioRenderer {

    private int index;

    public MultiTrackCodecAudioRenderer(Context context, int index, MediaCodecSelector mediaCodecSelector, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener, @Nullable AudioCapabilities audioCapabilities, AudioProcessor... audioProcessors) {
        super(context, mediaCodecSelector, eventHandler, eventListener, audioCapabilities, audioProcessors);
        this.index = index;
    }
    @Override
    public MediaClock getMediaClock() {
        if (index == 0) {
            return super.getMediaClock();
        }
        return null;
    }
}
