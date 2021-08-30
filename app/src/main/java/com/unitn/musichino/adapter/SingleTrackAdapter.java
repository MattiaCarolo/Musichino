package com.unitn.musichino.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.ui.playlist.PlaylistSelectionDialog;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SingleTrackAdapter extends RecyclerView.Adapter<SingleTrackAdapter.ViewHolder> {

    private List<AudioModel> tracks;
    private List<AudioModel> backup;
    private FragmentActivity fragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton button;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            int index;
            textView = (TextView) view.findViewById(R.id.txt_playlist_name);
            button = (ImageButton) view.findViewById(R.id.btn_managePlaylist);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageButton getButton(){return button;}
    }



    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public SingleTrackAdapter(FragmentActivity fragment, List<AudioModel> dataSet, List<AudioModel> backup) {
        tracks = dataSet;
        this.backup = backup;
        this.fragment = fragment;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

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
                Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                Bundle bundle = new Bundle();
                List<AudioModel> items = new ArrayList<>();
                items.add(tracks.get(position));
                bundle.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                intent.putExtra("bundle", bundle);
                view.getContext().startActivity(intent);
            }
        });
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

    // Return the size of your dataset (invoked by the layout manager)
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

