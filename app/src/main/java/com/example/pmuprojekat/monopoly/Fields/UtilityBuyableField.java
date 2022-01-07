package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Player;

public class UtilityBuyableField extends BuyableField {

    public UtilityBuyableField(String name, int price) {
        super(name, price);
    }

    @Override
    public void effect(Player p) {
        //TODO player not implemented yet
    }

    @Override
    public void setHousesOwned(int housesOwned) {
        //DOES NOTHING
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " Utility";
    }
}
