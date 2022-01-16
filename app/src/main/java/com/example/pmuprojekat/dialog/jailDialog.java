package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.databinding.DialogJailBinding;
import com.example.pmuprojekat.monopoly.Fields.ChanceChestField;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

public class jailDialog extends DialogFragment {

    private DialogJailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogJailBinding.inflate(inflater, container, false);

        Player currPlayer = Game.getInstance().getCurrPlayer();

        binding.getOutOfJailCard.setEnabled(!currPlayer.getChanceJailFree().equals("") || !currPlayer.getChestJailFree().equals(""));

        String text = "Player " + currPlayer.getPlayerName() + " has " + currPlayer.getJailTime() + " turn{s} left in jail";

        binding.jailTimeLeft.setText(text);

        binding.getOutOfJailCard.setOnClickListener(v -> {
            if (!currPlayer.getChanceJailFree().equals("")) {
                ChanceChestField.putBackChance(currPlayer.getChanceJailFree());
                currPlayer.setChanceJailFree("");
            } else {
                ChanceChestField.putBackChest(currPlayer.getChestJailFree());
                currPlayer.setChestJailFree("");
            }
            currPlayer.setJailTime(0);
            Game.getInstance().playerJailUpdate();
            getDialog().dismiss();
        });

        binding.payGetOutOfJail.setEnabled(currPlayer.getCurrMoney() >= 50);

        binding.payGetOutOfJail.setOnClickListener(v -> {
            currPlayer.removeMoney(50);
            currPlayer.setJailTime(0);
            Game.getInstance().playerJailUpdate();
            getDialog().dismiss();
        });

        binding.waitToGetOutOfJail.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        return binding.getRoot();
    }
}
