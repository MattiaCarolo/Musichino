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
import android.widget.Button;

import com.unitn.musichino.R;
import com.unitn.musichino.uikit.SettingsTransition;

    public class VolumeFragment extends Fragment {

    private Button btn_title;
    private ConstraintLayout constraintLayout;

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
        setSharedElementEnterTransition(new SettingsTransition());
        setSharedElementReturnTransition(new SettingsTransition());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_volume, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        btn_title = view.findViewById(R.id.btn_volumes_title);
        constraintLayout = view.findViewById(R.id.lay_frag_volume);
        ViewCompat.setTransitionName(btn_title, "title");
        ViewCompat.setTransitionName(constraintLayout, "background");

        postponeEnterTransition();
        startPostponedEnterTransition();
    }
}