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
import com.android.mamoapp.view.activity.SadariDetailUserActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<SadariListResponse.SadariListModel> list;

    public HistoryAdapter(ArrayList<SadariListResponse.SadariListModel> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new HistoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_sadari, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isIndicated == 1) {
            holder.textViewSadariStatus.setText("Terindikasi Kangker");
            holder.textViewSadariIsChecked.setVisibility(View.VISIBLE);
            if (list.get(position).isChecked == 1) {
                holder.textViewSadariIsChecked.setText("Sudah Diperiksa");
                holder.textViewSadariIsChecked.setBackgroundResource(R.drawable.label_green);
            } else {
                holder.textViewSadariIsChecked.setText("Belum Diperiksa");
                holder.textViewSadariIsChecked.setBackgroundResource(R.drawable.label_red);
            }
        } else {
            holder.textViewSadariStatus.setText("Tidak Terindikasi Kangker");
        }
        String nmyFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat nsdf = new SimpleDateFormat(nmyFormat, new Locale("id", "ID"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = format.parse(list.get(position).dateSadari);
            holder.textViewSadariSurveyDate.setText(nsdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SadariDetailUserActivity.class);
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
        private TextView textViewSadariStatus, textViewSadariSurveyDate, textViewSadariIsChecked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSadariStatus = itemView.findViewById(R.id.textViewSadariStatus);
            textViewSadariSurveyDate = itemView.findViewById(R.id.textViewSadariSurveyDate);
            textViewSadariIsChecked = itemView.findViewById(R.id.textViewSadariIsChecked);
        }
    }
}
