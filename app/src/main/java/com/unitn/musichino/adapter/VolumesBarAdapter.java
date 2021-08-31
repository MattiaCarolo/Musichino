package com.unitn.musichino.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.player.MultiAudioTrackSelector;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VolumesBarAdapter extends RecyclerView.Adapter<VolumesBarAdapter.ViewHolder>{

    public static SimpleExoPlayer player;
    public static List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;
    public static List<VolumesBarAdapter.ViewHolder> childViews;




    public VolumesBarAdapter(SimpleExoPlayer player, List<MediaCodecAudioRenderer> mediaCodecAudioRendererList){
        this.player = player;
        this.mediaCodecAudioRendererList = mediaCodecAudioRendererList;
        this.childViews = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private boolean isMuted = false;
        private float backupVolume = 100;
        SeekBar seekBar;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img_tagtrack);
            seekBar = (SeekBar) view.findViewById(R.id.bar_volumebar);
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.volumebar, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        childViews.add(viewHolder);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        Log.d("VOLPOS", "created at pos: "+position);
        viewHolder.seekBar.setMax(100);
        MediaCodecAudioRenderer renderer = mediaCodecAudioRendererList.get(position);
        viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    float value = i / (float)seekBar.getMax();
                    Log.d("VOLCHANGE", "SET: " +value+ ", TO POS" +position);
                    player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolder.isMuted) {
                    player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload((float)viewHolder.backupVolume).send();
                    viewHolder.seekBar.setProgress((int)viewHolder.backupVolume);

                }
                else{
                    viewHolder.backupVolume = (float)viewHolder.seekBar.getProgress();
                    player.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload((float)0).send();
                    viewHolder.seekBar.setProgress(0);

                }
                viewHolder.isMuted = !viewHolder.isMuted;
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


    public void setVolumePreset(List<Integer> values){
        //Log.d("SETVOL", "Chiild size: " +childViews.size()+", values size: " +values.size()+", media codec size: " +mediaCodecAudioRendererList.size());
        for(int i = 0; i < childViews.size(); i++){
            float value = values.get(i) / (float)childViews.get(i).seekBar.getMax();
            player.createMessage(mediaCodecAudioRendererList.get(i)).setType(C.MSG_SET_VOLUME).setPayload(value).send();
            childViews.get(i).seekBar.setProgress(values.get(i));
        }
    }

    public void setAllVolumesTo(int value){
        for(int i = 0; i < childViews.size(); i++){
            float valueFloat = value / (float)childViews.get(i).seekBar.getMax();
            player.createMessage(mediaCodecAudioRendererList.get(i)).setType(C.MSG_SET_VOLUME).setPayload(valueFloat).send();
            childViews.get(i).seekBar.setProgress(value);
        }
    }

    public List<Integer> getVolumePreset(){
        List<Integer> values = new ArrayList<>();
        for(int i = 0; i < childViews.size(); i++){
            values.add(childViews.get(i).seekBar.getProgress());
        }
        return values;
    }
}
