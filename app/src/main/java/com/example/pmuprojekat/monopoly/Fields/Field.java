package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Player;

public class Field {

    static int ID = 0;
    private int id;
    private String name;

    public static void reset() {
        ID = 0;
    }

    public void effect(Player p) {
        //By default fields have no effect on the player
    }

    public Field(String name) {
        this.name = name;
        this.id = ID++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return id + " " + name;
    }
}
