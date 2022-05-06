package com.example.databasemanipulator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Fragment_2 extends Fragment {

    private EditText customSqlEditText;
    private Button executeButton;
    private RecyclerView recyclerView;
    private ConsoleCommandAdapter dataListAdapter;
    private ArrayList<String> data = new ArrayList<>();

    public Fragment_2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        executeButton = getView().findViewById(R.id.executeButton_2);
        customSqlEditText = getView().findViewById(R.id.customSqlEditText_2);

        recyclerView = getView().findViewById(R.id.recyclerView2);
        dataListAdapter = new ConsoleCommandAdapter(getContext(), data);
        recyclerView.setAdapter(dataListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.add(customSqlEditText.getText().toString());
                dataListAdapter.notifyItemInserted(data.size());
                Callable<Boolean> callable = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Manipulator manipulator = new Manipulator();
                        return manipulator.execute(customSqlEditText.getText().toString());
                    }
                };
                FutureTask futureTask = new FutureTask(callable);
                new Thread(futureTask).start();
                Boolean flag = false;
                try {
                    flag = (Boolean) futureTask.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag == false) {
                    data.add("Execute Failed");
                } else {
                    data.add("Execute Success");
                }
                dataListAdapter.notifyItemInserted(data.size());
            }
        });
    }
}