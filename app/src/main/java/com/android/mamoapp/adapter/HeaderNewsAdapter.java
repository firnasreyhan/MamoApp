package com.android.mamoapp.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.reponse.NewsResponse;
import com.android.mamoapp.api.reponse.NewsTrendingResponse;
import com.android.mamoapp.api.reponse.VideoResponse;
import com.android.mamoapp.view.activity.NewsDetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class HeaderNewsAdapter extends RecyclerView.Adapter<HeaderNewsAdapter.ViewHolder> {
    private ArrayList<NewsTrendingResponse.NewsTrendingModel> list;

    public HeaderNewsAdapter(ArrayList<NewsTrendingResponse.NewsTrendingModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeaderNewsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load("https://ilham.kristomoyo.com/images/news/" + list.get(position).newsImage)
                //.load(R.drawable.img_default_video)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .placeholder(R.drawable.img_default_video)
                .into(holder.imageViewHeaderNews);
        holder.textViewTitleNews.setText(list.get(position).titleNews);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra("ID_NEWS", list.get(position).idNews);
                v.getContext().startActivity(intent);
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

    public void addAll(ArrayList<NewsTrendingResponse.NewsTrendingModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewHeaderNews;
        private TextView textViewTitleNews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewHeaderNews = itemView.findViewById(R.id.imageViewHeaderNews);
            textViewTitleNews = itemView.findViewById(R.id.textViewTitleNews);
        }
    }
}
