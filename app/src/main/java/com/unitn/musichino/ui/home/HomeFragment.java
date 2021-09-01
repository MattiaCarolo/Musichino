package com.unitn.musichino.ui.home;

import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.unitn.musichino.BottomPlayerFragment;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.CardModel;
import com.unitn.musichino.Models.PlaylistModel;
import com.unitn.musichino.R;
import com.unitn.musichino.adapter.PagerCardAdapter;
import com.unitn.musichino.databinding.FragmentHomeBinding;
import com.unitn.musichino.service.AudioService;
import com.unitn.musichino.util.C;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private PagerCardAdapter pagerCardAdapter;
    private ViewPager viewPager;
    private String CHANGE1 = "asset:///Example_Electro.m4a";
    private String CHANGE2 = "asset:///Example_Logan.m4a";
    private String CHANGE3 = "asset:///Example_Rock.m4a";
    private String CHANGE4 = "asset:///relaxingMusic.mp3";
    private MediaItem mediaItem;
    private MediaMetadata metadata;
    private SimpleExoPlayer simpleExoPlayer;
    private AudioService mService;

    private List<CardModel> models;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch (screenSize) {
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
        Log.d("size", toastMsg);


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        models = new ArrayList<>();
        models.add(getMetadata(CHANGE1));
        models.add(getMetadata(CHANGE2));
        models.add(getMetadata(CHANGE3));
        models.add(getMetadata(CHANGE4));
        Log.d("models", "" + models.toString());
        pagerCardAdapter = new PagerCardAdapter(models, root.getContext());

        viewPager = root.findViewById(R.id.vp_recommended);
        viewPager.setAdapter(pagerCardAdapter);


        try {
            PlaylistModel playlistModel = new PlaylistModel(requireContext(), C.SHARED_PLAYLISTS_LIKED);
            List<CardModel> models = new ArrayList<>();
            for (AudioModel audioModel : playlistModel.getPlaylist()) {
                models.add(getMetadata(audioModel.getPath()));
            }
            pagerCardAdapter = new PagerCardAdapter(models, root.getContext());
            viewPager = root.findViewById(R.id.vp_liked);
            viewPager.setAdapter(pagerCardAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Fragment fragment = new BottomPlayerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.play_mini, fragment);
        transaction.commit();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private CardModel getMetadata(String path) {
        Uri uri = Uri.parse(path);
        String artist, title;
        Bitmap picture;
        File file = new File(uri.toString());
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        final AssetFileDescriptor afd;
        try {
            afd = requireContext().getAssets().openFd(uri.getLastPathSegment());
            metaRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
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

        return new CardModel(title, artist, path, picture);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            PlaylistModel playlistModel = new PlaylistModel(requireContext(), C.SHARED_PLAYLISTS_LIKED);
            List<CardModel> models = new ArrayList<>();
            for (AudioModel audioModel : playlistModel.getPlaylist()) {
                models.add(getMetadata(audioModel.getPath()));
            }
            pagerCardAdapter = new PagerCardAdapter(models, requireContext());
            viewPager = requireActivity().findViewById(R.id.vp_liked);
            viewPager.setAdapter(pagerCardAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}