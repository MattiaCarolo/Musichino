package com.unitn.musichino.ui.home;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.google.android.exoplayer2.MediaItem;
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
    private String CHANGE = "asset:///output2.mp4";
    private String CHANGE1 = "asset:///Example_Electro.m4a";
    private String CHANGE2 = "asset:///Example_Logan.m4a";
    private String CHANGE3 = "asset:///Example_Rock.m4a";
    private String CHANGE4 = "asset:///relaxingMusic.mp4";

    private List<CardModel> models;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        Log.d("size",toastMsg);


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        //Viewpager Default
        models = new ArrayList<>();

        MediaItem mediaItem = MediaItem.fromUri(CHANGE1);

        models.add(new CardModel("" + mediaItem.mediaMetadata.albumArtist, "" + mediaItem.mediaMetadata.title, CHANGE, Bitmap.createBitmap()mediaItem.mediaMetadata.artworkData));
        models.add(new CardModel("Enrico Papi", "La mamma", CHANGE, R.drawable.royalblood_cover));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE, R.drawable.albumcover));
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_likedlist);
        viewPager.setAdapter(pagerCardAdapter);


        //Viewpager
        models = new ArrayList<>();
        models.add(new CardModel("Royal Blood", "Typhoon", CHANGE, R.drawable.royalblood_cover));
        models.add(new CardModel("Enrico Papi", "La mamma", CHANGE, R.drawable.royalblood_cover));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE, R.drawable.royalblood_cover));
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_recentlyplayed);
        viewPager.setAdapter(pagerCardAdapter);

        //Viewpager
        models = new ArrayList<>();
        models.add(new CardModel("Royal Blood", "Typhoon", CHANGE, R.drawable.royalblood_cover));
        models.add(new CardModel("Enrico Papi", "La mamma", CHANGE, R.drawable.royalblood_cover));
        models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE, R.drawable.royalblood_cover));
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