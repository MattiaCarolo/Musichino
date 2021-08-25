package com.unitn.musichino.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class AudioModel implements Parcelable {
    String path;
    String name;
    String album;
    String artist;
    String format;

    protected AudioModel(Parcel in) {
        path = in.readString();
        name = in.readString();
        album = in.readString();
        artist = in.readString();
        format = in.readString();
    }

    public AudioModel(){
        path = "path";
        name = "name";
        album = "album";
        artist = "artist";
        format = "format";
    }

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
    public String toString(){
        return("Name:" +getName()+", Artist: " +getArtist()+", Album: "+getAlbum());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(name);
        parcel.writeString(album);
        parcel.writeString(artist);
        parcel.writeString(format);
    }
}
