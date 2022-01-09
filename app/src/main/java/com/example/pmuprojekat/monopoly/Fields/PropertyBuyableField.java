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

    public String getColor() {
        return color;
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
        List<PropertyBuyableField> fields = Game.getInstance().getPropertyBuyableFields();
        int numExist = 0;
        int numOwned = 0;
        for(PropertyBuyableField f:fields)
            if(f.getColor().equals(getColor())) {
                numExist++;
                if(owner.getOwned().contains(f))
                    numOwned++;
            }
        return numOwned == numExist ? getRentPrices().get(0) * 2 : getRentPrices().get(0);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + color  + " " + getHouseHotelPrice();
    }
}
