package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

public class TaxField extends Field {

    private int taxNum;

    public TaxField(String name, int taxNum) {
        super(name);
        this.taxNum = taxNum;
    }

    @Override
    public void effect(Player p) {
        if (!p.removeMoney(taxNum))
            Game.getInstance().notEnoughMoney(p);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + taxNum;
    }
}
