package com.unitn.musichino.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.service.MixMe;
import com.unitn.musichino.ui.search.PlaylistSelectionDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
/*
    Adapter per le tracce ritrovate nella searchview
 */
public class SearchTrackAdapter extends RecyclerView.Adapter<SearchTrackAdapter.ViewHolder> {

    private List<AudioModel> tracks;
    private List<AudioModel> backup;
    private FragmentActivity fragment;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton button, btn_queue;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.txt_playlist_name);
            button = (ImageButton) view.findViewById(R.id.btn_play_playlist);
            btn_queue = (ImageButton) view.findViewById(R.id.btn_add_queue);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageButton getButton() {
            return button;
        }

        public ImageButton getQueueButton() {
            return btn_queue;
        }
    }


    public SearchTrackAdapter(FragmentActivity fragment, List<AudioModel> dataSet, List<AudioModel> backup) {
        tracks = dataSet;
        this.backup = backup;
        this.fragment = fragment;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_track_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(position + ":" + tracks.get(position).toString());
        /*
            Riproduce direttamente la canzone selezionata
         */
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                Bundle bundle = new Bundle();
                List<AudioModel> items = new ArrayList<>();
                items.add(tracks.get(position));
                bundle.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                intent.putExtra("bundle", bundle);
                view.getContext().startActivity(intent);
            }
        });
        /*
            Aggiunge la canzone selezionata alla queue di riproduzione
         */
        viewHolder.getQueueButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MixMe mixMe = (MixMe) fragment.getApplication();
                if (mixMe.is_running() && mixMe.getService().currentlyPlaying != null) {
                    mixMe.getService().addToQueue(tracks.get(position));
                    Log.d("QUEUE", "Adding to queue");
                } else {
                    Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                    Bundle bundle = new Bundle();
                    List<AudioModel> items = new ArrayList<>();
                    items.add(tracks.get(position));
                    bundle.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                    intent.putExtra("bundle", bundle);
                    view.getContext().startActivity(intent);
                }
            }
        });

        /*
            Crea un dialog per aggiungere una traccia ad una playlist
         */
        viewHolder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new PlaylistSelectionDialog();
                Bundle b = new Bundle();
                b.putParcelable("item", tracks.get(position));
                newFragment.setArguments(b);
                newFragment.show(fragment.getSupportFragmentManager(), "playlist_selection");
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tracks.clear();
        if (charText.length() == 0) {
            tracks.addAll(backup);
        } else {
            for (AudioModel audio : backup) {
                if (audio.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    tracks.add(audio);
                }
            }
        }
        notifyDataSetChanged();
    }

}

