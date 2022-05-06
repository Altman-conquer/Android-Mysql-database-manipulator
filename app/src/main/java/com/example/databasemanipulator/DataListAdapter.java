package com.example.databasemanipulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataViewHolder> {

    private ArrayList<ArrayList<String>> data;
    private LayoutInflater Inflater;

    public DataListAdapter(Context context,
                           ArrayList<ArrayList<String>> data) {
        Inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView=Inflater.inflate(R.layout.data_item,parent,false);
        return new DataViewHolder(ItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        int i=position/data.get(0).size();
        int j=position%data.get(0).size();
        holder.displayTextView.setText(data.get(i).get(j));
    }

    @Override
    public int getItemCount() {
        if (data.size()==0)
            return 0;
        else
            return data.size()*data.get(0).size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        public final TextView displayTextView;
        final DataListAdapter adapter;

        public DataViewHolder(@NonNull View itemView, DataListAdapter adapter) {
            super(itemView);
            displayTextView = itemView.findViewById(R.id.dataTextView);
            this.adapter = adapter;
        }

    }
}
