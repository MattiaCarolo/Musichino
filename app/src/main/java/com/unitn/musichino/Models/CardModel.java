package com.unitn.musichino.Models;

import android.widget.ImageView;

public class CardModel {
    private String Artist,Track;

    public CardModel(String artist, String track) {
        Artist = artist;
        Track = track;
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
}
