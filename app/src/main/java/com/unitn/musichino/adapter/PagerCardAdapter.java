package com.unitn.musichino.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import com.unitn.musichino.PlayerActivity;
import com.unitn.musichino.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

        imageView = view.findViewById(R.id.img_cardalbum);
        title = view.findViewById(R.id.txt_cardTitle);

        imageView.setImageBitmap(models.get(position).getAlbum_logo());
        title.setText(models.get(position).getTrack() + " by " + models.get(position).getArtist());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                Bundle b = new Bundle();
                AudioModel item = new AudioModel();
                item.setName(models.get(position).getTrack());
                item.setArtist(models.get(position).getArtist());
                item.setPath(models.get(position).getFileName());
                List<AudioModel> items = new ArrayList<>();
                items.add(item);
                b.putParcelableArrayList("items", (ArrayList<? extends Parcelable>) items);
                b.putString("trackname",models.get(position).getTrack());
                b.putString("artist",models.get(position).getArtist());
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
