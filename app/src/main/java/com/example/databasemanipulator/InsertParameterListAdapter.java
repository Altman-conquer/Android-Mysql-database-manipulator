package com.example.databasemanipulator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InsertParameterListAdapter extends RecyclerView.Adapter<InsertParameterListAdapter.InsertParameterViewHolder> {

    private ArrayList<String> parameterNameArrayList;
    private LayoutInflater Inflater;

    public InsertParameterListAdapter(Context context, ArrayList<String> parameterNameArrayList) {
        Inflater = LayoutInflater.from(context);
        this.parameterNameArrayList = parameterNameArrayList;
    }

    public void removeData(int location){
        notifyItemRemoved(location);
        parameterNameArrayList.remove(location);
    }
    public void addData(){
        parameterNameArrayList.add("");
        notifyItemInserted(parameterNameArrayList.size()-1);
    }

    @NonNull
    @Override
    public InsertParameterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=Inflater.inflate(R.layout.insert_parameter_item,parent,false);
        return new InsertParameterViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull InsertParameterViewHolder holder, int position) {
        holder.parameterNameEditText.setText(parameterNameArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return parameterNameArrayList.size();
    }


    class InsertParameterViewHolder extends RecyclerView.ViewHolder {

        public final EditText parameterNameEditText;
        public final Button button;
        final InsertParameterListAdapter adapter;

        public InsertParameterViewHolder(@NonNull View itemView, InsertParameterListAdapter adapter) {
            super(itemView);

            button=itemView.findViewById(R.id.parameterDeleteButton);
            parameterNameEditText = itemView.findViewById(R.id.parameterNameEditText);
            this.adapter = adapter;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(getAdapterPosition());
                }
            });

            parameterNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    parameterNameArrayList.set(getAdapterPosition(),s.toString());
                }
            });

        }
    }

}
