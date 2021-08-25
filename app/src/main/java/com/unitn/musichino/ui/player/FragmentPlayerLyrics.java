package com.unitn.musichino.ui.player;

import android.os.Bundle;

import androidx.annotation.Dimension;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.SubtitleView;
import com.unitn.musichino.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerLyrics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerLyrics extends Fragment {

    private SubtitleView subtitleView;

    public FragmentPlayerLyrics() {
        // Required empty public constructor
    }

    public static FragmentPlayerLyrics newInstance(String param1, String param2) {
        FragmentPlayerLyrics fragment = new FragmentPlayerLyrics();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_player_lyrics, container, false);
        subtitleView = root.findViewById(R.id.exo_subtitles_view);
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        subtitleView.setFixedTextSize(Dimension.DP, 69);
    }
}