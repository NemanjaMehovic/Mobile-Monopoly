package com.example.pmuprojekat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.R;
import com.example.pmuprojekat.databinding.FragmentStartBinding;
import com.example.pmuprojekat.dialog.newGameDialog;


public class startFragment extends Fragment {

    private FragmentStartBinding binding;
    private MainActivity mainActivity;

    public startFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false);

        binding.newGameButton.setOnClickListener(v -> {
            //mainActivity.switchFragment(MainActivity.FRAGMENTS[MainActivity.GAME_FRAGMENT]);
            newGameDialog dialog = new newGameDialog(mainActivity);
            dialog.show(mainActivity.getSupportFragmentManager(), "Test");
        });

        binding.optionsButton.setOnClickListener(v -> {
            mainActivity.switchFragment(MainActivity.FRAGMENTS[MainActivity.OPTIONS_FRAGMENT]);
        });

        binding.historyButton.setOnClickListener(v -> {
            mainActivity.switchFragment(MainActivity.FRAGMENTS[MainActivity.HISTORY_FRAGMENT]);
        });

        return binding.getRoot();
    }
}