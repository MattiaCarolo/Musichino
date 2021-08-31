package com.unitn.musichino.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
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
}
