package com.unitn.musichino.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.adapter.SearchTrackAdapter;
import com.unitn.musichino.databinding.FragmentSearchBinding;
import com.unitn.musichino.util.C;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
    Fragment di ricerca dei file audio locali con filtro.
*/

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentSearchBinding binding;
    public static List<AudioModel> tracks = new ArrayList<>();
    RecyclerView searchRecycler;
    SearchTrackAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tracks = new ArrayList<>();
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchRecycler = binding.searchRecycler;
        SearchView searchView = binding.searchView;
        searchView.setOnQueryTextListener(this);
        adapter = new SearchTrackAdapter(getActivity(), tracks, null);
        searchRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRecycler.setAdapter(adapter);
        final Button searchButton = binding.btnSearch;
        //getContext().getSharedPreferences(C.SHARED_PREFERENCES_PLAYLIST, 0).edit().remove("playlists").remove("playlist_names").commit();
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
                    ArrayList<AudioModel> backup = new ArrayList<>(tracks);
                    adapter = new SearchTrackAdapter(getActivity(), tracks, backup);
                    searchRecycler.swapAdapter(adapter, true);
                    //Log.d("SEARCH", Objects.requireNonNull(searchRecycler.getAdapter()).getItemCount() + " items created.");
                }


            }
        });
        searchViewModel.getText().observe(getViewLifecycleOwner(), searchButton::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*
        Metodo per recuperare una lista di file audio presenti nella memoria interna ed esterna del telofono.
        Restituisce una lista di AudioModel creati da dati recuperati con context.getContentResolver tramite
        una query di ricerca puntata a proiettare i metadati dei file audio.
    */
    public List<AudioModel> getAllAudioFromDevice(final Context context) {
        final List<AudioModel> tempAudioList = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
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
                if (title != null)
                    audioModel.setName(title);
                else
                    audioModel.setName(name);
                if (album != null)
                    audioModel.setAlbum(album);
                if (artist != null)
                    audioModel.setArtist(artist);
                audioModel.setPath(path);
                audioModel.setFormat(format);

                //Log.d("Name :" + name, " Album :" + album);
                //Log.d("Path :" + path, " Artist :" + artist);

                tempAudioList.add(audioModel);
            }
            c.close();
        }
        //Log.d("LIST", "total count: " + tempAudioList.size());
        return tempAudioList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tracks = getAllAudioFromDevice(requireContext());
                Objects.requireNonNull(searchRecycler.getAdapter()).notifyDataSetChanged();
            } else {
                // Permission Denied
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}
