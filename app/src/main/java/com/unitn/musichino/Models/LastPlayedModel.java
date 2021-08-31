package com.unitn.musichino.Models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LastPlayedModel extends PlaylistModel {

    private List<AudioModel> lastplayed;

    public LastPlayedModel(Context context, String name) throws JSONException {
        super(context, name);
    }

    public LastPlayedModel(List<AudioModel> playlist, String name, String artworkUri) {
        super(playlist, name, artworkUri);
    }

    public LastPlayedModel() {
        lastplayed = new ArrayList<AudioModel>();
    }

    public LastPlayedModel(JSONObject playlistModelJSON) throws JSONException {
        super(playlistModelJSON);
    }
}
