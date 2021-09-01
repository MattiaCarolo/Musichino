package com.unitn.musichino.ui.search;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.exoplayer2.Player;
import com.google.android.material.textfield.TextInputEditText;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistSelectionDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(
                        R.layout.fragment_playlist_selection_dialog, container, /* attachToRoot= */ false);
        RecyclerView playlistRecyclerView = rootView.findViewById(R.id.playlistDialogRecyclerView);

        return rootView;
    }


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Bundle b = getArguments();
        AudioModel item = b.getParcelable("item");
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item);
        PlaylistModel playlistModel = new PlaylistModel();
        List<String> playlistNames = new ArrayList<>();
        try {
            playlistNames = playlistModel.getPlaylistNamesFromSharedPreferences(requireContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayAdapter.addAll(playlistNames);
        arrayAdapter.add("Create new playlist");
        builder.setTitle(R.string.dialog_select_playlist)
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i < (arrayAdapter.getCount() - 1)) {
                            String strName = arrayAdapter.getItem(i);
                            try {
                                PlaylistModel newPlaylistModel = new PlaylistModel(requireContext(), strName);
                                newPlaylistModel.addAudioModel(item);
                                newPlaylistModel.saveToSharedPreferences(requireContext());
                                dialogInterface.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            // create new playlist
                            AlertDialog alertDialog;
                            AlertDialog.Builder innerBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), android.R.style.Theme_Material_Dialog_Alert));
                            TextInputEditText editText = new TextInputEditText(requireActivity());
                            innerBuilder.setTitle("Choose a name: ")
                                    .setView(editText)
                                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String newPlaylistName = editText.getText().toString();
                                            try {
                                                PlaylistModel newPlaylistModel = new PlaylistModel();
                                                Log.d("PLAYLIST", "Created new playlist name: " + newPlaylistName);
                                                newPlaylistModel.setName(newPlaylistName);
                                                newPlaylistModel.addAudioModel(item);
                                                newPlaylistModel.saveToSharedPreferences(builder.getContext());
                                                dialogInterface.dismiss();
                                            }
                                            catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            alertDialog = innerBuilder.show();
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
                            });
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                           // innerBuilder.show();
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }



}