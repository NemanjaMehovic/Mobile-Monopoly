package com.example.pmuprojekat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.R;
import com.example.pmuprojekat.databinding.FragmentHistoryBinding;
import com.example.pmuprojekat.databinding.FragmentStartBinding;
import com.example.pmuprojekat.dialog.TradeRecycleViewAdapter;
import com.example.pmuprojekat.replay.HistoryRecycleViewAdapter;


public class historyFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryRecycleViewAdapter recycleView;
    private MainActivity mainActivity;

    public historyFragment() {
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        binding.deleteAllButton.setOnClickListener(v -> {
            MainActivity.repository.deleteAllFinishedGames();
        });


        recycleView = new HistoryRecycleViewAdapter(mainActivity);
        binding.recyclerViewHistory.setHasFixedSize(true);
        binding.recyclerViewHistory.setAdapter(recycleView);
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(mainActivity));

        MainActivity.repository.getAllFinishedGamesLive().observe(getViewLifecycleOwner(), recycleView::setGames);

        return binding.getRoot();
    }
}