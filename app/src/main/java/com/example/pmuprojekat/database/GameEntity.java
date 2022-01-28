package com.example.pmuprojekat.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class GameEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long gameLength;
    private boolean gameFinished;
    private String buyableFieldOwners;
    private String buyableFieldHouses;
    private String buyableFieldMortage;
    private String startDate;
    private int numOfHousesLeft;
    private int numOfHotelsLeft;
    private int currPlayerNum;
    private  boolean rolled;

    public GameEntity(long id, long gameLength, boolean gameFinished, String buyableFieldOwners, String buyableFieldHouses, String buyableFieldMortage, int numOfHousesLeft, int numOfHotelsLeft, int currPlayerNum, boolean rolled) {
        this.id = id;
        this.gameLength = gameLength;
        this.gameFinished = gameFinished;
        this.buyableFieldOwners = buyableFieldOwners;
        this.buyableFieldHouses = buyableFieldHouses;
        this.buyableFieldMortage = buyableFieldMortage;
        this.numOfHousesLeft = numOfHousesLeft;
        this.numOfHotelsLeft = numOfHotelsLeft;
        this.currPlayerNum = currPlayerNum;
        this.rolled = rolled;
        Date start = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.startDate = format.format(start);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameLength() {
        return gameLength;
    }

    public void setGameLength(long gameLength) {
        this.gameLength = gameLength;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public String getBuyableFieldOwners() {
        return buyableFieldOwners;
    }

    public void setBuyableFieldOwners(String buyableFieldOwners) {
        this.buyableFieldOwners = buyableFieldOwners;
    }

    public String getBuyableFieldHouses() {
        return buyableFieldHouses;
    }

    public void setBuyableFieldHouses(String buyableFieldHouses) {
        this.buyableFieldHouses = buyableFieldHouses;
    }

    public String getBuyableFieldMortage() {
        return buyableFieldMortage;
    }

    public void setBuyableFieldMortage(String buyableFieldMortage) {
        this.buyableFieldMortage = buyableFieldMortage;
    }

    public int getNumOfHousesLeft() {
        return numOfHousesLeft;
    }

    public void setNumOfHousesLeft(int numOfHousesLeft) {
        this.numOfHousesLeft = numOfHousesLeft;
    }

    public int getNumOfHotelsLeft() {
        return numOfHotelsLeft;
    }

    public void setNumOfHotelsLeft(int numOfHotelsLeft) {
        this.numOfHotelsLeft = numOfHotelsLeft;
    }

    public int getCurrPlayerNum() {
        return currPlayerNum;
    }

    public void setCurrPlayerNum(int currPlayerNum) {
        this.currPlayerNum = currPlayerNum;
    }

    public boolean isRolled() {
        return rolled;
    }

    public void setRolled(boolean rolled) {
        this.rolled = rolled;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }
}
