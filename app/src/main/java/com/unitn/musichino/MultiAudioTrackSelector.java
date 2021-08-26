package com.unitn.musichino;

import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.util.MimeTypes;
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
        int idx = 0;
        int groupIndex = 0;
        for(int rendererIndex = 0; rendererIndex < rendererCount && groupIndex < trackGroups.length; rendererIndex++){
            int rendererType = rendererCapabilities[rendererIndex].getTrackType();
            TrackGroup trackGroup = trackGroups.get(groupIndex);
            String trackMime = trackGroup.getFormat(0).sampleMimeType;
            int trackType = MimeTypes.getTrackType(trackMime);
            switch (rendererType){
                case C.TRACK_TYPE_AUDIO:
                    Log.d("TRACKTYPE", "Audio renderer found.");
                    if(trackType == rendererType){
                        rendererTrackSelections[rendererIndex] = new FixedTrackSelection(trackGroup, 0, C.TRACK_TYPE_AUDIO);
                        Log.d("TRACKTYPE", "Audio renderer selected, renderer index: " +rendererIndex+", group index: " +groupIndex);
                        groupIndex++;
                        audioRendererCount++;
                    }
                    break;
                case C.TRACK_TYPE_VIDEO:
                    Log.d("TRACKTYPE", "Video renderer found.");
                    if(trackType == rendererType){
                        rendererTrackSelections[rendererIndex] = new FixedTrackSelection(trackGroup, 0, C.TRACK_TYPE_VIDEO);
                        Log.d("TRACKTYPE", "Video renderer selected, renderer index: " +rendererIndex+", group index: " +groupIndex);
                        groupIndex++;
                    }
                    break;
                case C.TRACK_TYPE_TEXT:
                    Log.d("TRACKTYPE", "Text renderer found.");
                    if(trackType == rendererType){
                        rendererTrackSelections[rendererIndex] = new FixedTrackSelection(trackGroup, 0, C.TRACK_TYPE_TEXT);
                        Log.d("TRACKTYPE", "Text renderer selected, renderer index: " +rendererIndex+", group index: " +groupIndex);
                        groupIndex++;
                    }
                    break;
                case C.TRACK_TYPE_METADATA:
                    Log.d("TRACKTYPE", "Metadata renderer found.");
                    if(trackType == rendererType){
                        rendererTrackSelections[rendererIndex] = new FixedTrackSelection(trackGroup, 0, C.TRACK_TYPE_METADATA);
                        Log.d("TRACKTYPE", "Metadata renderer selected, renderer index: " +rendererIndex+", group index: " +groupIndex);
                        groupIndex++;
                    }
                    break;
                default:
                    Log.d("TRACKTYPE", "Track type unknown or unsupported [" +trackType+"]");
                    break;
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
