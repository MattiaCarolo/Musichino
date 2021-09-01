package com.unitn.musichino.ui.player.lyrics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.player.MultiAudioTrackSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment responsabile della gestione delle lyrics.
 * Consiste principalmente in un'estensione della subtitleView di Exoplayer,
 * che viene popolata dalla textComponent del player. Viene impostato un listener
 * OnCue nel quale vengono aggiunte le lyrics nuove e dinamicamente spostate ed eventualmente eliminate
 * le stringhe precedenti, simulando un'esperienza dinamica. Sfortunatamente per visualizzare le lyrics "future"
 * avremo bisogno di un file esterno da leggere prima di far partire il player, ma per come sono strutturati i file
 * del progetto, tutte le tracce relative al brano sono interne al file e vengono analizzate soltanto in runtime.
 */
public class FragmentPlayerLyrics extends Fragment {

    private static final int CUE_LINE_COUNT = 12;
    private static final int CUE_LINE_DECREMENT = 3;
    private static final int CUE_TEXT_SIZE = 28;
    private SubtitleView subtitleView;
    private TextView noLyricsView;
    List<Cue> pastCues = new ArrayList<>();

    public FragmentPlayerLyrics() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player_lyrics, container, false);
        subtitleView = root.findViewById(R.id.exo_subtitles_view);
        noLyricsView = root.findViewById(R.id.txt_noLyrics);
        subtitleView.setFixedTextSize(Dimension.SP, CUE_TEXT_SIZE);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        SimpleExoPlayer simpleExoPlayer = ((PlayerActivity) requireActivity()).getPlayer();
        if (((MultiAudioTrackSelector) Objects.requireNonNull(simpleExoPlayer.getTrackSelector())).textRendererCount == 0) {
            subtitleView.setVisibility(View.GONE);
            noLyricsView.setVisibility(View.VISIBLE);
        } else {
            subtitleView.setVisibility(View.VISIBLE);
            noLyricsView.setVisibility(View.GONE);
        }
        simpleExoPlayer.addListener(new Player.Listener() {
            /*
                Al cambio delle tracce selezionate, controlla se esiste una traccia di tipo testo
                e se assente imposta una textView che avverte l'utente della mancanza
            */
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                boolean trackContainsText = false;
                for (int i = 0; i < trackSelections.length; i++) {
                    TrackSelection trackSelection = trackSelections.get(i);
                    if (trackSelection != null) {
                        String trackMime = trackSelection.getFormat(0).sampleMimeType;
                        int trackType = MimeTypes.getTrackType(trackMime);
                        if (trackType == C.TRACK_TYPE_TEXT) {
                            trackContainsText = true;
                            break;
                        }
                    }
                }
                if (!trackContainsText) {
                    subtitleView.setVisibility(View.GONE);
                    noLyricsView.setVisibility(View.VISIBLE);
                } else {
                    subtitleView.setVisibility(View.VISIBLE);
                    noLyricsView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCues(List<Cue> cues) {
                if (cues.size() > 1) {
                    Log.d("CUES", "There are more than one cues available.");
                }
                for (Cue cue : cues) {
                    assert cue.text != null;
                    if (!cue.text.toString().equals(" ")) {
                        cue = cue.buildUpon().setWindowColor(Color.TRANSPARENT).setLine(CUE_LINE_COUNT, Cue.LINE_TYPE_NUMBER).setLineAnchor(Cue.ANCHOR_TYPE_MIDDLE).setPositionAnchor(Cue.ANCHOR_TYPE_MIDDLE).build();
                        for (int i = 0; i < pastCues.size(); i++) {
                            Cue pastCue = pastCues.get(i).buildUpon().setLine((pastCues.get(i).line - CUE_LINE_DECREMENT), Cue.LINE_TYPE_NUMBER).build();
                            pastCues.set(i, pastCue);
                        }
                        if (pastCues.size() > 0 && pastCues.get(0).line <= 0) {
                            pastCues.remove(0);
                        }
                        pastCues.add(cue);
                        subtitleView.setCues(pastCues);
                    }
                }
            }

            /*
                In caso l'utente muova la seekbar della canzone, per evitare inconsistenze
                con la visualizzazione delle lyrics, viene pulita pastCues contenente le lyrics passate
                e svuotata subtitleView. Stesso in caso venga cambiata canzone.
             */
            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                pastCues = new ArrayList<>();
                subtitleView.setCues(null);
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                pastCues = new ArrayList<>();
                subtitleView.setCues(null);
            }
        });
    }
}