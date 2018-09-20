package com.demo.photoalbum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.photoalbum.R;
import com.demo.photoalbum.model.Photos_Vm;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Photos_Vm> listOfPhotos;

    public PhotosAdapter(Context context, ArrayList<Photos_Vm> listOfPhotos) {
        this.context = context;
        this.listOfPhotos = listOfPhotos;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_photo_view,parent,false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Picasso.with(context).load(listOfPhotos.get(position).getUrl()).placeholder(R.drawable.placeholder).into(((Viewholder) holder).img_photo);
    }

    @Override
    public int getItemCount() {
        return listOfPhotos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView img_photo;
        public Viewholder(View itemView) {
            super(itemView);
            img_photo=(ImageView)itemView.findViewById(R.id.img_photo);
        }
    }
}
