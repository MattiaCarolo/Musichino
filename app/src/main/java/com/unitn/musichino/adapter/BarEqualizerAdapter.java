package com.unitn.musichino.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        this.childViews = new ArrayList<>();

//        get the level ranges to be used in setting the band level
//        get lower limit of the range in milliBels
        lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
//        get the upper limit of the range in millibels
        upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];

        mEqualizer.setEnabled(false);
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
        ViewHolder viewHolder = new ViewHolder(view);
        childViews.add(viewHolder);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Log.d("VOLPOS", "created at pos: "+position);
        viewHolder.seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
        SharedPreferences properties = context.getSharedPreferences("equalizer", 0);
        viewHolder.textView.setText((mEqualizer.getCenterFreq((short)position) / 1000) + " Hz");
        mEqualizer.setBandLevel((short)position,
                (short) upperEqualizerBandLevel);
        viewHolder.seekBar.setProgress(upperEqualizerBandLevel);
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
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return numberFrequencyBands;
    }

    public void setEqPreset(List<Integer> values){
        for(int i = 0; i < getItemCount(); i++){
            childViews.get(i).seekBar.setProgress(values.get(i));
        }
    }

    public List<Integer> getEqPreset(){
        List<Integer> values = new ArrayList<>();
        for(int i= 0; i < childViews.size(); i++){
            values.add(childViews.get(i).seekBar.getProgress());
        }
        return values;
    }

    public void setAllBandsToNeutral(){
        short upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];
        for(int i= 0; i < childViews.size(); i++){
            childViews.get(i).seekBar.setProgress(upperEqualizerBandLevel);
            mEqualizer.setBandLevel((short)i, (short)0);
        }
    }


}
