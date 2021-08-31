package com.unitn.musichino.ui.player.Settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.material.textfield.TextInputEditText;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.Models.PresetModel;
import com.unitn.musichino.Models.TrackConfigurationModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.TrackSettingAdapter;
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
    VolumesBarAdapter volumesBarAdapter;
    BarEqualizerAdapter eqBarAdapter;
    Equalizer mEqualizer;


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
        Context context = requireContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_volumes);
        eqRecyclerView = view.findViewById(R.id.rv_eq);
        volumePresetButton = view.findViewById(R.id.volumePresetListButton);
        eqPresetButton = view.findViewById(R.id.eqPresetListButton);
        mEqualizer = new Equalizer(1000, simpleExoPlayer.getAudioSessionId());
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                volumesBarAdapter = new VolumesBarAdapter(simpleExoPlayer,  mediaCodecAudioRendererList);
                eqBarAdapter = new BarEqualizerAdapter(context, mEqualizer);
                //TrackAdapter trackAdapter = new TrackAdapter(simpleExoPlayer,mediaCodecAudioRendererList,this);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(volumesBarAdapter);
                eqRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                eqRecyclerView.setAdapter(eqBarAdapter);
                currentlyPlaying = ((PlayerActivity) context).mService.currentlyPlaying;
                trackConfigurationModel = new TrackConfigurationModel();
                try {
                    trackConfigurationModel = trackConfigurationModel.getTrackConfigurationFromSharedPreferences(context, currentlyPlaying.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!trackConfigurationModel.getTrackReference().equals( "trackReference") && trackConfigurationModel.getVolumePresetModels().size() > 0){
                    volumesBarAdapter.setVolumePreset(trackConfigurationModel.getVolumePresetModels().get(0).getValues());

                    Log.d("PRESETVOLUME", "Set preset volume: " + trackConfigurationModel.getVolumePresetModels().get(0).getName());
                }
                else if(!trackConfigurationModel.getTrackReference().equals( "trackReference") && trackConfigurationModel.getEqPresetModels().size() > 0){
                    eqBarAdapter.setEqPreset(trackConfigurationModel.getEqPresetModels().get(0).getValues());
                    Log.d("PRESETEQ", "Set preset eq: " + trackConfigurationModel.getEqPresetModels().get(0).getName());
                }
                else{
                    PresetModel defaultVolumePreset = new PresetModel("default", volumesBarAdapter.getItemCount(), true);
                    PresetModel defaultEqPreset = new PresetModel("default", eqBarAdapter.getItemCount(), false);
                    List<PresetModel> volumeList = new ArrayList<>();
                    List<PresetModel> eqList = new ArrayList<>();
                    volumeList.add(defaultVolumePreset);
                    eqList.add(defaultEqPreset);
                    trackConfigurationModel.setVolumePresetModels(volumeList);
                    trackConfigurationModel.setEqPresetModels(eqList);
                    trackConfigurationModel.setTrackReference(currentlyPlaying.getName());
                    try {
                        trackConfigurationModel.saveToSharedPreferences(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        volumePresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requireActivity().invalidateOptionsMenu();
                PopupMenu popup = new PopupMenu(requireContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();
                List<PresetModel> volumePresets = trackConfigurationModel.getVolumePresetModels();
                Log.d("MENUPREPARE", "Preparing list size: " + volumePresets.size());
                for(PresetModel volumePreset : volumePresets){
                    menu.add(volumePreset.getName()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            volumesBarAdapter.setVolumePreset(volumePreset.getValues());
                            return false;
                        }
                    });
                }
                menu.add("Add new").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog alertDialog;
                        AlertDialog.Builder innerBuilder = new AlertDialog.Builder(requireActivity());
                        TextInputEditText editText = new TextInputEditText(requireActivity());
                        innerBuilder.setTitle("Choose a name: ")
                                .setView(editText)
                                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String newPresetName = editText.getText().toString();
                                        try {
                                            PresetModel newPresetModel = new PresetModel();
                                            Log.d("PRESET", "Created new preset name: " + newPresetName);
                                            newPresetModel.setName(newPresetName);
                                            newPresetModel.setValues(volumesBarAdapter.getVolumePreset());
                                            trackConfigurationModel.addVolumePresetModel(newPresetModel);
                                            trackConfigurationModel.saveToSharedPreferences(requireContext());
                                            dialogInterface.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        alertDialog = innerBuilder.show();/*
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String newPlaylistName = editText.getText().toString();
                                Log.d("ONKEY", "Listener activated: " +newPlaylistName);
                                try {
                                    PlaylistModel model = playlistModel.loadFromSharedPreferencesByName(builder.getContext(), newPlaylistName);
                                    AlertDialog dialog = alertDialog;
                                    if(model == null && !newPlaylistName.equals("")){
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                        editText.setError(null);
                                    }
                                    else{
                                        editText.setError("Unvalid playlist name");
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });*/
                        //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        return false;
                    }

                });

                        popup.show();

                    }
        });

        eqPresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requireActivity().invalidateOptionsMenu();
                PopupMenu popup = new PopupMenu(requireContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();
                List<PresetModel> eqPresets = trackConfigurationModel.getEqPresetModels();
                Log.d("MENUPREPARE", "Preparing list size: " + eqPresets.size());
                for(PresetModel eqPreset : eqPresets){
                    menu.add(eqPreset.getName()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            eqBarAdapter.setEqPreset(eqPreset.getValues());
                            return false;
                        }
                    });
                }
                menu.add("Add new").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog alertDialog;
                        AlertDialog.Builder innerBuilder = new AlertDialog.Builder(requireActivity());
                        TextInputEditText editText = new TextInputEditText(requireActivity());
                        innerBuilder.setTitle("Choose a name: ")
                                .setView(editText)
                                .setInverseBackgroundForced(true)
                                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String newPresetName = editText.getText().toString();
                                        try {
                                            PresetModel newPresetModel = new PresetModel();
                                            Log.d("PRESET", "Created new preset name: " + newPresetName);
                                            newPresetModel.setName(newPresetName);
                                            newPresetModel.setValues(eqBarAdapter.getEqPreset());
                                            trackConfigurationModel.addEqPresetModel(newPresetModel);
                                            trackConfigurationModel.saveToSharedPreferences(requireContext());
                                            dialogInterface.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        alertDialog = innerBuilder.show();/*
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String newPlaylistName = editText.getText().toString();
                                Log.d("ONKEY", "Listener activated: " +newPlaylistName);
                                try {
                                    PlaylistModel model = playlistModel.loadFromSharedPreferencesByName(builder.getContext(), newPlaylistName);
                                    AlertDialog dialog = alertDialog;
                                    if(model == null && !newPlaylistName.equals("")){
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                        editText.setError(null);
                                    }
                                    else{
                                        editText.setError("Unvalid playlist name");
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });*/
                        //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        return false;
                    }

                });

                popup.show();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
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