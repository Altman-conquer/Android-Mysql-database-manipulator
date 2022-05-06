package com.example.databasemanipulator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InsertValueListAdapter extends RecyclerView.Adapter<InsertValueListAdapter.ValueViewHolder> {

    private ArrayList<String> parameter;
    private ArrayList<ArrayList<String>> values;
    private LayoutInflater Inflater;

    public InsertValueListAdapter(Context context,
                           ArrayList<ArrayList<String>> values,ArrayList<String> parameter) {
        Inflater = LayoutInflater.from(context);
        this.parameter=parameter;
        this.values=values;
    }

    @NonNull
    @Override
    public InsertValueListAdapter.ValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView=Inflater.inflate(R.layout.insert_value_item,parent,false);
        return new InsertValueListAdapter.ValueViewHolder(ItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull InsertValueListAdapter.ValueViewHolder holder, int position) {
        int i=position/values.get(0).size();
        int j=position%(values.get(0).size()+1);
        if (j== parameter.size()){
            holder.parameterNameEditText.setVisibility(View.GONE);
            holder.parameterNameTextView.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.parameterNameEditText.setVisibility(View.VISIBLE);
            holder.parameterNameTextView.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.GONE);
            holder.parameterNameTextView.setText(parameter.get(j));
        }
    }

    @Override
    public int getItemCount() {
        if (values.size()==0)
            return 0;
        else
            return values.size()*(values.get(0).size()+1);
    }

    public void addData(){
        int position=getItemCount()+1;
        ArrayList<String> temp=new ArrayList<>();
        for (int i=0;i<parameter.size();i++) {
            temp.add("");
        }
        values.add(temp);
        notifyItemRangeInserted(position,parameter.size()+1);
    }

    class ValueViewHolder extends RecyclerView.ViewHolder {

        public final TextView parameterNameTextView;
        public final TextView parameterNameEditText;
        public final Button deleteButton;
        final InsertValueListAdapter adapter;

        public ValueViewHolder(@NonNull View itemView, InsertValueListAdapter adapter) {
            super(itemView);
            parameterNameTextView=itemView.findViewById(R.id.parameterNameTextView);
            parameterNameEditText=itemView.findViewById(R.id.valueEditText);
            deleteButton=itemView.findViewById(R.id.deleteButton);
            this.adapter=adapter;

            parameterNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position=getAdapterPosition();
                    int i=position/values.get(0).size();
                    int j=position%values.get(0).size();
                    ArrayList<String>temp=values.get(i);
                    temp.set(j,s.toString());
                    values.set(i,temp);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=getAdapterPosition()/(parameter.size()+1);
                    values.remove(i);
                    notifyItemRangeRemoved(i*(parameter.size()+1),parameter.size()+1);
                }
            });
        }

    }
}
