package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Player;

public class PropertyBuyableField extends BuyableField {

    private String color;

    public PropertyBuyableField(String name, int price, String color) {
        super(name, price);
        this.color = color;
    }

    @Override
    public void effect(Player p) {
        //TODO player not implemented yet
    }

    public String getColor() {
        return color;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + color;
    }
}
