package com.unitn.musichino;

import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.util.NonNullApi;

import androidx.annotation.NonNull;

public class MultiAudioTrackSelector extends TrackSelector {

    @Override
    public @NonNull TrackSelectorResult  selectTracks(RendererCapabilities[] rendererCapabilities, @NonNull TrackGroupArray trackGroups,@NonNull MediaSource.MediaPeriodId periodId,@NonNull Timeline timeline) throws ExoPlaybackException {

        int rendererCount = rendererCapabilities.length;
        TrackSelection[] rendererTrackSelections = new TrackSelection[rendererCount];
        Log.i("SELECTOR", "RendererCount: " + rendererCount + ", TrackGroupsCount: " + trackGroups.length);
        int idx = 1;
        for (int i = 0; i < rendererCount; i++) {
            int trackType = rendererCapabilities[i].getTrackType();
            int length = trackGroups.length;
            if (trackType == C.TRACK_TYPE_AUDIO) {
               // int trackGroup = 2 * idx + 1;
                int trackGroup = idx - 1;                                                           // TODO search for track group
                Log.i("TRAKTAG", "sample mime: " + trackGroups.get(trackGroup).getFormat(0).sampleMimeType + ", codecs: " + trackGroups.get(trackGroup).getFormat(0).codecs);
                rendererTrackSelections[i] = new FixedTrackSelection(trackGroups.get(trackGroup), 0);
                idx++;
            }
            else if( trackType == C.TRACK_TYPE_TEXT){
                int textGroup = length - 1;                                                         // TODO search for text group
                rendererTrackSelections[i] = new FixedTrackSelection(trackGroups.get(textGroup), 0);

            }

        }

        RendererConfiguration[] rendererConfigurations = new RendererConfiguration[rendererCapabilities.length];
        for (int i = 0; i < rendererCount; i++) {
            rendererConfigurations[i] = rendererTrackSelections[i] != null ? RendererConfiguration.DEFAULT : null;
        }
        return new TrackSelectorResult( rendererConfigurations, rendererTrackSelections, null);
    }

    @Override
    public void onSelectionActivated(Object info) {
    }
}
