package com.unitn.musichino.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.ultramegasoft.radarchart.RadarView;
import com.unitn.musichino.Models.AudioModel;
import com.unitn.musichino.Models.CardModel;
import com.unitn.musichino.ui.player.PlayerActivity;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PagerCardAdapter extends PagerAdapter {
    private List<CardModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public PagerCardAdapter(List<CardModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardsong_layout, container, false);

        ImageView imageView;
        TextView title;
        RadarView radarView;

        imageView = view.findViewById(R.id.img_cardalbum);
        title = view.findViewById(R.id.txt_cardTitle);
        radarView = view.findViewById(R.id.rdr_cardradar);

        imageView.setImageResource(models.get(position).getAlbum_logo());
        title.setText(models.get(position).getTrack() + " by " + models.get(position).getArtist());
        radarView.setData(models.get(position).getRadarHolder());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                Bundle b = new Bundle();
                AudioModel item = new AudioModel();
                item.setPath(models.get(position).getFileName());
               // intent.putExtra("fileName", models.get(position).getFileName());
                b.putParcelable("item", item);
                intent.putExtra("bundle", b);
                context.startActivity(intent);

            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
