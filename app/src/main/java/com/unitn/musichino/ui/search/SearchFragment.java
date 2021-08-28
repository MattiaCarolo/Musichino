package com.unitn.musichino.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.databinding.FragmentSearchBinding;
import com.unitn.musichino.util.C;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    List<AudioModel> tracks;
    RecyclerView searchRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        tracks = new ArrayList<>();
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchRecycler = binding.searchRecycler;
        SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(getActivity(), tracks);
        searchRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRecycler.setAdapter(adapter);
        final Button searchButton = binding.searchButton;
       // getContext().getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, 0).edit().remove("playlists").remove("playlist_names").commit();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    tracks = getAllAudioFromDevice(requireContext());
                    SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(getActivity(), tracks);
                    searchRecycler.swapAdapter(adapter, true);
                    Log.d("SEARCH", searchRecycler.getAdapter().getItemCount()+" items created.");
                }


            }
        });
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                searchButton.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public List<AudioModel> getAllAudioFromDevice(final Context context) {

        final List<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
       // requireActivity().grantUriPermission("com.unitn.musichino", uri, );
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.AudioColumns.TITLE};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {

                AudioModel audioModel = new AudioModel();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);
                String title = c.getString(3);

                String name = path.substring(path.lastIndexOf("/") + 1);
                String format = name.substring(name.lastIndexOf(".") + 1);
                if(title != null)
                    audioModel.setName(title);
                else
                    audioModel.setName(name);
                if(album != null)
                    audioModel.setAlbum(album);
                if(artist != null)
                    audioModel.setArtist(artist);
                audioModel.setPath(path);
                audioModel.setFormat(format);

                Log.d("Name :" + name, " Album :" + album);
                Log.d("Path :" + path, " Artist :" + artist);

                tempAudioList.add(audioModel);
            }
            c.close();
        }
        Log.d("LIST", "total count: " + tempAudioList.size());
        return tempAudioList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tracks = getAllAudioFromDevice(requireContext());
                searchRecycler.getAdapter().notifyDataSetChanged();
            } else {
                // Permission Denied
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}