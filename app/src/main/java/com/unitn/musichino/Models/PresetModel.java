package com.unitn.musichino.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.unitn.musichino.util.C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PresetModel {
    private String name;
    private List<Integer> values;

    public PresetModel(){
        this.name = "name";
        this.values = new ArrayList<>();
    }

    public PresetModel(String name, int size, boolean isVolume){
        this.name = name;
        this.values = new ArrayList<>();
        for(int i = 0; i < size; i++){
            if(isVolume)
                values.add(100);
            else
                values.add(1500);
        }
    }

    public PresetModel(JSONObject presetModelJSON) throws JSONException{
        this.values = new ArrayList<>();
        this.name = presetModelJSON.getString("name");
        JSONArray valuesJSON = presetModelJSON.getJSONArray("values");
        for(int i = 0; i < valuesJSON.length(); i++){
            this.values.add(valuesJSON.getInt(i));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }



    public JSONObject getJSONPresetModel() throws JSONException {
        JSONObject presetModelJSON = new JSONObject();
        presetModelJSON.put("name", this.name);
        JSONArray valuesJSON = new JSONArray();
        for(Integer value: values) {
            valuesJSON.put(value);
        }
        presetModelJSON.put("values", valuesJSON);
        return presetModelJSON;
    }

    public void saveToSharedPreferences(Context context, SharedPreferences trackPreferences, String sharedPreferencesBase) throws JSONException {
        String sharedPresets = trackPreferences.getString(sharedPreferencesBase, "[]");
        SharedPreferences.Editor editor = trackPreferences.edit();
        JSONArray presetsJSON = new JSONArray(sharedPresets);
        boolean found = false;
        if(editor != null){
            for (int i = 0; i < presetsJSON.length(); i++) {
                if (presetsJSON.getJSONObject(i).getString("name").equals(this.name)) {
                    presetsJSON.put(i, this.getJSONPresetModel());
                    found = true;
                }
            }
        }
        if(!found) {
            presetsJSON.put(this.getJSONPresetModel());
        }
        editor.putString(sharedPreferencesBase, presetsJSON.toString()).apply();
        Log.d("SHARED", "preset:" +trackPreferences.getString(sharedPreferencesBase, "[]"));
    }

    public List<String> getPresetNamesFromSharedPreferences(Context context, String sharedPreferencesBase) throws JSONException {
        SharedPreferences sharedPresets = context.getSharedPreferences(sharedPreferencesBase, Context.MODE_PRIVATE);
        if(sharedPresets != null) {
            JSONArray namesJSON = new JSONArray(sharedPresets.getString(C.SHARED_PRESET_NAMES, "[]"));
            List<String> playlistNames = new ArrayList<>();
            Log.d("PRESET", "Total names: " +namesJSON.length());
            for(int i = 0; i < namesJSON.length(); i++){
                playlistNames.add(namesJSON.getString(i));
            }
            return playlistNames;
        }
        return null;
    }

    public List<PresetModel> getPresetModelsFromSharedPreferences(Context context, String sharedPreferencesBase) throws  JSONException{
        SharedPreferences sharedPresets = context.getSharedPreferences(sharedPreferencesBase, Context.MODE_PRIVATE);
        if(sharedPresets != null){
            JSONArray presetModelsJSON = new JSONArray(sharedPresets.getString(C.SHARED_PRESETS, "[]"));
            List<PresetModel> presetModels = new ArrayList<>();
            for(int i = 0; i < presetModelsJSON.length(); i++){
                presetModels.add(new PresetModel(presetModelsJSON.getJSONObject(i)));
            }
            return presetModels;
        }
        return null;
    }

    public PresetModel getPresetModelFromSharedPreferencesByName(Context context, String sharedPreferencesBase, String name) throws  JSONException{
        SharedPreferences sharedPresets = context.getSharedPreferences(sharedPreferencesBase, Context.MODE_PRIVATE);
        if(sharedPresets != null) {
            JSONArray namesJSON = new JSONArray(sharedPresets.getString(C.SHARED_PRESET_NAMES, "[]"));
            for (int i = 0; i < namesJSON.length(); i++) {
                if (namesJSON.getString(i).equals(name)) {
                    PresetModel model = new PresetModel(new JSONArray(sharedPresets.getString(C.SHARED_PRESETS, "[]")).getJSONObject(i));
                    Log.d("LOADSHARE", "Found name at index: " +i+", model: " + model.getName());
                    return model;
                }
            }
        }
        return null;
    }

}
