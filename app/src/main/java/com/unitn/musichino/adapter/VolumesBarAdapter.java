package com.unitn.musichino.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.MixMeExoPlayer;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.PlayerActivity;

public class VolumesBarAdapter extends RecyclerView.Adapter<VolumesBarAdapter.ViewHolder>{

    private SimpleExoPlayer player;

    public VolumesBarAdapter(AudioService service){
        player = service.getplayerInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SeekBar seekBar;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            int index;
            imageView = (ImageView) view.findViewById(R.id.img_tagtrack);
            seekBar = (SeekBar) view.findViewById(R.id.bar_volumebar);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(b){
                        float value = i / (float)seekBar.getMax();
                        //    System.out.println("total audio decoders? " + mixplayer.player.getAudioDecoderCounters().renderedOutputBufferCount + ", value? " + value);
                        MediaCodecAudioRenderer renderer = mixMePlayer.renderers.get(0);
                        mixMePlayer.player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.volumebar, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return player.getAudioDecoderCounters().inputBufferCount;
    }


}
