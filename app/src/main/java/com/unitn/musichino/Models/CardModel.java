package com.unitn.musichino.Models;

import android.widget.ImageView;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.List;

public class CardModel {
    private String Artist,Track,FileName;
    private int Album_logo;

    public CardModel(String artist, String track, String file_name, int album_logo) {
        Artist = artist;
        Track = track;
        FileName = file_name;
        Album_logo = album_logo;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getTrack() {
        return Track;
    }

    public void setTrack(String track) {
        Track = track;
    }

    public int getAlbum_logo() {
        return Album_logo;
    }

    public void setAlbum_logo(int album_logo) {
        Album_logo = album_logo;
    }
}
