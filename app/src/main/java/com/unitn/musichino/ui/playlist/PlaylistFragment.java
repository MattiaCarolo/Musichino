package com.unitn.musichino.ui.playlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PlaylistItemRecyclerViewAdapter;
import com.unitn.musichino.adapter.SingleTrackAdapter;
import com.unitn.musichino.interfaces.PlaylistToFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment representing a list of Items.
 */
public class PlaylistFragment extends Fragment implements PlaylistToFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaylistFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
     //   args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_list, container, false);
        List<PlaylistModel> playlistModels = new ArrayList<>();
        try {
            PlaylistModel tempModel = new PlaylistModel();
            playlistModels = tempModel.getPlaylistModelsFromSharedPreferences(requireContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new PlaylistItemRecyclerViewAdapter(playlistModels, this));
        }
        return view;
    }

    @Override
    public void onClickChange(PlaylistModel playListModel) {
        SingleTrackAdapter singleTrackAdapter = new SingleTrackAdapter(requireActivity(),playListModel.getPlaylist(),playListModel.getPlaylist(), 1);
        recyclerView.setAdapter(singleTrackAdapter);
    }
}