package com.unitn.musichino.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.ultramegasoft.radarchart.RadarEditWidget;
import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;
import com.unitn.musichino.Models.CardModel;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter {

    @NonNull
    private ArrayList<RadarHolder> mData = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Body", 3));
            add(new RadarHolder("Charcoal", 4));
            add(new RadarHolder("Oak", 4));
            add(new RadarHolder("Leather", 4));
            add(new RadarHolder("Spice", 2));
            add(new RadarHolder("Alcohol", 3));
            add(new RadarHolder("Astringent", 3));
            add(new RadarHolder("Linger", 4));
        }
    };

    private List<CardModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public CardAdapter(List<CardModel> models, Context context) {
        this.models = models;
        this.context = context;
    }


    @NonNull
    @NotNull
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardsong_layout,container,false);

        ImageView imageView;
        TextView title;
        RadarView radarView;
        RadarEditWidget mEditWidget;
        Animation mEditInAnimation;
        Animation mEditOutAnimation;

        radarView = view.findViewById(R.id.radar);
        title = view.findViewById(R.id.txt_cardTitle);
        title.setText(models.get(position).getArtist() + " by " + models.get(position).getArtist());

        container.addView(view,0);

        return view;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
