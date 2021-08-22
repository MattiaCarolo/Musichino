package com.unitn.musichino.ui.player;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.unitn.musichino.R;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.unitn.musichino.ui.player.Settings.VolumeFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerSettings extends Fragment implements View.OnClickListener {

    private RadarChart chart;
    private Button btn_volumes;
    private ConstraintLayout lay_container;

    public FragmentPlayerSettings() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentPlayerSettings newInstance(String param1, String param2) {
        FragmentPlayerSettings fragment = new FragmentPlayerSettings();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player__settings, container, false);
        btn_volumes = root.findViewById(R.id.btn_volumes);
        btn_volumes.setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        lay_container = view.findViewById(R.id.lay_container_volume);
        ViewCompat.setTransitionName(lay_container, "sfondo");
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btn_volumes:
                fragment = new VolumeFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.lay_settingsContainer, someFragment);
        transaction.addSharedElement(lay_container, "sfondo");
        transaction.addToBackStack(null);
        transaction.commit();
    }


}