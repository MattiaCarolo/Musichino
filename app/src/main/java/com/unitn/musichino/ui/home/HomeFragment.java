package com.unitn.musichino.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.ultramegasoft.radarchart.RadarHolder;
import com.unitn.musichino.Home;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.CardModel;
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PagerCardAdapter;
import com.unitn.musichino.databinding.FragmentHomeBinding;
import com.unitn.musichino.service.AudioService;

import java.io.File;
import java.io.IOException;
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
    private MediaItem mediaItem;
    private MediaMetadata metadata;
    private FloatingActionMenu player_menu;
    private FloatingActionButton fab1, fab2, fab3;
    private SimpleExoPlayer simpleExoPlayer;
    private AudioService mService;

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

        models = new ArrayList<>();
        models.add(getMetadata(CHANGE1));
        models.add(getMetadata(CHANGE2));
        models.add(getMetadata(CHANGE3));
        models.add(getMetadata(CHANGE4));
        models.add(getMetadata(CHANGE));
        Log.d("models", "" + models.toString());
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_likedlist);
        viewPager.setAdapter(pagerCardAdapter);



        mediaItem = MediaItem.fromUri(CHANGE);
        metadata = mediaItem.mediaMetadata;
        if(mediaItem != null){
            Log.d("dio",mediaItem.mediaMetadata.albumArtist + "");
        }
        metadata = mediaItem.mediaMetadata;
        if(metadata.artworkData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(metadata.artworkData, 0, metadata.artworkData.length);
            models = new ArrayList<>();
            models.add(new CardModel("Royal Blood", "Typhoon", CHANGE,bitmap));
            models.add(new CardModel("Enrico Papi", "La mamma", CHANGE,bitmap));
            models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE,bitmap));
            pagerCardAdapter = new PagerCardAdapter(models, root.getContext());
        }
        viewPager = root.findViewById(R.id.vp_recentlyplayed);
        viewPager.setAdapter(pagerCardAdapter);




        if(metadata.artworkData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(metadata.artworkData, 0, metadata.artworkData.length);
            models = new ArrayList<>();
            models.add(new CardModel("Royal Blood", "Typhoon", CHANGE,bitmap));
            models.add(new CardModel("Enrico Papi", "La mamma", CHANGE,bitmap));
            models.add(new CardModel("Bambini autistici", "Lodiamo gesu cristo", CHANGE,bitmap));
            pagerCardAdapter = new PagerCardAdapter(models, root.getContext());
        }
        viewPager = root.findViewById(R.id.vp_recentlyplayed);
        viewPager.setAdapter(pagerCardAdapter);


        player_menu = root.findViewById(R.id.player_menu);
        fab1 = (FloatingActionButton) root.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) root.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) root.findViewById(R.id.fab3);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private CardModel getMetadata (String path) {
        Uri uri = Uri.parse(path);
        String artist, title;
        Bitmap picture;
        File file = new File(uri.toString());
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        final AssetFileDescriptor afd;
        try {
            afd = requireContext().getAssets().openFd(uri.getLastPathSegment());
            metaRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            artist = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);

            title = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            picture = BitmapFactory.decodeByteArray(metaRetriever.getEmbeddedPicture(), 0, metaRetriever.getEmbeddedPicture().length);

        } catch (Exception e) {
            artist = "Unknown";
            title = "Unknown";
            picture = null;
        }

        return new CardModel(title,artist,artist,picture);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        if(mService != null){
            mService = ((Home) requireActivity()).mService;
        }

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:

                    break;
                case R.id.fab2:
                    fab2.setVisibility(View.GONE);
                    break;
                case R.id.fab3:
                    fab2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}