package com.unitn.musichino.interfaces;

import android.widget.Button;

import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;

public interface ButtonTrackClickListener {
    void onButtonTrackClick(int pos, Button button, MediaCodecAudioRenderer renderer);
}
