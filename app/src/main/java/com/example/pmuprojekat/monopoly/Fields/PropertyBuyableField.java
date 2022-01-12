package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.List;

public class PropertyBuyableField extends BuyableField {

    private String color;

    public PropertyBuyableField(String name, int price, String color) {
        super(name, price);
        this.color = color;
    }

    @Override
    public boolean canHaveHouse() {
        return true;
    }

    @Override
    public int getAmount(Player p) {
        Player owner = getOwner();
        int housesOwned = getHousesOwned();
        if(housesOwned > 0)
            return getRentPrices().get(housesOwned);
        List<BuyableField> fields = Game.getInstance().getBuyableFields();
        int numExist = 0;
        int numOwned = 0;
        for(BuyableField f:fields)
            if(f.getType().equals(getType())) {
                numExist++;
                if(owner.getOwned().contains(f))
                    numOwned++;
            }
        return numOwned == numExist ? getRentPrices().get(0) * 2 : getRentPrices().get(0);
    }

    @Override
    public String getType() {
        return color;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + color  + " " + getHouseHotelPrice();
    }
}
