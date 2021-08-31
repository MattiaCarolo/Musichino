package com.unitn.musichino.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
    Classe generalizzata che incapsula i valori di VolumePreset ed EqPreset
    gestendo la logica del loro salvataggio/caricamento nelle SharedPreferences
*/
public class PresetModel {
    private String name;
    private List<Integer> values;

    public PresetModel() {
        this.name = "name";
        this.values = new ArrayList<>();
    }

    /*
        Costruttore con specifica della dimensione del array di valori
        (numero tracce audio nel file in caso dei volumi, numero delle bande supportate nel caso dell'equalizzatore)
        e del tipo di preset
        (valore standard per volumi = 100 per volume massimo, valore equalizzatore = 1500 per equalizzazione a 0 poiche' le seekbar usate vanno da 0 a 3000)
    */
    public PresetModel(String name, int size, boolean isVolume) {
        this.name = name;
        this.values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (isVolume)
                values.add(100);
            else
                values.add(1500);
        }
    }

    /*
        Costruttore tramite JSON usato per mantenere una consistenza dei dati
        nel salvataggio e caricamento delle informazioni nelle SharedPreferences
    */
    public PresetModel(JSONObject presetModelJSON) throws JSONException {
        this.values = new ArrayList<>();
        this.name = presetModelJSON.getString("name");
        JSONArray valuesJSON = presetModelJSON.getJSONArray("values");
        for (int i = 0; i < valuesJSON.length(); i++) {
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

    /*
        Metodo per convertire PresetModel in oggetto JSON
    */
    public JSONObject getJSONPresetModel() throws JSONException {
        JSONObject presetModelJSON = new JSONObject();
        presetModelJSON.put("name", this.name);
        JSONArray valuesJSON = new JSONArray();
        for (Integer value : values) {
            valuesJSON.put(value);
        }
        presetModelJSON.put("values", valuesJSON);
        return presetModelJSON;
    }

}
