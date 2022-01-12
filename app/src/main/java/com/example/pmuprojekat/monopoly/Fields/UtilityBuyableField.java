package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

public class UtilityBuyableField extends BuyableField {

    public UtilityBuyableField(String name, int price) {
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
        return getRentPrices().get(numOwned - 1) * p.getNumRolled();
    }

    @Override
    public String getType() {
        return "Utility";
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " Utility";
    }
}
