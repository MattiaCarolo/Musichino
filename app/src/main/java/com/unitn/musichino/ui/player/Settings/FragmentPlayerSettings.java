package com.unitn.musichino.ui.player.Settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;

import java.util.List;
import java.util.Objects;


public class FragmentPlayerSettings extends Fragment {

    private SimpleExoPlayer simpleExoPlayer;
    private List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleExoPlayer = ((PlayerActivity) requireActivity()).getPlayer();
        mediaCodecAudioRendererList = ((PlayerActivity) requireActivity()).mService.renderers;
        SettingsHUDFragment settingsHUDFragment = new SettingsHUDFragment(simpleExoPlayer,mediaCodecAudioRendererList);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.hud_settings, settingsHUDFragment, settingsHUDFragment.getClass().getName());
        fragmentTransaction.commit();

    }
}