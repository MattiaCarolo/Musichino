package com.unitn.musichino.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.unitn.musichino.util.C;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
    Classe che incapsula le informazioni riguardanti i file audio,
    usati per popolare i metadati e salvare/caricare i percorsi.
*/


public class AudioModel implements Parcelable {
    String path;
    String name;
    String album;
    String artist;
    String format;

    /*
        Costruttore Parcelable per facilitare le transizioni con gli Intent
        tramite Intent.putParcelable()
    */
    protected AudioModel(Parcel in) {
        path = in.readString();
        name = in.readString();
        album = in.readString();
        artist = in.readString();
        format = in.readString();
    }

    /*
        Costruttore di default
    */
    public AudioModel() {
        path = "path";
        name = "name";
        album = "album";
        artist = "artist";
        format = "format";
    }

    /*
        Costruttore tramite JSON usato per mantenere una consistenza dei dati
        nel salvataggio e caricamento delle informazioni nelle SharedPreferences
    */
    public AudioModel(JSONObject audioJSON) throws JSONException {
        path = audioJSON.getString("path");
        name = audioJSON.getString("name");
        album = audioJSON.getString("album");
        artist = audioJSON.getString("artist");
        format = audioJSON.getString("format");
    }

    /*
        Metodo per convertire AudioModel in oggetto JSON
    */
    public JSONObject getJSONAudioModel() throws JSONException {
        JSONObject audioJSON = new JSONObject();
        audioJSON.put("path", path);
        audioJSON.put("name", name);
        audioJSON.put("album", album);
        audioJSON.put("artist", artist);
        audioJSON.put("format", format);
        return audioJSON;
    }

    /*
        Creator e metodi override obbligatori per implementare Parcelable
    */
    public static final Creator<AudioModel> CREATOR = new Creator<AudioModel>() {
        @Override
        public AudioModel createFromParcel(Parcel in) {
            return new AudioModel(in);
        }

        @Override
        public AudioModel[] newArray(int size) {
            return new AudioModel[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @NotNull
    @Override
    public String toString() {
        return ("Name:" + getName() + ", Artist: " + getArtist() + ", Album: " + getAlbum());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*
        Metodo override obbligatorio per popolare la classe da un Parcelable
    */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(name);
        parcel.writeString(album);
        parcel.writeString(artist);
        parcel.writeString(format);
    }

    public void addToLiked(Context context) throws JSONException {
        SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
        JSONArray playlistModelsJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS, "[]"));
        JSONArray playlistNamesJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
        boolean found = false;
        for (int i = 0; i < playlistNamesJSON.length(); i++) {
            if (playlistNamesJSON.getString(i).equals(C.SHARED_PLAYLISTS_LIKED)) {
                JSONArray playlistItemsJSON = new JSONArray();
                PlaylistModel playlistModel = new PlaylistModel(playlistModelsJSON.getJSONObject(i));
                playlistModel.addAudioModel(this);
                playlistModel.saveToSharedPreferences(context);
                found = true;
            }
        }
        if (!found) {
            PlaylistModel playlistModel = new PlaylistModel();
            playlistModel.addAudioModel(this);
            playlistModel.setName(C.SHARED_PLAYLISTS_LIKED);
            playlistModel.setArtworkUri(C.SHARED_PLAYLISTS_LIKED);
            playlistModel.saveToSharedPreferences(context);
        }

    }

    public void removeFromLiked(Context context) throws JSONException {
        SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
        JSONArray playlistModelsJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS, "[]"));
        JSONArray playlistNamesJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
        for (int i = 0; i < playlistNamesJSON.length(); i++) {
            if (playlistNamesJSON.getString(i).equals(C.SHARED_PLAYLISTS_LIKED)) {
                JSONArray playlistItemsJSON;
                PlaylistModel playlistModel = new PlaylistModel(playlistModelsJSON.getJSONObject(i));
                playlistItemsJSON = playlistModel.getJSONPlaylist();
                for (int j = 0; j < playlistItemsJSON.length(); j++) {
                    if (playlistItemsJSON.getJSONObject(j).getString("name").equals(this.name)) {
                        playlistItemsJSON.remove(j);
                        playlistModel.setPlaylist(playlistItemsJSON);
                        playlistModelsJSON.put(i, playlistModel.getJSONPlaylistModel());
                    }
                }
            }
        }
        sharedPlaylists.edit().putString(C.SHARED_PLAYLISTS, playlistModelsJSON.toString()).apply();
    }

    public boolean isSongLiked(Context context) throws JSONException {
        SharedPreferences sharedPlaylists = context.getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, Context.MODE_PRIVATE);
        JSONArray playlistModelsJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS, "[]"));
        JSONArray playlistNamesJSON = new JSONArray(sharedPlaylists.getString(C.SHARED_PLAYLISTS_NAMES, "[]"));
        for (int i = 0; i < playlistNamesJSON.length(); i++) {
            if (playlistNamesJSON.getString(i).equals(C.SHARED_PLAYLISTS_LIKED)) {
                JSONArray playlistItemsJSON = new JSONArray();
                PlaylistModel playlistModel = new PlaylistModel(playlistModelsJSON.getJSONObject(i));
                playlistItemsJSON = playlistModel.getJSONPlaylist();
                for (int j = 0; j < playlistItemsJSON.length(); j++) {
                    if (playlistItemsJSON.getJSONObject(j).getString("name").equals(this.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
