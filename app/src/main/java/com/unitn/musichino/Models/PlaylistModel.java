package com.unitn.musichino.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.MediaItem;
import com.unitn.musichino.util.C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaylistModel {
    private static final String PLAYLIST = "playlist";
    private static final String PLAYLIST_NAME = "name";
    private static final String PLAYLIST_ARTWORK_URI = "artwork_uri";
    private String name;
    private String artworkUri;
    private List<AudioModel> playlist;

    public PlaylistModel(Context context, String name) throws JSONException {
        PlaylistModel model = loadFromSharedPreferencesByName(context, name);
        if(model != null) {
            this.name = model.name;
            this.artworkUri = model.artworkUri;
            this.playlist = model.playlist;
        }
        else{
            Log.d("PlaylistModel", "Cannot find playlist in shared preferences given name: " + name);
        }
    }

    public PlaylistModel(List<AudioModel> playlist, String name, String artworkUri) {
        this.playlist = playlist;
        this.name = name;
        this.artworkUri = artworkUri;
    }

    public PlaylistModel(){
        this.playlist = new ArrayList<>();
        this.name = PLAYLIST_NAME;
        this.artworkUri = C.DEFAULT_ARTWORK_URI;
    }

    public PlaylistModel(JSONObject playlistModelJSON) throws JSONException {
        playlist = new ArrayList<>();
        this.name = playlistModelJSON.getString(PLAYLIST_NAME);
        this.artworkUri = playlistModelJSON.getString(PLAYLIST_ARTWORK_URI);
        JSONArray playlistJSON = playlistModelJSON.getJSONArray(PLAYLIST);
        for(int i = 0; i < playlistJSON.length(); i++){
            JSONObject audioJSON = playlistJSON.getJSONObject(i);
            playlist.add(new AudioModel(audioJSON));
        }
    }

    public String getArtworkUri() {
        return artworkUri;
    }

    public void setArtworkUri(String artworkUri) {
        this.artworkUri = artworkUri;
    }

    public List<AudioModel> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<AudioModel> playlist) {
        this.playlist = playlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAudioModel(AudioModel audioModel) {
        this.playlist.add(audioModel);
    }

    public void removeAudioModel(int i){
        this.playlist.remove(i);
    }

    public void swapAudioModels(int i, int j){
        AudioModel a = this.playlist.get(i);
        AudioModel b = this.playlist.get(j);
        this.playlist.set(j, a);
        this.playlist.set(i, b);
    }

    public List<MediaItem> getPlaylistMediaItems(){
        List<MediaItem> mediaItems = new ArrayList<>();
        for(AudioModel audio : playlist){
            mediaItems.add(MediaItem.fromUri(Uri.parse(audio.getPath())));
        }
        return mediaItems;
    }

    public JSONArray getJSONPlaylist() throws JSONException {
        JSONArray playlistJSON = new JSONArray();
        for(AudioModel audio : playlist){
            playlistJSON.put(audio.getJSONAudioModel());
        }
        return playlistJSON;
    }

    public JSONObject getJSONPlaylistModel() throws  JSONException{
        JSONObject playlistModelJSON = new JSONObject();
        playlistModelJSON.put(PLAYLIST_NAME, this.name);
        playlistModelJSON.put(PLAYLIST_ARTWORK_URI, this.artworkUri);
        JSONArray playlistJSON = new JSONArray();
        for(AudioModel audio : playlist){
            playlistJSON.put(audio.getJSONAudioModel());
        }
        playlistModelJSON.put(PLAYLIST, playlistJSON);
        return playlistModelJSON;
    }

    public void saveToSharedPreferences(Context context) throws JSONException {
        SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPlaylists.edit();
        JSONArray namesJSON = new JSONArray();
        JSONArray playlistsJSON = new JSONArray();
        boolean found = false;
        if(editor != null){
            namesJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
            playlistsJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS, "[]"));
            for (int i = 0; i < namesJSON.length(); i++) {
                if (namesJSON.getString(i).equals(this.name)) {
                    playlistsJSON.put(i, this.getJSONPlaylistModel());
                    found = true;
                }
            }
        }
        if(!found) {
            namesJSON.put(this.name);
            playlistsJSON.put(this.getJSONPlaylistModel());
            editor.putString(C.SHARED_PLAYLISTS_NAMES, namesJSON.toString()).apply();
        }
        editor.putString(C.SHARED_PLAYLISTS, playlistsJSON.toString()).apply();
        Log.d("SHARED", "playlist:" +context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE).getString(C.SHARED_PLAYLISTS, "[]"));
    }

    public PlaylistModel loadFromSharedPreferencesByName(Context context, String name) throws  JSONException{
     SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
     if(sharedPlaylists != null) {
         JSONArray namesJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
         for (int i = 0; i < namesJSON.length(); i++) {
             if (namesJSON.getString(i).equals(name)) {
                 PlaylistModel model = new PlaylistModel(new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS, "[]")).getJSONObject(i));
                 Log.d("LOADSHARE", "Found name at index: " +i+", model: " + model.getName());
                 return model;
             }
         }
     }
     return null;
    }

    public List<String> getPlaylistNamesFromSharedPreferences(Context context) throws JSONException {
        SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
        if(sharedPlaylists != null) {
            JSONArray namesJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
            List<String> playlistNames = new ArrayList<>();
            Log.d("PLAYLIST", "Total names: " +namesJSON.length());
            for(int i = 0; i < namesJSON.length(); i++){
                playlistNames.add(namesJSON.getString(i));
            }
            return playlistNames;
        }
        return null;
    }

    public List<PlaylistModel> getPlaylistModelsFromSharedPreferences(Context context) throws  JSONException{
        SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
        if(sharedPlaylists != null){
            JSONArray playlistModelsJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
            List<PlaylistModel> playlistModels = new ArrayList<>();
            for(int i = 0; i < playlistModelsJSON.length(); i++){
                playlistModels.add(new PlaylistModel(playlistModelsJSON.getJSONObject(i)));
            }
            return playlistModels;
        }
        return null;
    }

}
