package com.unitn.musichino.ui.player.Settings;

import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unitn.musichino.R;
import com.unitn.musichino.uikit.SettingsTransition;

    public class VolumeFragment extends Fragment {
        /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            ImageView cover;
            TextView title;

            View view = inflater.inflate(R.layout., container, false);

            cover = view.findViewById(R.id.cover);
            title = view.findViewById(R.id.titletitle);

            Bundle b = getArguments();
            if (b != null) {

                String imageURL = b.getString("IMAGE_URL");
                int position = b.getInt("POSITION");

                Picasso.get().load(imageURL).into(cover);
                title.setText("Title " + (position+1));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String transitionName = b.getString("TRANSITION_NAME");
                    cover.setTransitionName(transitionName);
                    Log.i("CHILD FRAGMENT-DEBUG", transitionName );
                }
            }

            return view;
        }

    }
    */
    private Button btn_title;
    private ConstraintLayout constraintLayout;

    public VolumeFragment() {
        // Required empty public constructor
    }

    public static VolumeFragment newInstance() {
        VolumeFragment fragment = new VolumeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new SettingsTransition());
        setSharedElementReturnTransition(new SettingsTransition());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_volume, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        btn_title = view.findViewById(R.id.btn_volumes_title);
        constraintLayout = view.findViewById(R.id.lay_frag_volume);
        ViewCompat.setTransitionName(btn_title, "titolo");
        ViewCompat.setTransitionName(constraintLayout, "sfondo");

        postponeEnterTransition();
        startPostponedEnterTransition();
    }
}