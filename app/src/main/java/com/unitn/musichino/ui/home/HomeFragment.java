package com.unitn.musichino.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.C;
import com.ultramegasoft.radarchart.RadarHolder;
import com.unitn.musichino.Models.CardModel;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PagerCardAdapter;
import com.unitn.musichino.adapter.RecyclerCardAdapter;
import com.unitn.musichino.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private PagerCardAdapter pagerCardAdapter;
    private ViewPager viewPager;

    private ArrayList<RadarHolder> mData = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Guitar", 3));
            add(new RadarHolder("Bass", 4));
            add(new RadarHolder("Drums", 4));
            add(new RadarHolder("Voice", 4));
            add(new RadarHolder("SFX", 2));
        }
    };
    private ArrayList<RadarHolder> mData2 = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Guitar", 3));
            add(new RadarHolder("Bass", 4));
            add(new RadarHolder("Drums", 4));
            add(new RadarHolder("Voice", 4));
            add(new RadarHolder("SFX", 2));
        }
    };
    private ArrayList<RadarHolder> mData3 = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Guitar", 3));
            add(new RadarHolder("Bass", 4));
            add(new RadarHolder("Drums", 4));
            add(new RadarHolder("Voice", 4));
            add(new RadarHolder("SFX", 2));
        }
    };

    private List<CardModel> models;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        recyclerView = root.findViewById(R.id.rcv_likedlist);
        linearLayoutManager =new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerCardAdapter();
        recyclerView.setAdapter(adapter);


        //Viewpager
        models = new ArrayList<>();
        models.add(new CardModel("Royal Blood", "Typhoon", R.drawable.albumcover, mData));
        models.add(new CardModel("Enrico Papi", "La mamma", R.drawable.albumcover, mData2));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", R.drawable.albumcover, mData3));
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_recentlyplayed);
        viewPager.setAdapter(pagerCardAdapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}