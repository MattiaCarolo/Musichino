package com.unitn.musichino.ui.equalizer;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.audiofx.Equalizer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.unitn.musichino.R;
import com.unitn.musichino.databinding.FragmentSearchBinding;
import com.unitn.musichino.ui.search.SearchViewModel;

public class EqualizerFragment extends Fragment {

    private EqualizerViewModel equalizerViewModel;
    private FragmentSearchBinding binding;
    private Equalizer mEqualizer;
    private LinearLayout mLinearLayout;

    public static EqualizerFragment newInstance() {
        return new EqualizerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        equalizerViewModel =
                new ViewModelProvider(this).get(EqualizerViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mEqualizer = new Equalizer(0, getActivity().getTaskId());
        Log.i("taskID:", requireActivity().getTaskId()+"");
        mEqualizer.setEnabled(true);// setup FX
        mLinearLayout = (LinearLayout) root.findViewById(R.id.equalizerLayout);
        mLinearLayout.setPadding(0, 0, 0, 20);

//        get number frequency bands supported by the equalizer engine
        short numberFrequencyBands = mEqualizer.getNumberOfBands();

//        get the level ranges to be used in setting the band level
//        get lower limit of the range in milliBels
        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
//        get the upper limit of the range in millibels
        final short upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];


        // UI
        SharedPreferences properties = requireContext().getSharedPreferences("equalizer", 0);
        for (short i = 0; i < numberFrequencyBands; i++) {
            final short equalizerBandIndex = i;

//            frequency header for each seekBar
            TextView frequencyHeaderTextview = new TextView(requireContext());
            frequencyHeaderTextview.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            frequencyHeaderTextview.setGravity(Gravity.CENTER_HORIZONTAL);
            frequencyHeaderTextview
                    .setText((mEqualizer.getCenterFreq(equalizerBandIndex) / 1000) + " Hz");
            mLinearLayout.addView(frequencyHeaderTextview);

//            set up linear layout to contain each seekBar
            LinearLayout seekBarRowLayout = new LinearLayout(requireContext());
            seekBarRowLayout.setOrientation(LinearLayout.HORIZONTAL);

//            set up lower level textview for this seekBar
            TextView lowerEqualizerBandLevelTextview = new TextView(requireContext());
            lowerEqualizerBandLevelTextview.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            lowerEqualizerBandLevelTextview.setText((lowerEqualizerBandLevel / 100) + " dB");
            lowerEqualizerBandLevelTextview.setRotation(90);
//            set up upper level textview for this seekBar
            TextView upperEqualizerBandLevelTextview = new TextView(requireContext());
            upperEqualizerBandLevelTextview.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            upperEqualizerBandLevelTextview.setText((upperEqualizerBandLevel / 100) + " dB");
            upperEqualizerBandLevelTextview.setRotation(90);


            //            **********  the seekBar  **************
//            set the layout parameters for the seekbar
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT - 60,
                    120);
            layoutParams.weight = 1;

//            create a new seekBar
            SeekBar seekBar = new SeekBar(requireContext());
//            give the seekBar an ID
            seekBar.setId(i);
            ColorDrawable seekbg;
            seekbg = new ColorDrawable(Color.parseColor("#CECECE"));
            seekbg.setAlpha(90);
//            seekBar.setBackground(new ColorDrawable(Color.rgb(201, 224, 203)));
            seekBar.setBackground(seekbg);
            seekBar.setPadding(35, 15, 35, 15);

            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
//            set the progress for this seekBar
            final int seek_id = i;
            int progressBar = properties.getInt("seek_" + seek_id, 1500);
//            Log.i("storedOld_seek_"+seek_id,":"+ progressBar);
            if (progressBar != 1500) {
                seekBar.setProgress(progressBar);
                mEqualizer.setBandLevel(equalizerBandIndex,
                        (short) (progressBar + lowerEqualizerBandLevel));
            } else {
                seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex));
                mEqualizer.setBandLevel(equalizerBandIndex,
                        (short) (progressBar + lowerEqualizerBandLevel));
            }
//            change progress as its changed by moving the sliders
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    mEqualizer.setBandLevel(equalizerBandIndex,
                            (short) (progress + lowerEqualizerBandLevel));

                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    //not used
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //not used
                    properties.edit().putInt("seek_" + seek_id, seekBar.getProgress()).apply();
                    properties.edit().putInt("position", 0).apply();
                }
            });
            seekBar.setThumb(ContextCompat.getDrawable(requireContext(), R.drawable.exo_controls_play));
            seekBar.setProgressDrawable(new ColorDrawable(Color.rgb(56, 60, 62)));
// seekbar row layout settings. The layout is rotated at 270 so left=>bottom, Right=>top and so on
            LinearLayout.LayoutParams seekBarLayout = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            seekBarLayout.weight = 1;
            seekBarLayout.setMargins(5, 0, 5, 0);
            seekBarRowLayout.setLayoutParams(seekBarLayout);

//            add the lower and upper band level textviews and the seekBar to the row layout
            seekBarRowLayout.addView(lowerEqualizerBandLevelTextview);
            seekBarRowLayout.addView(seekBar);
            seekBarRowLayout.addView(upperEqualizerBandLevelTextview);

            mLinearLayout.addView(seekBarRowLayout);

            //        show the spinner ??????????
            //equalizeSound();
        }
        mLinearLayout.setRotation(270);

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}