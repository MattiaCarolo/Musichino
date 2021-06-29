package com.unitn.musichino.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;
import com.unitn.musichino.R;

import java.util.ArrayList;

public class RecyclerCardAdapter extends RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder> {

    private String[] artist = {"Royal Blood",
            "GINO",
            "Bamba",
    };

    private String[] track = {"Typhoons",
            "Tua", "Strabombs",
            };

    private int[] images = { R.drawable.albumcover,
            R.drawable.albumcover,
            R.drawable.albumcover,
    };
    private ArrayList<RadarHolder> mData = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Guitar", 3));
            add(new RadarHolder("Bass", 4));
            add(new RadarHolder("Drums", 4));
            add(new RadarHolder("Voice", 4));
            add(new RadarHolder("SFX", 2));
        }
    };
    private ArrayList<RadarHolder> mData2 = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Guitar", 3));
            add(new RadarHolder("Bass", 4));
            add(new RadarHolder("Drums", 4));
            add(new RadarHolder("Voice", 4));
            add(new RadarHolder("SFX", 2));
        }
    };
    private ArrayList<RadarHolder> mData3 = new ArrayList<RadarHolder>() {
        {
            add(new RadarHolder("Guitar", 3));
            add(new RadarHolder("Bass", 4));
            add(new RadarHolder("Drums", 4));
            add(new RadarHolder("Voice", 4));
            add(new RadarHolder("SFX", 2));
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemTitle;
        public RadarView radarView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.img_cardalbum);
            itemTitle = (TextView)itemView.findViewById(R.id.txt_cardTitle);
            radarView = (RadarView)itemView.findViewById(R.id.rdr_cardradar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardsong_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(track[i] + " by " + artist[i]);
        viewHolder.itemImage.setImageResource(images[i]);
        switch (i){
            case 0:
                viewHolder.radarView.setData(mData);
                break;
            case 1:
                viewHolder.radarView.setData(mData);
                break;
            default:
                viewHolder.radarView.setData(mData3);
                break;
        }
    }
    @Override
    public int getItemCount() {
        return artist.length;
    }

}
