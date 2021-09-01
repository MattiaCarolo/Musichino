package com.unitn.musichino.service;

import android.app.Application;

import com.unitn.musichino.databinding.AppBarHomeBinding;

public class MixMe extends Application {

    private boolean _running;
    private AudioService audioService;

    public boolean is_running() {
        return _running;
    }

    public void set_running(boolean someVariable) {
        this._running = someVariable;
    }

    public AudioService getService() {return audioService;}

    public void runningService(AudioService audioService) { this.audioService = audioService; }
}
