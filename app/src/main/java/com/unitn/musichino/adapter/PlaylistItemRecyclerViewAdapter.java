package com.unitn.musichino.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.interfaces.PlaylistToFragment;
import com.unitn.musichino.ui.playlist.PlaylistSettingsDialog;
import com.unitn.musichino.util.C;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
/*
    Adapter per la recyclerView di playlist item.
 */

public class PlaylistItemRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistItemRecyclerViewAdapter.ViewHolder> {

    private List<PlaylistModel> playlistModels;
    private final PlaylistToFragment playlistToFragment;
    Fragment fragment;

    public PlaylistItemRecyclerViewAdapter(Fragment fragment, List<PlaylistModel> items, PlaylistToFragment playlistToFragment) {
        playlistModels = items;
        this.playlistToFragment = playlistToFragment;
        this.fragment = fragment;
    }

    public List<PlaylistModel> getPlaylistModels() {
        return this.playlistModels;
    }

    public void setPlaylistModels(List<PlaylistModel> playlistModels) {
        this.playlistModels = playlistModels;
        notifyDataSetChanged();
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
            /*
                onClick dell'item carica il contenuto della playlist in un nuovo fragment
             */
            @Override
            public void onClick(View view) {
                List<AudioModel> items = holder.playlistModel.getPlaylist();
                PlaylistModel playlistModel = playlistModels.get(position);
                playlistToFragment.onClickChange(playlistModel);

            }
        });
        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            /*
                onCLick genera una lista con all'interno la lista di tracce della playlist e la
                passa al player
             */
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
            /*
                Invoca un dialog per settare i parametri della playlist
             */
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new PlaylistSettingsDialog();
                Bundle b = new Bundle();
                b.putString("name", holder.playlistModel.getName());
                b.putString("artworkUri", holder.playlistModel.getArtworkUri());
                newFragment.setArguments(b);
                newFragment.show(fragment.getParentFragmentManager(), "playlist_settings");

            }
        });
        if (holder.playlistModel.getName().equals(C.SHARED_PLAYLISTS_LIKED)) {
            holder.btn_settings.setVisibility(View.GONE);
        }
        if (holder.playlistModel.getArtworkUri().equals("artwork_uri")) {
            holder.imageView.setImageResource(R.drawable.royalblood_cover);
        } else if (holder.playlistModel.getArtworkUri().equals(C.SHARED_PLAYLISTS_LIKED)) {
            holder.imageView.setImageResource(R.drawable.icon_liked);
        } else {
            holder.imageView.setImageDrawable(Drawable.createFromPath(holder.playlistModel.getArtworkUri()));
        }

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
            btn_settings = (ImageButton) view.findViewById(R.id.btn_add_queue);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}