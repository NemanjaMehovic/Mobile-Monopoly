package com.example.pmuprojekat.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.databinding.DialogMortageBinding;
import com.example.pmuprojekat.databinding.DialogTradeBinding;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.ArrayList;
import java.util.List;

public class tradeDialog extends DialogFragment {

    private DialogTradeBinding binding;
    private List<BuyableField> offerFields;
    private List<BuyableField> receiveFields;
    private int offerMoney;
    private int receiveMoney;
    private MainActivity mainActivity;
    private List<Player> otherPlayers;
    private TradeRecycleViewAdapter adapterCurrPlayer;
    private TradeRecycleViewAdapter adapterOtherPlayer;
    private Player tradePlayer;
    private Player currPlayer;

    public tradeDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.offerFields = new ArrayList<>();
        this.receiveFields = new ArrayList<>();
        this.otherPlayers = new ArrayList<>();
        this.offerMoney = 0;
        this.receiveMoney = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogTradeBinding.inflate(inflater, container, false);


        currPlayer = Game.getInstance().getCurrPlayer();
        binding.currPlayerNameTextView.setText(currPlayer.getPlayerName());

        List<String> nameList = new ArrayList<>();
        for (Player p : Game.getInstance().getPlayers())
            if (p != currPlayer && !p.isLost()) {
                nameList.add(p.getPlayerName());
                otherPlayers.add(p);
            }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_item, nameList);
        binding.selectPlayerSpinner.setAdapter(adapter);
        binding.selectPlayerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receiveFields = new ArrayList<>();
                tradePlayer = otherPlayers.get(position);
                adapterOtherPlayer = new TradeRecycleViewAdapter(tradePlayer.getAllTradableFields());
                adapterOtherPlayer.setTradeFields(receiveFields);
                binding.receiveRecyclerView.setHasFixedSize(true);
                binding.receiveRecyclerView.setAdapter(adapterOtherPlayer);
                binding.receiveRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterCurrPlayer = new TradeRecycleViewAdapter(currPlayer.getAllTradableFields());
        adapterCurrPlayer.setTradeFields(offerFields);
        binding.offerRecyclerView.setHasFixedSize(true);
        binding.offerRecyclerView.setAdapter(adapterCurrPlayer);
        binding.offerRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        binding.tradeButtonAccept.setOnClickListener(v -> {
            try {
                String offerString = binding.offerMoneyEdit.getText().toString();
                String receiveString = binding.receiveMoneyEdit.getText().toString();
                offerMoney = offerString.equals("") ? 0 : (int) Double.parseDouble(offerString);
                receiveMoney = receiveString.equals("") ? 0 : (int) Double.parseDouble(receiveString);
                if (currPlayer.getCurrMoney() < offerMoney || tradePlayer.getCurrMoney() < receiveMoney) {
                    Toast.makeText(mainActivity, "Not enough money", Toast.LENGTH_SHORT).show();
                    return;
                }
                Game.getInstance().trade(tradePlayer, offerFields,offerMoney, receiveFields, receiveMoney);
                getDialog().dismiss();
            } catch (Exception e) {
                Toast.makeText(mainActivity, "Not a number entered", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tradeButtonCancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        return binding.getRoot();
    }
}
