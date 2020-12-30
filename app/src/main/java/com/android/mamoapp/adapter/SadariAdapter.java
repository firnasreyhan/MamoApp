package com.android.mamoapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mamoapp.R;
import com.android.mamoapp.api.reponse.SadariListResponse;
import com.android.mamoapp.view.activity.SadariDetailActivity;
import com.android.mamoapp.view.activity.VideoDetailActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SadariAdapter extends RecyclerView.Adapter<SadariAdapter.ViewHolder> {
    private ArrayList<SadariListResponse.SadariListModel> list;

    public SadariAdapter(ArrayList<SadariListResponse.SadariListModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new SadariAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sadari, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewNameSadari.setText(list.get(position).name);
        String nmyFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat nsdf = new SimpleDateFormat(nmyFormat, new Locale("id", "ID"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = format.parse(list.get(position).dateSadari);
            holder.textViewDateSadari.setText(nsdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.get(position).isChecked.equalsIgnoreCase("t")) {
            holder.textViewStatusSadari.setText("Sudah Diperiksa");
            holder.textViewStatusSadari.setBackgroundResource(R.drawable.label_green);
        } else {
            holder.textViewStatusSadari.setText("Belum Diperiksa");
            holder.textViewStatusSadari.setBackgroundResource(R.drawable.label_red);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SadariDetailActivity.class);
                intent.putExtra("ID_SADARI", list.get(position).idSadari);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameSadari, textViewDateSadari, textViewStatusSadari;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameSadari = itemView.findViewById(R.id.textViewNameSadari);
            textViewDateSadari = itemView.findViewById(R.id.textViewDateSadari);
            textViewStatusSadari = itemView.findViewById(R.id.textViewStatusSadari);
        }
    }
}
