package com.example.pmuprojekat.monopoly.Fields;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.List;

public class ToJailField extends Field {

    public ToJailField(String name) {
        super(name);
    }

    @Override
    public void effect(Player p) {
        p.setJailTime(3);
        p.setPosition(Game.getInstance().getToJailField().getId());
    }
}
