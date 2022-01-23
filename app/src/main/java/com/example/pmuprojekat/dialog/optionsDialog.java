package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.databinding.DialogFieldInfoBinding;
import com.example.pmuprojekat.databinding.FragmentOptionsBinding;

public class optionsDialog extends DialogFragment {

    private FragmentOptionsBinding binding;
    private MainActivity mainActivity;

    public optionsDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOptionsBinding.inflate(inflater, container, false);

        binding.sliderTime.setValue(mainActivity.waitTimeBetweenShakesInSec.getValue());
        binding.sliderWeight.setValue(mainActivity.shakeThreshold.getValue().floatValue());

        binding.optionsSet.setOnClickListener(v -> {
            int time = (int) binding.sliderTime.getValue();
            double shake = binding.sliderWeight.getValue();
            mainActivity.update(time, shake);
            getDialog().dismiss();
        });

        return binding.getRoot();
    }

}
