package com.unitn.musichino.interfaces;

import android.view.View;

import com.unitn.musichino.Models.ModelClass;

public interface AdapterListener {
    void itemClicked(int pos, ModelClass object, View view, String transition);
}
