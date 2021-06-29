package com.unitn.musichino.Models;

import android.widget.ImageView;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.List;

public class CardModel {
    private String Artist,Track;
    private int Album_logo;
    private List<RadarHolder> radarHolder;

    public CardModel(String artist, String track, int album_logo, List<RadarHolder> radarHolder) {
        Artist = artist;
        Track = track;
        Album_logo = album_logo;
        this.radarHolder = radarHolder;
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

    public List<RadarHolder> getRadarHolder() {
        return radarHolder;
    }

    public void setRadarHolder(List<RadarHolder> radarHolder) {
        this.radarHolder = radarHolder;
    }
}
