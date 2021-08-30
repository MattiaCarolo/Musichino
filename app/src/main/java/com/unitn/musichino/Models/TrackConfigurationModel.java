package com.unitn.musichino.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.unitn.musichino.util.C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackConfigurationModel {
    private String trackReference;
    private List<PresetModel> volumePresetModels;
    private List<PresetModel> eqPresetModels;

    public TrackConfigurationModel(){
        this.trackReference = "trackReference";
        this.volumePresetModels = new ArrayList<>();
        this.eqPresetModels = new ArrayList<>();
    }

    public TrackConfigurationModel(JSONObject trackConfigurationModelJSON) throws JSONException{
        this.trackReference = trackConfigurationModelJSON.getString("trackReference");
        this.volumePresetModels = new ArrayList<>();
        this.eqPresetModels = new ArrayList<>();
        JSONArray volumePresetsJSON = trackConfigurationModelJSON.getJSONArray("volumePresets");
        JSONArray eqPresetsJSON = trackConfigurationModelJSON.getJSONArray("eqPresets");
        for(int i = 0; i < volumePresetsJSON.length(); i++){
            this.volumePresetModels.add(new PresetModel(volumePresetsJSON.getJSONObject(i)));
        }
        for (int i = 0; i < eqPresetsJSON.length(); i++){
            this.eqPresetModels.add(new PresetModel(eqPresetsJSON.getJSONObject(i)));
        }
    }

    public JSONObject getJSONTrackConfigurationModel() throws JSONException{
        JSONObject trackConfigurationModelJSON = new JSONObject();
        trackConfigurationModelJSON.put("trackReference", this.trackReference);
        JSONArray volumeModelsJSON = new JSONArray();
        JSONArray eqModelsJSON = new JSONArray();
        for(PresetModel volume: this.volumePresetModels){
            volumeModelsJSON.put(volume.getJSONPresetModel());
        }
        for(PresetModel eq: this.eqPresetModels){
            eqModelsJSON.put(eq.getJSONPresetModel());
        }
        trackConfigurationModelJSON.put("volumePresets", volumeModelsJSON);
        trackConfigurationModelJSON.put("eqPresets", eqModelsJSON);
        return trackConfigurationModelJSON;
    }

    public String getTrackReference() {
        return trackReference;
    }

    public void setTrackReference(String trackReference) {
        this.trackReference = trackReference;
    }

    public List<PresetModel> getVolumePresetModels() {
        return volumePresetModels;
    }

    public void setVolumePresetModels(List<PresetModel> volumePresetModels) {
        this.volumePresetModels = volumePresetModels;
    }

    public void addVolumePresetModel(PresetModel volumePreset){
        this.volumePresetModels.add(volumePreset);
    }

    public void addEqPresetModel(PresetModel eqPreset){
        this.eqPresetModels.add((eqPreset));
    }

    public List<PresetModel> getEqPresetModels() {
        return eqPresetModels;
    }

    public void setEqPresetModels(List<PresetModel> eqPresetModels) {
        this.eqPresetModels = eqPresetModels;
    }

    public JSONArray getVolumePresetsJSON() throws JSONException {
        JSONArray volumePresetsJSON = new JSONArray();
        for(PresetModel volume : this.volumePresetModels){
            volumePresetsJSON.put(volume.getJSONPresetModel());
        }
        return volumePresetsJSON;
    }

    public JSONArray getEqPresetsJSON() throws JSONException {
        JSONArray eqPresetsJSON = new JSONArray();
        for(PresetModel eq : this.eqPresetModels){
            eqPresetsJSON.put(eq.getJSONPresetModel());
        }
        return eqPresetsJSON;
    }

    public void saveToSharedPreferences(Context context) throws JSONException{
        SharedPreferences sharedConfiguration = context.getSharedPreferences(this.trackReference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedConfiguration.edit();
        editor.putString(C.SHARED_PREFERENCES_PRESET_VOLUME, this.getVolumePresetsJSON().toString()).apply();
        editor.putString(C.SHARED_PREFERENCES_PRESET_EQ, this.getEqPresetsJSON().toString()).apply();
   }

   public TrackConfigurationModel getTrackConfigurationFromSharedPreferences(Context context, String trackName) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(trackName, Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            JSONObject trackConfigurationJSON = new JSONObject();
            JSONArray volumePresetsJSON = new JSONArray(sharedPreferences.getString(C.SHARED_PREFERENCES_PRESET_VOLUME, "[]"));
            JSONArray eqPresetsJSON = new JSONArray(sharedPreferences.getString(C.SHARED_PREFERENCES_PRESET_EQ, "[]"));
            trackConfigurationJSON.put("trackReference", trackName);
            trackConfigurationJSON.put("volumePresets", volumePresetsJSON);
            trackConfigurationJSON.put("eqPresets", eqPresetsJSON);
            return new TrackConfigurationModel(trackConfigurationJSON);
        }
        return null;
   }

}
