package com.unitn.musichino.ui.player.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
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
    public AudioService mService;
    private boolean mBound = false;
    AudioModel item;

    public FragmentPlayerHome() {
        // Required empty public constructor
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.LocalBinder binder = (AudioService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            initializePlayer();
            if(mService.currentlyPlaying != null){
                Log.d("onServiceConnected", "Currently playing: " + mService.currentlyPlaying.getPath());
                mService.changeSong(Uri.parse(item.getPath()));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player_home, container, false);

        playerView = root.findViewById(R.id.video_view);
        return root;
    }

    private void initializePlayer() {
        if (mBound) {
            simpleExoPlayer = mService.getplayerInstance();
            playerView.setPlayer(simpleExoPlayer);
            playerView.setUseController(true);
            playerView.showController();
            playerView.setUseArtwork(false);
            playerView.setControllerAutoShow(true);
            playerView.setControllerHideOnTouch(false);
        }
    }
}