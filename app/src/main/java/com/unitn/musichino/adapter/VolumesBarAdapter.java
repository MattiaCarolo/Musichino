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
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.MixMeExoPlayer;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.MultiAudioTrackSelector;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.PlayerActivity;

public class VolumesBarAdapter extends RecyclerView.Adapter<VolumesBarAdapter.ViewHolder>{

    public static SimpleExoPlayer player;
    public static List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;

    private SeekBar seekBar;
    private ImageView imageView;

    public VolumesBarAdapter(SimpleExoPlayer player, List<MediaCodecAudioRenderer> mediaCodecAudioRendererList){
        this.player = player;
        this.mediaCodecAudioRendererList = mediaCodecAudioRendererList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.volumebar, viewGroup, false);
        imageView = (ImageView) view.findViewById(R.id.img_tagtrack);
        seekBar = (SeekBar) view.findViewById(R.id.bar_volumebar);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Log.d("VOLPOS", "created at pos: "+position);
        MediaCodecAudioRenderer renderer = mediaCodecAudioRendererList.get(position);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    float value = i / (float)seekBar.getMax();
                    player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        MultiAudioTrackSelector selector = (MultiAudioTrackSelector)player.getTrackSelector();
        if (selector != null)
            return selector.audioRendererCount;
        else
            return 0;
    }


}
