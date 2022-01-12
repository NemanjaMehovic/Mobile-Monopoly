package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RailroadBuyableField extends BuyableField {

    public RailroadBuyableField(String name, int price) {
        super(name, price);
    }

    @Override
    public void setHousesOwned(int housesOwned) {
        //DOES NOTHING
    }

    @Override
    public int getAmount(Player p) {
        int numOwned = 0;
        for (BuyableField field : getOwner().getOwned())
            if (getRentPrices() == field.getRentPrices())
                numOwned++;
        return getRentPrices().get(numOwned - 1);
    }

    @Override
    public String getType() {
        return "Railroad";
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " Railroad";
    }
}
