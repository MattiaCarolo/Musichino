package com.unitn.musichino.ui.player.Settings;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unitn.musichino.R;
import com.unitn.musichino.ui.player.Settings.FragmentPlayerSettings;
import com.unitn.musichino.ui.player.Settings.VolumeFragment;

public class SettingsHUDFragment extends Fragment implements View.OnClickListener {

    Button btn_volumes;
    

    public SettingsHUDFragment() {
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
        View root = inflater.inflate(R.layout.fragment_player_hud, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        btn_volumes = view.findViewById(R.id.btn_volumes);
        btn_volumes.setOnClickListener(this);
        ViewCompat.setTransitionName(btn_volumes, "title");
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btn_volumes:
                fragment = new VolumeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
                transaction.replace(R.id.hud_settings, fragment);
                transaction.addSharedElement(btn_volumes, "titolo");
                transaction.addToBackStack("volumi");
                transaction.commit();
                break;

        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.hud_settings, someFragment);
        transaction.addSharedElement(btn_volumes, "titolo");
        transaction.addToBackStack(null);
        transaction.commit();
    }


}