package com.shenby.launcher;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shenby.launcher.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(mainBinding.fragmentContainerView.getId(), AppListFragment.newInstance())
                    .commit();
        }
    }
}