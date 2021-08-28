package com.unitn.musichino.ui.player.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Util;
import com.unitn.musichino.MixMeExoPlayer;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.service.AudioService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerHome extends Fragment {


    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private AudioService mService;
    private boolean mBound = false;
    private Button button;
    private Boolean _isPlaying;
    private ImageButton btn_play_pause, btn_previous, btn_next, btn_shuffle, btn_addPlaylist;
    private DefaultTimeBar defaultTimeBar;
    AudioModel item;

    public FragmentPlayerHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPlayerHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPlayerHome newInstance(String param1, String param2) {
        FragmentPlayerHome fragment = new FragmentPlayerHome();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = ((PlayerActivity) getActivity()).mService;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player_home, container, false);
        btn_next = root.findViewById(R.id.btn_next);
        btn_addPlaylist = root.findViewById(R.id.btn_addplaylist);
        btn_play_pause = root.findViewById(R.id.btn_play_pause);
        btn_previous = root.findViewById(R.id.btn_prev);
        btn_shuffle = root.findViewById(R.id.btn_shuffle);
        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("stop", "pppppa");
                if (simpleExoPlayer == null) {
                    simpleExoPlayer = mService.getplayerInstance();
                    _isPlaying = true;
                    btn_play_pause.setImageResource(R.drawable.exo_controls_pause);
                }
                if(_isPlaying) {
                    try {
                        if (simpleExoPlayer != null) {
                            simpleExoPlayer.setPlayWhenReady(false);
                            _isPlaying = false;
                        }
                    } catch (NullPointerException e) {
                        simpleExoPlayer = mService.getplayerInstance();
                        playerView.setPlayer(simpleExoPlayer);
                    }
                    Log.d("HElloooo", "00000000000000");
                }else{
                    try {
                        if (simpleExoPlayer != null) {
                            simpleExoPlayer.setPlayWhenReady(true);
                            _isPlaying = true;
                        }
                    } catch (NullPointerException e) {
                        simpleExoPlayer = mService.getplayerInstance();
                        playerView.setPlayer(simpleExoPlayer);
                    }
                    Log.d("HElloooo", "00000000000000");
                }
            }
        });
    }

    public void startPlayerMethods(SimpleExoPlayer simpleExoPlayer) {
        playerView.setPlayer(simpleExoPlayer);
    }

    private void initializePlayer() {
        if (mBound) {
            Log.d("ciao","ciao");
            simpleExoPlayer = mService.getplayerInstance();
            playerView.setPlayer(simpleExoPlayer);
        }
    }
}