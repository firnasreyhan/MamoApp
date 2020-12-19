package com.android.mamoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.reponse.VideoResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private ArrayList<VideoResponse.VideoModel> list;

    public VideoAdapter(ArrayList<VideoResponse.VideoModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(list.get(position).urlHighThumbnail)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
//                .placeholder(R.drawable.mqdefault)
                .into(holder.imageViewVideo);
        holder.textViewTitle.setText(list.get(position).title);
        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<VideoResponse.VideoModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewVideo;
        private TextView textViewTitle;
        private LinearLayout linearLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewVideo = itemView.findViewById(R.id.imageViewVideo);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            linearLayoutItem = itemView.findViewById(R.id.linearLayoutItem);
        }
    }
}
