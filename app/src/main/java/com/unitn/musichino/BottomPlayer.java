package com.unitn.musichino;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.ui.playlist.PlaylistSelectionDialog;


public class BottomPlayer extends Fragment {

    ImageButton btn_next,btn_previous,btn_play_pause;
    ImageView imageView;
    PlayerView playerView;
    AudioService mService;
    SimpleExoPlayer simpleExoPlayer;
    TextView textView;
    AudioModel item;
    private Boolean _isPlaying = true;


    public BottomPlayer() {
        // Required empty public constructor
    }

    public static BottomPlayer newInstance() {
        BottomPlayer fragment = new BottomPlayer();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = ((PlayerActivity) requireActivity()).mService;
        simpleExoPlayer = mService.getplayerInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_bottom_player, container, false);

        btn_next = root.findViewById(R.id.btn_next);
        btn_play_pause = root.findViewById(R.id.btn_play_pause);
        btn_previous = root.findViewById(R.id.btn_prev);
        imageView = root.findViewById(R.id.iv_album);
        textView = root.findViewById(R.id.txt_info);

        item = mService.currentlyPlaying;

        textView.setText(item.getName() + " - " + item.getArtist());

        simpleExoPlayer = mService.getplayerInstance();
        playerView.setPlayer(simpleExoPlayer);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_isPlaying) {
                    simpleExoPlayer.setPlayWhenReady(false);
                    _isPlaying = !_isPlaying;
                    btn_play_pause.setImageResource(R.drawable.exo_controls_play);
                    Log.d("HElloooo", "pause");
                }else{
                    simpleExoPlayer.setPlayWhenReady(true);
                    _isPlaying = !_isPlaying;
                    btn_play_pause.setImageResource(R.drawable.exo_controls_pause);
                    Log.d("HElloooo", "play");
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleExoPlayer != null && simpleExoPlayer.hasPreviousWindow()){
                    simpleExoPlayer.seekToPreviousWindow();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleExoPlayer != null && simpleExoPlayer.hasNextWindow()){
                    simpleExoPlayer.seekToNextWindow();
                }
            }
        });

    }
}