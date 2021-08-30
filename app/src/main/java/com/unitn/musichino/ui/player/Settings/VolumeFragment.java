package com.unitn.musichino.ui.player.Settings;

import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.BarEqualizerAdapter;
import com.unitn.musichino.adapter.VolumesBarAdapter;
import com.unitn.musichino.uikit.SettingsTransition;

import java.util.List;

public class VolumeFragment extends Fragment {
    private Button btn_title;
    private SeekBar seekBar;
    private ConstraintLayout constraintLayout;
    private SimpleExoPlayer simpleExoPlayer;
    private List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;
    private int pos;
    private MediaCodecAudioRenderer mediaCodecAudioRenderer;
    private RecyclerView recyclerView;
    private VolumesBarAdapter volumesBarAdapter;
    private Equalizer mEqualizer;
    public BarEqualizerAdapter barEqualizerAdapter;

    public VolumeFragment() {
        // Required empty public constructor
    }

    public VolumeFragment(int pos) {
        // Required empty public constructor
        this.pos = pos;
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
        seekBar = root.findViewById(R.id.bar_volumebar);
        mEqualizer = new Equalizer(0, ((PlayerActivity)requireActivity()).getPlayer().getAudioSessionId());
        mEqualizer.setEnabled(true);
        recyclerView = root.findViewById(R.id.rv_equalizer);
        barEqualizerAdapter = new BarEqualizerAdapter(getContext(),mEqualizer);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(barEqualizerAdapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        btn_title = view.findViewById(R.id.btn_volumes_title);
        constraintLayout = view.findViewById(R.id.lay_frag_volume);
        ViewCompat.setTransitionName(btn_title, "titolo");

        postponeEnterTransition();
        startPostponedEnterTransition();
    }

    @Override
    public void onStart() {
        super.onStart();
        simpleExoPlayer = ((PlayerActivity) getActivity()).getPlayer();
        mediaCodecAudioRendererList = ((PlayerActivity) getActivity()).mService.renderers;

        MediaCodecAudioRenderer renderer = mediaCodecAudioRendererList.get(pos);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
              //  if(fromUser){
                    float value = i / (float)seekBar.getMax();
                    simpleExoPlayer.createMessage(renderer).setType(C.MSG_SET_VOLUME).setPayload(value).send();

               // }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}