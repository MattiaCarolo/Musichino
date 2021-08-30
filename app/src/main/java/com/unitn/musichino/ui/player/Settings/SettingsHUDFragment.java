package com.unitn.musichino.ui.player.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PresetModel;
import com.unitn.musichino.Models.TrackConfigurationModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.BarEqualizerAdapter;
import com.unitn.musichino.adapter.VolumesBarAdapter;
import com.unitn.musichino.interfaces.ButtonTrackClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsHUDFragment extends Fragment implements ButtonTrackClickListener {
    Button volumePresetButton;
    Button eqPresetButton;
    RecyclerView eqRecyclerView;
    RecyclerView recyclerView;
    SimpleExoPlayer simpleExoPlayer;
    List<MediaCodecAudioRenderer> mediaCodecAudioRendererList;
    AudioModel currentlyPlaying;
    TrackConfigurationModel trackConfigurationModel;
    

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
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_volumes);
        eqRecyclerView = view.findViewById(R.id.rv_eq);
        volumePresetButton = view.findViewById(R.id.volumePresetListButton);
        eqPresetButton = view.findViewById(R.id.eqPresetListButton);

    }

    @Override
    public void onStart() {
        super.onStart();

        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                VolumesBarAdapter volumesBarAdapter = new VolumesBarAdapter(simpleExoPlayer,  mediaCodecAudioRendererList);

                Equalizer mEqualizer = new Equalizer(0, simpleExoPlayer.getAudioSessionId());
                BarEqualizerAdapter eqBarAdapter = new BarEqualizerAdapter(requireContext(), mEqualizer);
                //TrackAdapter trackAdapter = new TrackAdapter(simpleExoPlayer,mediaCodecAudioRendererList,this);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(volumesBarAdapter);
                eqRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                eqRecyclerView.setAdapter(eqBarAdapter);
                currentlyPlaying = ((PlayerActivity) requireActivity()).mService.currentlyPlaying;
                trackConfigurationModel = new TrackConfigurationModel();
                try {
                    trackConfigurationModel = trackConfigurationModel.getTrackConfigurationFromSharedPreferences(requireContext(), currentlyPlaying.getName());
                    Log.d("COMPARE", "conf: " +trackConfigurationModel.getTrackReference()+", current:" +currentlyPlaying.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!trackConfigurationModel.getTrackReference().equals( "trackReference") && trackConfigurationModel.getVolumePresetModels().size() > 0){
                    volumesBarAdapter.setVolumePreset(trackConfigurationModel.getVolumePresetModels().get(0).getValues());
                    Log.d("PRESETVOLUME", "Set preset volume: " + trackConfigurationModel.getVolumePresetModels().get(0).getName());
                }
                else{
                    PresetModel defaultVolumePreset = new PresetModel("default", volumesBarAdapter.getItemCount(), true);
                    PresetModel defaultEqPreset = new PresetModel("default", eqBarAdapter.getItemCount(), false);
                    trackConfigurationModel.getVolumePresetModels().add(defaultVolumePreset);
                    trackConfigurationModel.getEqPresetModels().add(defaultEqPreset);
                    trackConfigurationModel.setTrackReference(currentlyPlaying.getPath());
                    try {
                        trackConfigurationModel.saveToSharedPreferences(requireContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        volumePresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(requireContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());

                popup.show();

            }
        });

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

    @Override
    public void onPrepareOptionsMenu(@androidx.annotation.NonNull Menu menu) {
        List<PresetModel> volumePresets = trackConfigurationModel.getVolumePresetModels();
        for(PresetModel volumePreset : volumePresets){
            menu.add(volumePreset.getName());
        }

        super.onPrepareOptionsMenu(menu);
    }
}