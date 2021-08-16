package com.unitn.musichino.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ultramegasoft.radarchart.RadarHolder;
import com.unitn.musichino.Models.CardModel;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PagerCardAdapter;
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
    private String CHANGE = "logan3.mp4";

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
        //Viewpager
        models = new ArrayList<>();
        models.add(new CardModel("Royal Blood", "Typhoon", CHANGE, R.drawable.royalblood_cover, mData));
        models.add(new CardModel("Enrico Papi", "La mamma", CHANGE, R.drawable.royalblood_cover, mData2));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE, R.drawable.albumcover, mData3));
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_likedlist);
        viewPager.setAdapter(pagerCardAdapter);


        //Viewpager
        models = new ArrayList<>();
        models.add(new CardModel("Royal Blood", "Typhoon", CHANGE, R.drawable.royalblood_cover, mData));
        models.add(new CardModel("Enrico Papi", "La mamma", CHANGE, R.drawable.royalblood_cover, mData2));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE, R.drawable.royalblood_cover, mData3));
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_recentlyplayed);
        viewPager.setAdapter(pagerCardAdapter);

        //Viewpager
        models = new ArrayList<>();
        models.add(new CardModel("Royal Blood", "Typhoon", CHANGE, R.drawable.royalblood_cover, mData));
        models.add(new CardModel("Enrico Papi", "La mamma", CHANGE, R.drawable.royalblood_cover, mData2));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE, R.drawable.royalblood_cover, mData3));
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_recentlyplayed2);
        viewPager.setAdapter(pagerCardAdapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}