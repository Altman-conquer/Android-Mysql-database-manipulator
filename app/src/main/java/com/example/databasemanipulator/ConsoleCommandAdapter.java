package com.example.databasemanipulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConsoleCommandAdapter extends RecyclerView.Adapter<ConsoleCommandAdapter.ConsoleCommandHolder> {
    private ArrayList<String> data;
    private LayoutInflater inflater;

    public ConsoleCommandAdapter(Context context,
                                 ArrayList<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ConsoleCommandHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.data_item, parent, false);
        return new ConsoleCommandHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsoleCommandHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ConsoleCommandHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ConsoleCommandAdapter adapter;

        public ConsoleCommandHolder(@NonNull View itemView, ConsoleCommandAdapter adapter) {
            super(itemView);
            textView = itemView.findViewById(R.id.dataTextView);
            this.adapter = adapter;
        }
    }
}