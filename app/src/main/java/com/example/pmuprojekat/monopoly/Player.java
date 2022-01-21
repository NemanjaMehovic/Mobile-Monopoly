package com.example.pmuprojekat.monopoly;

import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Fields.Field;
import com.example.pmuprojekat.monopoly.Fields.PropertyBuyableField;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private static final int StartingMoney = 1500;
    private List<BuyableField> owned;
    private int currMoney;
    private String playerName;
    private int position;
    private int jailTime;
    private int numRolled;
    private String chanceJailFree;
    private String chestJailFree;
    private boolean lost;

    public Player(String playerName) {
        this.playerName = playerName;
        this.currMoney = StartingMoney;
        this.position = 0;
        this.jailTime = 0;
        this.numRolled = 1;
        this.owned = new ArrayList<>();
        this.chanceJailFree = "";
        this.chestJailFree = "";
        this.lost = false;
    }

    public List<BuyableField> getOwned() {
        return owned;
    }

    public void setOwned(List<BuyableField> owned) {
        this.owned = owned;
    }

    public boolean removeOwned(BuyableField field) {
        return owned.remove(field);
    }

    public boolean addOwned(BuyableField field) {
        if (owned.contains(field))
            return false;
        owned.add(field);
        return true;
    }

    public int getCurrMoney() {
        return currMoney;
    }

    public void setCurrMoney(int currMoney) {
        this.currMoney = currMoney;
    }

    public int addMoney(int amount) {
        currMoney += amount;
        return currMoney;
    }

    public boolean removeMoney(int amount) {
        if ((currMoney - amount) < 0)
            return false;
        addMoney(-amount);
        return true;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int move(int num) {
        position = (position + num) % 40;
        return position;
    }

    public int getJailTime() {
        return jailTime;
    }

    public void setJailTime(int jailTime) {
        this.jailTime = jailTime;
    }

    public int updateJailTime() {
        if (jailTime > 0)
            jailTime--;
        return jailTime;
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

    public String getPlayerName() {
        return playerName;
    }

    public int getNumOfHouses() {
        int num = 0;
        for (BuyableField field : owned) {
            int tmp = field.getHousesOwned();
            num += tmp < 5 ? tmp : 0;
        }
        return num;
    }

    public int getNumOfHotels() {
        int num = 0;
        for (BuyableField field : owned) {
            int tmp = field.getHousesOwned();
            num += tmp > 4 ? 1 : 0;
        }
        return num;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public List<BuyableField> getAllFieldsThatCanBeMortgaged() {
        List<BuyableField> list = new ArrayList<>();
        for (BuyableField b : owned)
            if (b.getHousesOwned() == 0 && !b.isMortgage())
                list.add(b);
        return list;
    }

    public List<BuyableField> getAllMortgagedFields() {
        List<BuyableField> list = new ArrayList<>();
        for (BuyableField b : owned)
            if (b.isMortgage())
                list.add(b);
        return list;
    }

    private List<BuyableField> getAllSameTypeFields(BuyableField field) {
        List<BuyableField> allBuyableFields = Game.getInstance().getBuyableFields();
        List<BuyableField> list = new ArrayList<>();

        for (BuyableField f : allBuyableFields)
            if (f.getType().equals(field.getType()))
                list.add(f);

        return list;
    }

    public List<BuyableField> getAllFieldsWithProperty() {
        List<BuyableField> list = new ArrayList<>();
        for (BuyableField b : owned)
            if (b.getHousesOwned() != 0)
                list.add(b);
        return list;
    }

    public List<BuyableField> getAllFieldsThatCanHaveAProperty() {
        List<BuyableField> list = new ArrayList<>();

        for (BuyableField field : owned) {
            if (!field.canHaveHouse() || field.getHousesOwned() == 5 || field.isMortgage())
                continue;
            List<BuyableField> tmp = getAllSameTypeFields(field);
            int ownedNum = 0;
            for (BuyableField tmpField : tmp)
                if (tmpField.getOwner() == this)
                    ownedNum++;
            if (ownedNum == tmp.size())
                list.add(field);
        }

        return list;
    }

    public List<BuyableField> getAllTradableFields()
    {
        List<BuyableField> list = new ArrayList<>();

        for(BuyableField field:owned)
        {
            if(field.getHousesOwned() != 0)
                continue;
            list.add(field);
            List<BuyableField> tmp = getAllSameTypeFields(field);
            for(BuyableField tmpField:tmp)
                if(tmpField.getHousesOwned() != 0)
                {
                    list.remove(field);
                    break;
                }
        }
        return list;
    }
}
