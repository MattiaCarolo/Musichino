package com.unitn.musichino.Models;

import org.jetbrains.annotations.NotNull;

public class AudioModel {
    String path;
    String name;
    String album;
    String artist;
    String format;

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

}
