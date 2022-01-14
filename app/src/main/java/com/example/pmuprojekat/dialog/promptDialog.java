package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.databinding.DialogPromptBinding;

public class promptDialog extends DialogFragment {

    public interface Callback{
        public void invoked();
    }

    private Callback onFinish;
    private Callback onCancle;
    private String info;
    private DialogPromptBinding binding;

    public promptDialog(Callback onFinish, Callback onCancle, String info) {
        this.onFinish = onFinish;
        this.onCancle = onCancle;
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogPromptBinding.inflate(inflater, container, false);

        binding.promptTextView.setText(info);
        binding.callBackButton.setOnClickListener(v -> {
            getDialog().dismiss();
            onFinish.invoked();
        });

        binding.cancelButton.setOnClickListener(v -> {
            getDialog().dismiss();
            onCancle.invoked();
        });

        return binding.getRoot();
    }
}
