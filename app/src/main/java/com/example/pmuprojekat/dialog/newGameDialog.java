package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.databinding.DialogNewgameBinding;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class newGameDialog extends DialogFragment {

    private DialogNewgameBinding binding;
    private MainActivity mainActivity;


    public newGameDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogNewgameBinding.inflate(inflater, container, false);
        binding.submitPlayers.setOnClickListener(v -> {
            getDialog().dismiss();
            List<Player> list = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            nameList.add(binding.player1Name.getText().toString());
            nameList.add(binding.player2Name.getText().toString());
            nameList.add(binding.player3Name.getText().toString());
            nameList.add(binding.player4Name.getText().toString());
            nameList.add(binding.player5Name.getText().toString());
            nameList.add(binding.player6Name.getText().toString());
            nameList.add(binding.player7Name.getText().toString());
            nameList.add(binding.player8Name.getText().toString());
            for (String s : nameList)
                if (!s.equals(""))
                    list.add(new Player(s));
            if (list.size() >= 2) {
                mainActivity.resetGame();
                Game.getInstance().setPlayers(list);
                MainActivity.repository.deleteActiveGame();
                MainActivity.repository.startNewGame();
                mainActivity.switchFragment(MainActivity.FRAGMENTS[MainActivity.GAME_FRAGMENT]);
            } else
                Toast.makeText(mainActivity, "Minimum two players needed!", Toast.LENGTH_SHORT).show();
        });
        return binding.getRoot();
    }
}
