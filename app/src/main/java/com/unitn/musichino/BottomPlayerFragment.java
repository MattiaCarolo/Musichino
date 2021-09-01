package com.unitn.musichino;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.service.MixMe;

import java.util.ArrayList;
import java.util.List;


public class BottomPlayerFragment extends Fragment {

    ImageButton btn_next,btn_previous,btn_play_pause;
    ImageView imageView;
    PlayerView playerView;
    AudioService mService;
    SimpleExoPlayer simpleExoPlayer;
    TextView textView;
    AudioModel item;
    private Boolean _isPlaying = true;
    MediaMetadata metadata;

    public BottomPlayerFragment() {
        // Required empty public constructor
    }

    public static BottomPlayerFragment newInstance() {
        BottomPlayerFragment fragment = new BottomPlayerFragment();
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
        View root = inflater.inflate(R.layout.fragment_bottom_player, container, false);

        btn_next = root.findViewById(R.id.btn_next);
        btn_play_pause = root.findViewById(R.id.btn_play_pause);
        btn_previous = root.findViewById(R.id.btn_prev);
        imageView = (ImageView) root.findViewById(R.id.iv_album);
        textView = root.findViewById(R.id.txt_info);
        playerView = root.findViewById(R.id.video_view);
        playerView.setControllerShowTimeoutMs(-1);
        playerView.setControllerAutoShow(true);
        playerView.setControllerHideOnTouch(false);
        playerView.showController();


        if(mService != null) {
            item = mService.currentlyPlaying;

            textView.setText(item.getName() + " - " + item.getArtist());

            simpleExoPlayer = mService.getPlayerInstance();
            playerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(new Player.Listener() {

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    item = mService.currentlyPlaying;
                    textView.setText(metadata.title + " - " + metadata.albumArtist);
                }

                @Override
                public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                    if(metadata.artworkData != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(metadata.artworkData, 0, metadata.artworkData.length);
                        imageView.setImageBitmap(bitmap);
                    }
                    else{
                        imageView.setImageResource(R.drawable.albumcover);
                    }
                }
            });
        }

        return root;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(simpleExoPlayer != null){

            btn_play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_isPlaying) {
                        simpleExoPlayer.setPlayWhenReady(false);
                        _isPlaying = !_isPlaying;
                        btn_play_pause.setImageResource(R.drawable.exo_controls_play);
                    }else{
                        simpleExoPlayer.setPlayWhenReady(true);
                        _isPlaying = !_isPlaying;
                        btn_play_pause.setImageResource(R.drawable.exo_controls_pause);
                    }
                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(simpleExoPlayer != null && simpleExoPlayer.hasPreviousWindow()){
                        simpleExoPlayer.seekToPreviousWindow();
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

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("hello","hello");
                    Intent intent = new Intent(requireContext(), PlayerActivity.class);
                    requireContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if(((MixMe)requireActivity().getApplication()).is_running()){
            mService = ((MixMe)requireActivity().getApplication()).getService();
            item = mService.currentlyPlaying;
            simpleExoPlayer = mService.getplayer();
            simpleExoPlayer.addListener(new Player.Listener() {

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    item = mService.currentlyPlaying;
                    textView.setText(item.getName() + " - " + item.getArtist());
                }

                @Override
                public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                    if(mediaMetadata.artworkData != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(mediaMetadata.artworkData, 0, mediaMetadata.artworkData.length);
                        imageView.setImageBitmap(bitmap);
                    }
                    else{
                        imageView.setImageResource(R.drawable.albumcover);
                    }
                }
            });
        }

        if(simpleExoPlayer != null){
            playerView.setPlayer(simpleExoPlayer);
            metadata = simpleExoPlayer.getMediaMetadata();
            btn_play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_isPlaying) {
                        simpleExoPlayer.setPlayWhenReady(false);
                        _isPlaying = !_isPlaying;
                        btn_play_pause.setImageResource(R.drawable.exo_controls_play);
                    }else{
                        simpleExoPlayer.setPlayWhenReady(true);
                        _isPlaying = !_isPlaying;
                        btn_play_pause.setImageResource(R.drawable.exo_controls_pause);
                    }
                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(simpleExoPlayer != null && simpleExoPlayer.hasPreviousWindow()){
                        simpleExoPlayer.seekToPreviousWindow();
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
            if(metadata.artworkData != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(metadata.artworkData, 0, metadata.artworkData.length);
                imageView.setImageBitmap(bitmap);
            }
            else{
                imageView.setImageResource(R.drawable.albumcover);
            }
        }
    }
}