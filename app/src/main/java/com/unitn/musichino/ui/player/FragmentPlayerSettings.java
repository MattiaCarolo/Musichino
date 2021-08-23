package com.unitn.musichino.ui.player;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unitn.musichino.R;

import com.unitn.musichino.ui.player.Settings.VolumeFragment;
import com.unitn.musichino.uikit.SettingsTransition;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerSettings extends Fragment implements View.OnClickListener {

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
        btn_volumes = view.findViewById(R.id.btn_volumes);
        lay_container = view.findViewById(R.id.lay_btncontainer);
        ViewCompat.setTransitionName(btn_volumes, "title");
        ViewCompat.setTransitionName(lay_container, "background");
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btn_volumes:
                fragment = VolumeFragment.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
                transaction.replace(R.id.lay_settingsContainer, fragment);
                transaction.addSharedElement(btn_volumes, "title");
                transaction.addSharedElement(lay_container, "background");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
                /*






                 */
        }
    }

    public void replaceFragment(Fragment someFragment) {


        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.lay_settingsContainer, someFragment);
        transaction.addSharedElement(btn_volumes, "titolo");
        transaction.addSharedElement(lay_container, "sfondo");
        transaction.addToBackStack(null);
        transaction.commit();
    }


}