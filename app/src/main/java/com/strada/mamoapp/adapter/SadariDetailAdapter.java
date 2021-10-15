package com.strada.mamoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.strada.mamoapp.R;
import com.strada.mamoapp.api.reponse.SadariDetailResponse;

import java.util.ArrayList;

public class SadariDetailAdapter extends RecyclerView.Adapter<SadariDetailAdapter.ViewHolder> {
    private ArrayList<SadariDetailResponse.SadariDetail.DataSadariDetail> list;

    public SadariDetailAdapter(ArrayList<SadariDetailResponse.SadariDetail.DataSadariDetail> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new SadariDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sadari_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewContentQuestion.setText(list.get(position).contentQuestion);
        if (list.get(position).answer.equalsIgnoreCase("1")) {
            holder.textViewAnswer.setText("Ya");
            holder.textViewAnswer.setBackgroundResource(R.drawable.label_green);
        } else {
            holder.textViewAnswer.setText("Tidak");
            holder.textViewAnswer.setBackgroundResource(R.drawable.label_red);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContentQuestion, textViewAnswer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContentQuestion = itemView.findViewById(R.id.textViewContentQuestion);
            textViewAnswer = itemView.findViewById(R.id.textViewAnswer);
        }
    }
}
