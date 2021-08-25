package com.unitn.musichino.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.MixMeExoPlayer;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VolumesBarAdapter extends RecyclerView.Adapter<VolumesBarAdapter.ViewHolder>{


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final SeekBar seekBar;
        private MixMeExoPlayer mixMePlayer;
        private List<AudioModel> tracks;

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

        public ImageView getTextView() {
            return imageView;
        }

        public ImageView setTextView() {
            return imageView;
        }

    }
}
