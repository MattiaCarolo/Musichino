package com.unitn.musichino.ui.playlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PlaylistItemRecyclerViewAdapter;

import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class PlaylistSettingsDialog extends DialogFragment {

    String playlistName;
    String playlistArtworkUri;
    ImageView playlistArtwork;
    Bitmap bitmap;
    String overwrite = "overwrite";
    TextInputEditText editText;
    AlertDialog alertDialog;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        Context context = requireContext();
        playlistName = b.getString("name");
        playlistArtworkUri = b.getString("artworkUri");
        RecyclerView recyclerView = getParentFragment().getView().findViewById(R.id.rv_playlists);
        AlertDialog.Builder innerBuilder = new AlertDialog.Builder(requireContext());
        editText = new TextInputEditText(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        playlistArtwork = new ImageView(requireContext());
        if (playlistArtworkUri.equals("artwork_uri") || playlistArtworkUri.equals("assets:///default_cover.jpg")) {
            playlistArtwork.setImageResource(R.drawable.albumcover);
        } else {
            playlistArtwork.setImageDrawable(Drawable.createFromPath(playlistArtworkUri));
        }
        TextView nameView = new TextView(requireContext());
        nameView.setText("Change name:");
        editText.setText(playlistName);
        playlistArtwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Update playlist artwork"), 1);

            }
        });
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(playlistArtwork);
        linearLayout.addView(nameView);
        linearLayout.addView(editText);
        innerBuilder.setTitle("Customize playlist")
                .setView(linearLayout)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PlaylistModel newPlaylistModel = new PlaylistModel();
                        try {
                            if (!playlistArtworkUri.equals("artwork_uri")) {
                                FileOutputStream fileOutputStream = requireContext().openFileOutput(editText.getText().toString(), Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                fileOutputStream.close();
                                playlistArtworkUri = requireActivity().getFilesDir().getPath() + "/" + editText.getText().toString();
                            }
                            newPlaylistModel = newPlaylistModel.loadFromSharedPreferencesByName(requireContext(), playlistName);
                            newPlaylistModel.removePlaylistModelFromSharedPreferences(requireContext(), playlistName);
                            newPlaylistModel.setName(editText.getText().toString());
                            newPlaylistModel.setArtworkUri(playlistArtworkUri);
                            newPlaylistModel.saveToSharedPreferences(requireContext());
                            List<PlaylistModel> playlistModels = newPlaylistModel.getPlaylistModelsFromSharedPreferences(requireContext());
                            ((PlaylistItemRecyclerViewAdapter) recyclerView.getAdapter()).setPlaylistModels(playlistModels);
                            dialogInterface.dismiss();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(requireContext());
                        confirmDialog.setTitle("Are you sure?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        PlaylistModel tmpPlaylistModel = new PlaylistModel();
                                        try {
                                            tmpPlaylistModel.removePlaylistModelFromSharedPreferences(context, playlistName);
                                            List<PlaylistModel> playlistModels = tmpPlaylistModel.getPlaylistModelsFromSharedPreferences(context);
                                            ((PlaylistItemRecyclerViewAdapter) recyclerView.getAdapter()).setPlaylistModels(playlistModels);
                                            dialogInterface.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();


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
                    PlaylistModel model = new PlaylistModel();
                    model = model.loadFromSharedPreferencesByName(requireContext(), newPlaylistName);
                    AlertDialog dialog = alertDialog;
                    if ((model == null && !newPlaylistName.equals("")) || newPlaylistName.equals(overwrite)) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        editText.setError(null);
                    } else {
                        editText.setError("Unvalid playlist name");
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        if (requestCode == 1 && resultCode == RESULT_OK && i != null) {
            playlistArtworkUri = i.getData().toString();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), Uri.parse(playlistArtworkUri));
                overwrite = playlistName;
                editText.setError(null);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                playlistArtwork.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
