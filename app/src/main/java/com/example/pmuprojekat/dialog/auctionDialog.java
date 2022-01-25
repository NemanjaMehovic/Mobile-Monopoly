package com.example.pmuprojekat.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.databinding.DialogNewgameBinding;
import com.example.pmuprojekat.fragments.gameFragment;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class auctionDialog extends DialogFragment {


    private DialogNewgameBinding binding;
    private MainActivity mainActivity;
    private BuyableField field;
    List<TextInputEditText> textInputs;

    public auctionDialog(MainActivity mainActivity, BuyableField field) {
        this.mainActivity = mainActivity;
        this.field = field;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogNewgameBinding.inflate(inflater, container, false);
        binding.anchorTop.setText("Auction for " + field.getName());

        textInputs = new ArrayList<>();
        textInputs.add(binding.player1Name);
        textInputs.add(binding.player2Name);
        textInputs.add(binding.player3Name);
        textInputs.add(binding.player4Name);
        textInputs.add(binding.player5Name);
        textInputs.add(binding.player6Name);
        textInputs.add(binding.player7Name);
        textInputs.add(binding.player8Name);

        for(TextInputEditText editText : textInputs) {
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setBackgroundColor(Color.BLACK);
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }

        List<Player> playerList = Game.getInstance().getPlayers();

        for(int i = 0 ; i < playerList.size(); i++)
            if(!playerList.get(i).isLost()) {
                textInputs.get(i).setEnabled(true);
                textInputs.get(i).setBackgroundColor(Color.LTGRAY);
            }

        binding.submitPlayers.setOnClickListener(v -> {
            try {
                int max = -1;
                Player winner = null;
                for(int i = 0; i < textInputs.size(); i++)
                {
                    TextInputEditText tmp = textInputs.get(i);
                    if(tmp.isEnabled())
                    {
                        double tmpVal = Double.parseDouble(tmp.getText().toString());
                        if((int)tmpVal > max)
                        {
                            max = (int) tmpVal;
                            winner = Game.getInstance().getPlayers().get(i);
                        }
                    }
                }
                if(max >= 0)
                {
                    if(winner.getCurrMoney() > max) {
                        Game.getInstance().boughtField(winner, field, max);
                        gameFragment.auctionDialogWasActive = false;
                        getDialog().dismiss();
                    }
                    else
                        Toast.makeText(mainActivity, "Not enough money to cover cost", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(mainActivity, "Winning bid must be a positive number", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(mainActivity, "Not a number entered", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}
