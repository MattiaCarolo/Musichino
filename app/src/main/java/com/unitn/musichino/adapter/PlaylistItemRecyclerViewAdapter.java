package com.unitn.musichino.adapter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.interfaces.PlaylistToFragment;
import com.unitn.musichino.ui.playlist.PlaylistSettingsDialog;
import com.unitn.musichino.ui.search.PlaylistSelectionDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class PlaylistItemRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaylistModel> playlistModels;
    private final PlaylistToFragment playlistToFragment;
    Fragment fragment;

    public PlaylistItemRecyclerViewAdapter(Fragment fragment, List<PlaylistModel> items, PlaylistToFragment playlistToFragment) {
        playlistModels = items;
        this.playlistToFragment = playlistToFragment;
        this.fragment = fragment;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.playlistModel = playlistModels.get(position);
        holder.mContentView.setText(playlistModels.get(position).getName());
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AudioModel> items = holder.playlistModel.getPlaylist();
                /*
                Intent intent = new Intent(holder.mView.getContext(), PlayerActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                intent.putExtra("bundle", b);
                //holder.mView.getContext().startActivity(intent);
                */
                PlaylistModel playlistModel = playlistModels.get(position);
                playlistToFragment.onClickChange(playlistModel);

            }
        });
        holder.btn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                List<AudioModel> items = holder.playlistModel.getPlaylist();
                Intent intent = new Intent(holder.mView.getContext(), PlayerActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                intent.putExtra("bundle", b);
                holder.mView.getContext().startActivity(intent);
            }
        });
        holder.btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new PlaylistSettingsDialog();
                Bundle b = new Bundle();
                b.putString("name", holder.playlistModel.getName());
                b.putString("artworkUri", holder.playlistModel.getArtworkUri());
                newFragment.setArguments(b);
                newFragment.show(fragment.getParentFragmentManager(), "playlist_selection");

            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public PlaylistModel playlistModel;
        public ImageView imageView;
        public ImageButton btn_settings, btn_play;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.iv_playlist_image);
            mContentView = (TextView) view.findViewById(R.id.txt_playlist_name);
            btn_play = (ImageButton) view.findViewById(R.id.btn_play_playlist);
            btn_settings = (ImageButton) view.findViewById(R.id.btn_playlist_settings);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }



}