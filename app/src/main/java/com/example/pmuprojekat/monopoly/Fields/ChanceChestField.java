package com.example.pmuprojekat.monopoly.Fields;

import com.example.pmuprojekat.monopoly.Player;

public class ChanceChestField extends Field{

    public enum ChanceChestType{
        CHANCE,
        CHEST
    }

    private ChanceChestType type;

    public ChanceChestField(String name, ChanceChestType type) {
        super(name);
        this.type = type;
    }

    @Override
    public void effect(Player p) {
        //TODO player not implemented yet
    }

    public ChanceChestType getType() {
        return type;
    }
}
