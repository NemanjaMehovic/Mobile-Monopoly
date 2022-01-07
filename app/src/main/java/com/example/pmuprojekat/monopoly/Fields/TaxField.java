package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Player;

public class TaxField extends Field {

    private int taxNum;

    public TaxField(String name, int taxNum) {
        super(name);
        this.taxNum = taxNum;
    }

    @Override
    public void effect(Player p) {
        //TODO player not implemented yet
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + taxNum;
    }
}
