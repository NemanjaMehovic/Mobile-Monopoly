package com.example.pmuprojekat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.app.Fragment;
import android.os.Bundle;

import com.example.pmuprojekat.databinding.ActivityMainBinding;
import com.example.pmuprojekat.fragments.startFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        startFragment fragment = new startFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainContentFrame, fragment,"MAIN_FRAGMENT")
                .show(fragment)
                .commit();
        setContentView(binding.getRoot());
    }
}