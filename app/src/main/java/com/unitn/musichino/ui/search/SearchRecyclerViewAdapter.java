package com.unitn.musichino.ui.search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.databinding.FragmentPlaylistSelectionDialogBinding;
import com.unitn.musichino.ui.playlist.PlaylistSelectionDialog;


import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private List<AudioModel> tracks;
    private FragmentActivity fragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button button;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            int index;
            textView = (TextView) view.findViewById(R.id.textView);
            button = (Button) view.findViewById(R.id.addToPlaylistButton);
        }

        public TextView getTextView() {
            return textView;
        }
        public Button getButton(){return button;}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public SearchRecyclerViewAdapter(FragmentActivity fragment, List<AudioModel> dataSet) {
        tracks = dataSet;
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
                AudioModel item = tracks.get(position);
                bundle.putParcelable("item", item);
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


}

