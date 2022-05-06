package com.example.databasemanipulator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.sql.Connection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SettingsActivity extends AppCompatActivity {

    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        checkButton=findViewById(R.id.checkConnectionButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                Callable<Boolean> callable=new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Manipulator manipulator=new Manipulator();
                        Connection connection =manipulator.connect();
                        Boolean flag=false;
                        if (connection!=null)
                            flag=true;
                        close(connection);
                        return flag;
                    }
                };
                FutureTask<Boolean> futureTask=new FutureTask<>(callable);
                new Thread(futureTask).start();
                Boolean flag=false;
                try {
                    flag=futureTask.get();
                } catch (ExecutionException|InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag)
                    Toast.makeText(SettingsActivity.this, "Connection Success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SettingsActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public void update() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Manipulator.DatabaseUrl=sharedPreferences.getString("DatabaseUrl",this.getString(R.string.defaultDatabaseUrl));
        Manipulator.DatabaseUserName =sharedPreferences.getString("DatabaseUserName",this.getString(R.string.defaultDatabaseUser));
        Manipulator.DatabasePassword =sharedPreferences.getString("DatabasePassword",this.getString(R.string.defaultDatabasePassword));

        Manipulator.update(
                sharedPreferences.getString("Filter", ""),
                sharedPreferences.getString("TableName", "")
        );
    }

    public void close(AutoCloseable resource){
        try {
            resource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}