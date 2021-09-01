package com.unitn.musichino.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PlaylistTrackAdapter extends RecyclerView.Adapter<PlaylistTrackAdapter.ViewHolder> {

    private List<AudioModel> tracks;
    private FragmentActivity fragment;
    private PlaylistModel playlistModel;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton button,btn_queue;
        private final View mView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            int index;
            mView = view;
            textView = (TextView) view.findViewById(R.id.txt_playlist_name);
            button = (ImageButton) view.findViewById(R.id.btn_play_playlist);
            btn_queue = (ImageButton) view.findViewById(R.id.btn_add_queue);
            btn_queue.setVisibility(View.GONE);
            button.setImageResource(R.drawable.icon_remove);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageButton getButton(){return button;}
        public ImageButton getQueue(){return button;}
    }



    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public PlaylistTrackAdapter(FragmentActivity fragment, List<AudioModel> dataSet, PlaylistModel playlistModel) {
        tracks = dataSet;
        this.fragment = fragment;
        this.playlistModel = playlistModel;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_track_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(position+":" + tracks.get(position).toString());
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AudioModel> items = playlistModel.getPlaylist();
                Intent intent = new Intent(viewHolder.mView.getContext(), PlayerActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                b.putBoolean("pld_Playlist_item",true);
                b.putInt("pld_Position", position);
                intent.putExtra("bundle", b);
                viewHolder.mView.getContext().startActivity(intent);
            }
        });
        viewHolder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playlistModel.removeAudioFromPlaylist(fragment,tracks.get(position).getName());
                    tracks.remove(position);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tracks.size();
    }


}

