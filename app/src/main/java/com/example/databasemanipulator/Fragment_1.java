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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Fragment_1 extends Fragment {

    private ArrayList<ArrayList<String>> data=new ArrayList<>();
    private RecyclerView recyclerView;
    private DataListAdapter adapter;
    private Button button;
    private static Manipulator manipulator = new Manipulator();
    private EditText conditionEditText,customSqlEditText;

    public Fragment_1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.recyclerView);
        conditionEditText = getView().findViewById(R.id.conditionEditText_1);
        customSqlEditText=getView().findViewById(R.id.cusomtSqlEditText_1);
        button = getView().findViewById(R.id.executeButton);

        adapter = new DataListAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data=new ArrayList<>();
                //adapter.notifyDataSetChanged();
                update();
                Callable<ArrayList<ArrayList<String>>> callable = new Callable<ArrayList<ArrayList<String>>>() {
                    @Override
                    public ArrayList<ArrayList<String>> call() {
                        if (customSqlEditText.getText().toString().equals("")==false)
                            return manipulator.query(customSqlEditText.getText().toString(),Manipulator.filter, Manipulator.tableName, conditionEditText.getText().toString());
                        else
                            return manipulator.query("",Manipulator.filter, Manipulator.tableName, conditionEditText.getText().toString());

                    }
                };
                FutureTask<ArrayList<ArrayList<String>>> futureTask = new FutureTask<>(callable);
                new Thread(futureTask).start();
                ArrayList<ArrayList<String>> ans=null;

                try {
                    ans= futureTask.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (ans!=null&&ans.size()>0) {
                    data.clear();
                    for (int i=0;i<ans.size();i++){
                        ArrayList<String>temp=ans.get(i);
                        temp.add(" ");
                        data.add(ans.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.e(this.getClass().getSimpleName(),"Query Failed");
                    Toast.makeText(getContext(), "Error:please make sure your SQL statement is true or this data isn't exist in the table", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);
    }

    public void update() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Manipulator.update(
                sharedPreferences.getString("Filter", "*"),
                sharedPreferences.getString("TableName", "test")
        );
    }
}