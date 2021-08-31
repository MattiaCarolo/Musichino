package com.unitn.musichino.Models;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.List;

public class CardModel {
    private String Artist,Track,FileName;
    private Bitmap Album_logo;

    public CardModel(String track, String artist, String file_name, Bitmap album_logo) {
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

    public Bitmap getAlbum_logo() {
        return Album_logo;
    }

    public void setAlbum_logo(Bitmap album_logo) {
        Album_logo = album_logo;
    }
}
