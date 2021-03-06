package com.example.pmuprojekat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.R;
import com.example.pmuprojekat.databinding.FragmentGameBinding;
import com.example.pmuprojekat.databinding.FragmentOptionsBinding;


public class optionsFragment extends Fragment {


    private FragmentOptionsBinding binding;
    private MainActivity mainActivity;

    public optionsFragment() {
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
        binding = FragmentOptionsBinding.inflate(inflater, container, false);

        binding.sliderTime.setValue(mainActivity.waitTimeBetweenShakesInSec.getValue());
        binding.sliderWeight.setValue(mainActivity.shakeThreshold.getValue().floatValue());

        binding.optionsSet.setOnClickListener(v -> {
            int time = (int) binding.sliderTime.getValue();
            double shake = binding.sliderWeight.getValue();
            mainActivity.update(time, shake);
            mainActivity.getSupportFragmentManager().popBackStack();
        });

        return binding.getRoot();
    }
}