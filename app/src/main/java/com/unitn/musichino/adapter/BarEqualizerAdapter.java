package com.unitn.musichino.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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

public class BarEqualizerAdapter extends RecyclerView.Adapter<BarEqualizerAdapter.ViewHolder>{

    private Equalizer mEqualizer;
    private short numberFrequencyBands;
    private final short lowerEqualizerBandLevel;
    private final short upperEqualizerBandLevel;
    private Context context;
    private List<BarEqualizerAdapter.ViewHolder> childViews;

    public BarEqualizerAdapter(Context context, Equalizer mEqualizer){
        this.context = context;
        this.mEqualizer = mEqualizer;
        numberFrequencyBands = mEqualizer.getNumberOfBands();

//        get the level ranges to be used in setting the band level
//        get lower limit of the range in milliBels
        lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
//        get the upper limit of the range in millibels
        upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SeekBar seekBar;
        private TextView textView;
        public ViewHolder(View view) {
            super(view);
            seekBar = view.findViewById(R.id.mySeekBar);
            textView = view.findViewById(R.id.txt_eq_value);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.equalizerbar, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Log.d("VOLPOS", "created at pos: "+position);
        SharedPreferences properties = context.getSharedPreferences("equalizer", 0);
        viewHolder.textView.setText((mEqualizer.getCenterFreq((short)position) / 1000) + " Hz");
        int progressBar = properties.getInt("seek_" + position, 1500);
        if (progressBar != 1500) {
            viewHolder.seekBar.setProgress(progressBar);
        } else {
            viewHolder.seekBar.setProgress(mEqualizer.getBandLevel((short)position));
        }
        mEqualizer.setBandLevel((short)position,
                (short) (progressBar + lowerEqualizerBandLevel));
        viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mEqualizer.setBandLevel((short)position,
                        (short) (progress + lowerEqualizerBandLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                properties.edit().putInt("seek_" + position, seekBar.getProgress()).apply();
                properties.edit().putInt("position", 0).apply();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return numberFrequencyBands;
    }

    public void setVolumePreset(List<Integer> values){
        for(int i = 0; i < getItemCount(); i++){
           // player.createMessage(mediaCodecAudioRendererList.get(i)).setType(C.MSG_SET_VOLUME).setPayload(values.get(i)).send();
            childViews.get(i).seekBar.setProgress(values.get(i));
        }
    }


}
