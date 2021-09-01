package com.unitn.musichino.ui.player.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.ui.search.PlaylistSelectionDialog;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerHome extends Fragment {


    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private AudioService mService;
    private boolean mBound = false;
    private Button button;
    private Boolean _isPlaying = true;
    private ImageButton btn_play_pause, btn_previous, btn_next, btn_shuffle, btn_addPlaylist, btn_likeSong;
    private ImageView artworkView;
    private DefaultTimeBar defaultTimeBar;
    AudioModel item;
    TextView txt_trackname, txt_artist;
    Bundle b;

    public FragmentPlayerHome() {
        // Required empty public constructor
    }
    public FragmentPlayerHome(Bundle b) {
        // Required empty public constructor
        this.b = b;
    }

    public static FragmentPlayerHome newInstance(String param1, String param2) {
        FragmentPlayerHome fragment = new FragmentPlayerHome();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = ((PlayerActivity) requireActivity()).mService;
        simpleExoPlayer = mService.getPlayerInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player_home, container, false);
        btn_next = root.findViewById(R.id.btn_next);
        btn_addPlaylist = root.findViewById(R.id.btn_addToPlaylist);
        btn_play_pause = root.findViewById(R.id.btn_play_pause);
        btn_previous = root.findViewById(R.id.btn_prev);
        btn_shuffle = root.findViewById(R.id.btn_shuffle);
        btn_likeSong = root.findViewById(R.id.btn_likeSong);
        artworkView = root.findViewById(R.id.iv_album);
        txt_artist = root.findViewById(R.id.txt_ArtistName);
        txt_trackname = root.findViewById(R.id.txt_TrackName);

        if(simpleExoPlayer != null){
            if(simpleExoPlayer.getShuffleModeEnabled()) {
                btn_shuffle.setImageResource(R.drawable.exo_controls_shuffle_on);
            }else{
                btn_shuffle.setImageResource(R.drawable.exo_controls_shuffle_off);
            }
        }

        item = mService.currentlyPlaying;

        simpleExoPlayer.addListener(new Player.Listener() {

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                item = mService.currentlyPlaying;
                txt_trackname.setText(item.getName());
                txt_artist.setText(item.getArtist());
            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                if(mediaMetadata.artworkData != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(mediaMetadata.artworkData, 0, mediaMetadata.artworkData.length);
                    artworkView.setImageBitmap(bitmap);
                    txt_trackname.setText(mediaMetadata.title);
                    txt_artist.setText(mediaMetadata.albumArtist);

                }
                else{
                    artworkView.setImageResource(R.drawable.albumcover);
                }
            }
        });


        return root;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MediaMetadata metadata = simpleExoPlayer.getMediaMetadata();
        item = mService.currentlyPlaying;
        txt_trackname.setText(metadata.title);
        txt_artist.setText(metadata.albumArtist);
        if(metadata.artworkData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(metadata.artworkData, 0, metadata.artworkData.length);
            artworkView.setImageBitmap(bitmap);
        }

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_isPlaying) {
                    simpleExoPlayer.setPlayWhenReady(false);
                    _isPlaying = !_isPlaying;
                    btn_play_pause.setImageResource(R.drawable.exo_controls_play);
                    Log.d("HElloooo", "pause");
                }else{
                    simpleExoPlayer.setPlayWhenReady(true);
                    _isPlaying = !_isPlaying;
                    btn_play_pause.setImageResource(R.drawable.exo_controls_pause);
                    Log.d("HElloooo", "play");
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleExoPlayer != null && simpleExoPlayer.hasPreviousWindow()){
                    simpleExoPlayer.seekToPreviousWindow();
                }else if (simpleExoPlayer != null && !simpleExoPlayer.hasPreviousWindow()) {
                    simpleExoPlayer.seekToPrevious();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleExoPlayer != null && simpleExoPlayer.hasNextWindow()){
                    simpleExoPlayer.seekToNextWindow();
                }
            }
        });

        btn_addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService.currentlyPlaying != null ){
                    AudioModel audioModel = mService.currentlyPlaying;
                    DialogFragment newFragment = new PlaylistSelectionDialog();
                    Bundle b = new Bundle();
                    b.putParcelable("item", audioModel);
                    newFragment.setArguments(b);
                    newFragment.show(getParentFragmentManager(), "playlist_selection");
                }
            }
        });

        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleExoPlayer != null){
                    if(simpleExoPlayer.getShuffleModeEnabled()){
                        simpleExoPlayer.setShuffleModeEnabled(false);
                        btn_shuffle.setImageResource(R.drawable.exo_controls_shuffle_off);
                    }
                    else {
                        simpleExoPlayer.setShuffleModeEnabled(true);
                        btn_shuffle.setImageResource(R.drawable.exo_controls_shuffle_on);
                    }
                }
            }
        });

        try {
            if(item.isSongLiked(requireContext())){
                btn_likeSong.setImageResource(R.drawable.icon_liked);
                // Log.d("LIKED", "remove");
            }else {
                btn_likeSong.setImageResource(R.drawable.icon_not_liked);
                // Log.d("LIKED", "add");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_likeSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(item.isSongLiked(requireContext())){
                        item.removeFromLiked(requireContext());
                        btn_likeSong.setImageResource(R.drawable.icon_not_liked);
                       // Log.d("LIKED", "remove");
                    }else {
                        item.addToLiked(requireContext());
                        btn_likeSong.setImageResource(R.drawable.icon_liked);
                       // Log.d("LIKED", "add");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}