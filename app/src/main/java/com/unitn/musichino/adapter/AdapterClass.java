package com.unitn.musichino.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.unitn.musichino.Models.ModelClass;
import com.unitn.musichino.R;
import com.unitn.musichino.interfaces.AdapterListener;

import java.util.ArrayList;
import java.util.Random;


public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {

    private ArrayList<String> images;
    private AdapterListener mCallback;

    public AdapterClass(AdapterListener listener, ArrayList<String> images) {
        this.mCallback = listener;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.text.setText(String.valueOf(i+1));
        Picasso.get().load(images.get(i)).into(viewHolder.image);

        final String transition;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transition = "transition" + new Random().nextInt(9999999);
            viewHolder.image.setTransitionName(transition);
        } else {
            transition = null;
        }

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ADAPTER-DEBUG", transition + "/" + viewHolder.image.getTransitionName());
                mCallback.itemClicked(i, new ModelClass(), viewHolder.image, transition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
        }
    }
}
