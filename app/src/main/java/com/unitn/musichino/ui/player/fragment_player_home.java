package com.unitn.musichino.ui.player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.PlayerView;
import com.unitn.musichino.MixMeExoPlayer;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.databinding.FragmentHomeBinding;
import com.unitn.musichino.ui.home.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_player_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_player_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PlayerView playerView;
    private MixMeExoPlayer mixMePlayer;

    public fragment_player_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_player_home.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_player_home newInstance(String param1, String param2) {
        fragment_player_home fragment = new fragment_player_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player_home, container, false);
        root.findViewById(R.id.player_view);
        PlayerActivity playerActivity = (PlayerActivity)getActivity();
        mixMePlayer = playerActivity.getMixMePlayer();



        return root;
    }
}