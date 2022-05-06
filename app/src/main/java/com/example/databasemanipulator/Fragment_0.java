package com.example.databasemanipulator;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Fragment_0 extends Fragment {

    private Button addButton, confirmButton;
    private RecyclerView parameterRecyclerView, valuesRecyclerView;
    private InsertValueListAdapter valueListAdapter;
    private InsertParameterListAdapter parameterListAdapter;
    private ArrayList<String> parameterArrayList = new ArrayList<>();
    private ArrayList<ArrayList<String>> valueArrayList = new ArrayList<>();
    int mode = 1;

    public Fragment_0() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton = getView().findViewById(R.id.addButton);
        confirmButton = getView().findViewById(R.id.confirmButton);

        parameterRecyclerView = getView().findViewById(R.id.insertParameterRecyclerView);
        parameterListAdapter = new InsertParameterListAdapter(getContext(), parameterArrayList);
        parameterRecyclerView.setAdapter(parameterListAdapter);
        parameterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        valuesRecyclerView = getView().findViewById(R.id.insertValueRecyclerView);
        valueListAdapter = new InsertValueListAdapter(getContext(), valueArrayList, parameterArrayList);
        valuesRecyclerView.setAdapter(valueListAdapter);
        valuesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        parameterListAdapter.getItemId(1);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode == 1)
                    parameterListAdapter.addData();
                else if (mode == 2)
                    valueListAdapter.addData();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parameterArrayList.size() == 0) {
                    Toast.makeText(getContext(), "Please enter the parameter", Toast.LENGTH_SHORT).show();
                } else {
                    if (mode == 1) {
                        mode = 2;
                        Toast.makeText(getContext(), "Please enter the values", Toast.LENGTH_SHORT).show();
                    } else if (mode == 2) {
                        update();
                        Callable<Boolean>callable=new Callable<Boolean>() {
                            @Override
                            public Boolean call(){
                                Manipulator manipulator = new Manipulator();
                                return manipulator.insert(Manipulator.tableName, parameterArrayList, valueArrayList);
                            }
                        };
                        FutureTask<Boolean> futureTask=new FutureTask<>(callable);
                        new Thread(futureTask).start();
                        Boolean flag=false;
                        try {
                            flag=futureTask.get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (flag)
                            Toast.makeText(getContext(), "Insert Success", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Insert Failed", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
    }

    public void update() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Manipulator.update(
                sharedPreferences.getString("Filter", "*"),
                sharedPreferences.getString("TableName", "test")
        );
    }
}