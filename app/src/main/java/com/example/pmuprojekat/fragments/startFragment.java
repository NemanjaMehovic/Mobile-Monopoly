package com.example.pmuprojekat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.R;
import com.example.pmuprojekat.database.GameEntity;
import com.example.pmuprojekat.databinding.FragmentStartBinding;
import com.example.pmuprojekat.dialog.newGameDialog;
import com.example.pmuprojekat.shake.LifeCycleGameObserver;

import java.util.List;


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
        binding = FragmentStartBinding.inflate(inflater, container, false);

        List<GameEntity> runningGames = mainActivity.repository.getAllRunningGames();

        binding.resumeButton.setEnabled(runningGames.size() > 0);

        binding.resumeButton.setOnClickListener(v -> {
            mainActivity.switchFragment(MainActivity.FRAGMENTS[MainActivity.GAME_FRAGMENT]);
        });

        binding.newGameButton.setOnClickListener(v -> {
            newGameDialog dialog = new newGameDialog(mainActivity);
            dialog.show(mainActivity.getSupportFragmentManager(), "PlayerNames");
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