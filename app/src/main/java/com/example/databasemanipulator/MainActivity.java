package com.example.databasemanipulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES=3;
    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;

    private RadioGroup radioGroup;
    boolean isPageChangedByRadioGroup=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Manipulator manipulator=new Manipulator();
        Manipulator.DatabaseUrl="jdbc:mysql://gz-cynosdbmysql-grp-5r17wgzb.sql.tencentcdb.com:26316/Users";
        Manipulator.DatabaseUserName="root";
        Manipulator.DatabasePassword="aA877783019";
        new Thread(new Runnable() {
            @Override
            public void run() {
                manipulator.connect();

            }
        }).start();


        radioGroup=findViewById(R.id.radioGroup);

        viewPager=findViewById(R.id.viewPager);
        fragmentStateAdapter= new myAdapter(this);
        viewPager.setAdapter(fragmentStateAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                isPageChangedByRadioGroup=true;
                switch (position){
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;
                    default:
                        break;
                }
                isPageChangedByRadioGroup=false;
            }
        });
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;
            @Override
            public void transformPage(@NonNull View page, float position) {
                int pageWidth = page.getWidth();
                int pageHeight = page.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0f);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        page.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        page.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    page.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0f);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isPageChangedByRadioGroup==false) {
                    if (checkedId == R.id.radioButton)
                        viewPager.setCurrentItem(0);
                    else if (checkedId == R.id.radioButton2)
                        viewPager.setCurrentItem(1);
                    else if (checkedId == R.id.radioButton3)
                        viewPager.setCurrentItem(2);
                }

            }
        });

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Manipulator.DatabaseUrl=sharedPreferences.getString("DatabaseUrl",this.getString(R.string.defaultDatabaseUrl));
        Manipulator.DatabaseUserName =sharedPreferences.getString("DatabaseUserName",this.getString(R.string.defaultDatabaseUser));
        Manipulator.DatabasePassword =sharedPreferences.getString("DatabasePassword",this.getString(R.string.defaultDatabasePassword));

        if(sharedPreferences.getBoolean("FirstRun",false)==false){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setMessage("Please set up some settings on the first run");
            alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                }
            });
            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            sharedPreferences.edit().putBoolean("FirstRun",true);
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }


    private static class myAdapter extends FragmentStateAdapter{

        public myAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 1:
                    return new Fragment_1();
                case 2:
                    return new Fragment_2();
                default:
                    return new Fragment_0();
            }
        }
    }
}