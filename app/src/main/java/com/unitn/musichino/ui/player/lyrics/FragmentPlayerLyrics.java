package com.unitn.musichino.ui.player.lyrics;

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerLyrics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerLyrics extends Fragment {

    private SubtitleView subtitleView;
    private SimpleExoPlayer simpleExoPlayer;
    List<Cue> pastCues = new ArrayList<>();

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
               // if (subtitleView != null && !cues.isEmpty()) {
               //     subtitleView.setCues(cues);
               // }
              // Log.d("CUES",  simpleExoPlayer.getTextComponent().getCurrentCues().toString());
                for(Cue cue : cues){
                    if(!cue.text.toString().equals(" ")) {
                        cue = cue.buildUpon().setWindowColor(Color.TRANSPARENT).setLine(15, Cue.LINE_TYPE_NUMBER).setLineAnchor(Cue.ANCHOR_TYPE_MIDDLE).setPositionAnchor(Cue.ANCHOR_TYPE_MIDDLE).build();
                        for(int i = 0; i < pastCues.size(); i++){
                           Cue pastCue = pastCues.get(i).buildUpon().setLine((pastCues.get(i).line-3), Cue.LINE_TYPE_NUMBER).build();
                           pastCues.set(i, pastCue);
                           Log.d("PASTCUES", ""+ pastCue.line);
                        }
                        if(pastCues.size() > 0 && pastCues.get(0).line <= 0){
                            pastCues.remove(0);
                        }
                        pastCues.add(cue);
                        subtitleView.setCues(pastCues);
                    }
                }
            }
        });
        subtitleView.setFixedTextSize(Dimension.DP, 69);
    }
}