package com.unitn.musichino.ui.player.Settings;

import android.os.Bundle;

import androidx.annotation.Dimension;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.text.Cue;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.TrackAdapter;
import com.unitn.musichino.interfaces.ButtonTrackClickListener;
import com.unitn.musichino.ui.player.Settings.FragmentPlayerSettings;
import com.unitn.musichino.ui.player.Settings.VolumeFragment;

import java.util.List;

public class SettingsHUDFragment extends Fragment implements ButtonTrackClickListener {

    RecyclerView recyclerView;
    SimpleExoPlayer simpleExoPlayer;
    List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;
    

    public SettingsHUDFragment(SimpleExoPlayer simpleExoPlayer, List<MediaCodecAudioRenderer>mediaCodecAudioRendererList) {
        // Required empty public constructor
        this.simpleExoPlayer = simpleExoPlayer;
        this.mediaCodecAudioRendererList = mediaCodecAudioRendererList;
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentPlayerSettings newInstance(String param1, String param2) {
        FragmentPlayerSettings fragment = new FragmentPlayerSettings();

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
        View root = inflater.inflate(R.layout.fragment_player_hud, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        /*
        btn_volumes = view.findViewById(R.id.btn_volumes);
        btn_volumes.setOnClickListener(this);
        ViewCompat.setTransitionName(btn_volumes, "title");

         */
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_tracks);

    }

    @Override
    public void onStart() {
        super.onStart();

        TrackAdapter trackAdapter = new TrackAdapter(simpleExoPlayer,mediaCodecAudioRendererList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(trackAdapter);
    }


    @Override
    public void onButtonTrackClick(int pos, Button button, MediaCodecAudioRenderer mediaCodecAudioRenderer) {
        Fragment fragment = new VolumeFragment(pos);
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.hud_settings, fragment);
        transaction.addSharedElement(button, "titolo");
        transaction.addToBackStack(null);
        transaction.commit();

    }
}