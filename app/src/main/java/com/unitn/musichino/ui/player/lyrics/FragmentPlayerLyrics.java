package com.unitn.musichino.ui.player.lyrics;

import android.os.Bundle;

import androidx.annotation.Dimension;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerLyrics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerLyrics extends Fragment {

    private SubtitleView subtitleView;
    private SimpleExoPlayer simpleExoPlayer;

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
        subtitleView.setFixedTextSize(Dimension.DP, 69);
        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        simpleExoPlayer = ((PlayerActivity) getActivity()).getPlayer();
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onCues(List<Cue> cues) {
                if (cues.size() > 1) {
                    Log.d("CUES", "There are more than one cues available.");
                }
                if (subtitleView != null && !cues.isEmpty()) {
                    subtitleView.setCues(cues);
                }
            }
        });
        subtitleView.setFixedTextSize(Dimension.DP, 69);
    }
}