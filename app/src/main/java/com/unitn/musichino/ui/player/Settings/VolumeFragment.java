package com.unitn.musichino.ui.player.Settings;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unitn.musichino.R;

public class VolumeFragment extends Fragment {

    public VolumeFragment() {
        // Required empty public constructor
    }

    public static VolumeFragment newInstance() {
        VolumeFragment fragment = new VolumeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = new AutoTransition();
        transition.setDuration(5000);
        setSharedElementEnterTransition(transition);
        setSharedElementReturnTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_volume, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        View lay_volume = view.findViewById(R.id.lay_frag_volume);
        ViewCompat.setTransitionName(lay_volume, "sfondo");
        postponeEnterTransition();
        startPostponedEnterTransition();
    }
}