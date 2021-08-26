package com.unitn.musichino.ui.player.Settings;

import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.VolumesBarAdapter;
import com.unitn.musichino.uikit.SettingsTransition;

import java.util.List;

public class VolumeFragment extends Fragment {
    private Button btn_title;
    private ConstraintLayout constraintLayout;
    private SimpleExoPlayer simpleExoPlayer;
    private List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;
    private RecyclerView recyclerView;
    private VolumesBarAdapter volumesBarAdapter;

    public VolumeFragment() {
        // Required empty public constructor
    }

    public static VolumeFragment newInstance() {
        VolumeFragment fragment = new VolumeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new SettingsTransition());
        setSharedElementReturnTransition(new SettingsTransition());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_volume, container, false);
        recyclerView = root.findViewById(R.id.rv_volumes);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        btn_title = view.findViewById(R.id.btn_volumes_title);
        constraintLayout = view.findViewById(R.id.lay_frag_volume);
        ViewCompat.setTransitionName(btn_title, "titolo");
        ViewCompat.setTransitionName(constraintLayout, "sfondo");

        postponeEnterTransition();
        startPostponedEnterTransition();
    }

    @Override
    public void onStart() {
        super.onStart();
        simpleExoPlayer = ((PlayerActivity) getActivity()).getPlayer();
        mediaCodecAudioRendererList = ((PlayerActivity) getActivity()).mService.renderers;
        volumesBarAdapter = new VolumesBarAdapter(simpleExoPlayer,mediaCodecAudioRendererList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(volumesBarAdapter);
    }
}