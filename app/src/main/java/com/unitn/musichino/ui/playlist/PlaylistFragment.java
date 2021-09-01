package com.unitn.musichino.ui.playlist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.unitn.musichino.BottomPlayerFragment;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PlaylistItemRecyclerViewAdapter;
import com.unitn.musichino.adapter.PlaylistTrackAdapter;
import com.unitn.musichino.adapter.SearchTrackAdapter;
import com.unitn.musichino.interfaces.PlaylistToFragment;
import com.unitn.musichino.service.MixMe;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
    FrameLayout frameLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaylistFragment() {
    }


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

        recyclerView = view.findViewById(R.id.rv_playlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) 170);
        recyclerView.addItemDecoration(bottomOffsetDecoration);
        recyclerView.setAdapter(new PlaylistItemRecyclerViewAdapter(this, playlistModels, this));

        frameLayout = view.findViewById(R.id.play_mini);
        frameLayout.setVisibility(View.GONE);
        Fragment fragment = new BottomPlayerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.play_mini, fragment);
        transaction.commit();


        return view;
    }

    @Override
    public void onClickChange(PlaylistModel playListModel) {
        PlaylistTrackAdapter singleTrackAdapter = new PlaylistTrackAdapter(requireActivity(),playListModel.getPlaylist(), playListModel);
        recyclerView.setAdapter(singleTrackAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((MixMe)requireActivity().getApplication()).is_running()){
            frameLayout.setVisibility(View.VISIBLE);
        }
    }

    static class BottomOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mBottomOffset;

        public BottomOffsetDecoration(int bottomOffset) {
            mBottomOffset = bottomOffset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }

        }
    }
}