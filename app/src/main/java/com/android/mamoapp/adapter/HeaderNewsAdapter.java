package com.android.mamoapp.adapter;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class HeaderNewsAdapter extends RecyclerView.Adapter<HeaderNewsAdapter.ViewHolder> {
    private ArrayList<NewsResponse.NewsModel> list;

    public HeaderNewsAdapter(ArrayList<NewsResponse.NewsModel> list) {
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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
//                .placeholder(R.drawable.mqdefault)
                .into(holder.imageViewHeaderNews);
        holder.textViewTitleNews.setText(list.get(position).titleNews);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewHeaderNews;
        private TextView textViewTitleNews;
        private CardView cardViewHeaderNews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewHeaderNews = itemView.findViewById(R.id.imageViewHeaderNews);
            textViewTitleNews = itemView.findViewById(R.id.textViewTitleNews);
            cardViewHeaderNews = itemView.findViewById(R.id.cardViewHeaderNews);
        }
    }
}
