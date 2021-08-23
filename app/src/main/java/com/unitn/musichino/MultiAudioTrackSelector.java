package com.unitn.musichino;

import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.util.NonNullApi;

import java.util.List;

import androidx.annotation.NonNull;

public class MultiAudioTrackSelector extends TrackSelector {
    public int audioRendererCount;
    public int textRendererCount;
    public List<Integer> audioRendererIndexes;
    public List<Integer> textRendererIndexes;

    public int getAudioRendererCount() {
        return audioRendererCount;
    }

    public int getTextRendererCount() {
        return textRendererCount;
    }

    @Override
    public @NonNull TrackSelectorResult  selectTracks(RendererCapabilities[] rendererCapabilities, @NonNull TrackGroupArray trackGroups,@NonNull MediaSource.MediaPeriodId periodId,@NonNull Timeline timeline) throws ExoPlaybackException {

        int rendererCount = rendererCapabilities.length;
        audioRendererCount = 0;
        textRendererCount = 0;
        ExoTrackSelection[] rendererTrackSelections = new ExoTrackSelection[rendererCount];
        Log.i("SELECTOR", "RendererCount: " + rendererCount + ", TrackGroupsCount: " + trackGroups.length);
        int idx = 1;
        for (int i = 0; i < trackGroups.length; i++) {
            int trackType = rendererCapabilities[i].getTrackType();
            Log.d("TRAKTYPE", trackType+"");
            int length = trackGroups.length;
            if (trackType == C.TRACK_TYPE_AUDIO) {
               // int trackGroup = 2 * idx + 1;
                int trackGroup = idx - 1;                                                           // TODO search for track group
                if (trackGroups.get(trackGroup).getFormat(0).metadata != null)
                Log.i("TRAKTAG", "meta: " + trackGroups.get(trackGroup).getFormat(0).metadata.toString() + ", codecs: " + trackGroups.get(trackGroup).getFormat(0).codecs);
                rendererTrackSelections[i] = new FixedTrackSelection(trackGroups.get(trackGroup), 0, C.TRACK_TYPE_AUDIO);
                idx++;
                audioRendererCount++;
            }
            else if( trackType == C.TRACK_TYPE_TEXT){
                int textGroup = idx - 1;                                                         // TODO search for text group
                rendererTrackSelections[i] = new FixedTrackSelection(trackGroups.get(textGroup), 0, C.TRACK_TYPE_TEXT);
                textRendererCount++;
                idx++;
            }
            else if(trackType == C.TRACK_TYPE_VIDEO){
                int trackGroup = idx -1;                                                           // TODO search for track group
                if (trackGroups.get(trackGroup).getFormat(0).sampleMimeType != null)
                    Log.i("TRAKVIDIO", "meta: " + trackGroups.get(trackGroup).getFormat(0).containerMimeType+ ", codecs: " + trackGroups.get(trackGroup).getFormat(0).codecs);
               // rendererTrackSelections[i] = new FixedTrackSelection(trackGroups.get(trackGroup), 0);
              //  idx++;
            }
            else if(trackType == C.TRACK_TYPE_METADATA){
                int trackGroup = idx -1;
                if (trackGroups.get(trackGroup).getFormat(0).sampleMimeType != null)
                    Log.i("TRAKMETA", "MIME: " + trackGroups.get(trackGroup).getFormat(0).sampleMimeType+ ", codecs: " + trackGroups.get(trackGroup).getFormat(0).codecs);
                //rendererTrackSelections[i] = new FixedTrackSelection(trackGroups.get(trackGroup), 0);
                //idx++;
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
