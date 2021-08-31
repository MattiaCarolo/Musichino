package com.unitn.musichino.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.player.MultiAudioTrackSelector;
import com.unitn.musichino.R;
import com.unitn.musichino.interfaces.ButtonTrackClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder>{
    public static SimpleExoPlayer simpleExoPlayer;
    public static List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;
    public static ButtonTrackClickListener buttonTrackClickListener;

    private static Button button;

    public TrackAdapter (SimpleExoPlayer simpleExoPlayer, List<MediaCodecAudioRenderer>mediaCodecAudioRendererList, ButtonTrackClickListener buttonTrackClickListener){
        this.simpleExoPlayer = simpleExoPlayer;
        this.mediaCodecAudioRendererList = mediaCodecAudioRendererList;
        this.buttonTrackClickListener = buttonTrackClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);
            button = (Button) view.findViewById(R.id.btn_track);
        }

    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_btn_track, viewGroup, false);
        button = (Button) view.findViewById(R.id.btn_track);
        return new TrackAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull TrackAdapter.ViewHolder viewHolder, final int position) {
        Log.d("VOLPOS", "created at pos: "+position);
        MediaCodecAudioRenderer renderer = mediaCodecAudioRendererList.get(position);
        button.setText("Traccia N. " + position);
        ViewCompat.setTransitionName(button, "btn" + position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTrackClickListener.onButtonTrackClick(position,button,renderer);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        MultiAudioTrackSelector selector = (MultiAudioTrackSelector)simpleExoPlayer.getTrackSelector();
        if (selector != null)
            return selector.audioRendererCount;
        else
            return 0;
    }

}
