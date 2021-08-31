package com.unitn.musichino.ui.playlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.R;

import org.json.JSONException;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class PlaylistSettingsDialog extends DialogFragment {

    String playlistName;
    String playlistArtworkUri;
    ImageView playlistArtwork;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        playlistName = b.getString("name");
        playlistArtworkUri = b.getString("artworkUri");

        AlertDialog alertDialog;
        AlertDialog.Builder innerBuilder = new AlertDialog.Builder(requireContext());
        TextInputEditText editText = new TextInputEditText(requireContext());
        playlistArtwork = new ImageView(requireContext());
        if (playlistArtworkUri.equals("artworkUri") || playlistArtworkUri.equals("assets:///default_cover.jpg")) {
            playlistArtwork.setImageResource(R.drawable.albumcover);
            Log.d("ARTWORK", "max h: " +playlistArtwork.getMaxHeight()+" max w: " +playlistArtwork.getMaxWidth());
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
        innerBuilder.setTitle("Customize playlist")
                .setView(playlistArtwork)
                .setView(nameView)
                .setView(editText)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PlaylistModel newPlaylistModel = new PlaylistModel();
                        try {
                            newPlaylistModel = newPlaylistModel.loadFromSharedPreferencesByName(requireContext(), playlistName);
                            newPlaylistModel.removePlaylistModelFromSharedPreferences(requireContext(), playlistName);
                            newPlaylistModel.setName(editText.getText().toString());
                            newPlaylistModel.setArtworkUri(playlistArtworkUri);
                            newPlaylistModel.saveToSharedPreferences(requireContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return innerBuilder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        if (requestCode == 1 && resultCode == RESULT_OK && i != null) {
            playlistArtworkUri = i.getData().toString();
            playlistArtwork.setImageDrawable(Drawable.createFromPath(playlistArtworkUri));

        }
    }
}
