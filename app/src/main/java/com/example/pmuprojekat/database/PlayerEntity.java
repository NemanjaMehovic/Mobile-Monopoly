package com.example.pmuprojekat.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlayerEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long gameId;
    private String name;
    private int money;
    private int position;
    private int jailTime;
    private int numRolled;
    private String chanceJailFree;
    private String chestJailFree;
    private boolean lost;

    public PlayerEntity(long id, long gameId, String name, int money, int position, int jailTime, int numRolled, String chanceJailFree, String chestJailFree, boolean lost) {
        this.id = id;
        this.gameId = gameId;
        this.name = name;
        this.money = money;
        this.position = position;
        this.jailTime = jailTime;
        this.numRolled = numRolled;
        this.chanceJailFree = chanceJailFree;
        this.chestJailFree = chestJailFree;
        this.lost = lost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getJailTime() {
        return jailTime;
    }

    public void setJailTime(int jailTime) {
        this.jailTime = jailTime;
    }

    public int getNumRolled() {
        return numRolled;
    }

    public void setNumRolled(int numRolled) {
        this.numRolled = numRolled;
    }

    public String getChanceJailFree() {
        return chanceJailFree;
    }

    public void setChanceJailFree(String chanceJailFree) {
        this.chanceJailFree = chanceJailFree;
    }

    public String getChestJailFree() {
        return chestJailFree;
    }

    public void setChestJailFree(String chestJailFree) {
        this.chestJailFree = chestJailFree;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }
}
