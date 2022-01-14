package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.databinding.DialogInfoBinding;

public class infoDialog extends DialogFragment {

    private promptDialog.Callback onFinish;
    private String info;
    private DialogInfoBinding binding;


    public infoDialog(promptDialog.Callback onFinish, String info) {
        this.onFinish = onFinish;
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogInfoBinding.inflate(inflater, container, false);

        binding.infoTextView.setText(info);
        binding.infoButton.setOnClickListener(v -> {
            getDialog().dismiss();
            onFinish.invoked();
        });

        return binding.getRoot();
    }
}
