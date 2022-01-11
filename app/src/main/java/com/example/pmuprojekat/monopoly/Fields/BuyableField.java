package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.List;

public abstract class BuyableField extends Field {

    private int price;
    private boolean mortgage;
    private int housesOwned;
    private int houseHotelPrice;
    private List<Integer> rentPrices;
    private Player owner;

    public BuyableField(String name, int price) {
        super(name);
        this.price = price;
        this.mortgage = false;
        this.housesOwned = 0;
        this.owner = null;
    }

    public int getPrice() {
        return price;
    }

    public boolean isMortgage() {
        return mortgage;
    }

    public void setMortgage(boolean mortgage) {
        this.mortgage = mortgage;
    }

    public int getMortgage() {
        return price / 2;
    }

    public int getHousesOwned() {
        return housesOwned;
    }

    public void setHousesOwned(int housesOwned) {
        this.housesOwned = housesOwned;
    }

    public int getHouseHotelPrice() {
        return houseHotelPrice;
    }

    public void setHouseHotelPrice(int houseHotelPrice) {
        this.houseHotelPrice = houseHotelPrice;
    }

    public List<Integer> getRentPrices() {
        return rentPrices;
    }

    public void setRentPrices(List<Integer> rentPrices) {
        this.rentPrices = rentPrices;
    }

    public boolean canHaveHouse() {
        return false;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public abstract int getAmount(Player p);

    @Override
    public void effect(Player p) {
        if (!p.getOwned().contains(this)) {
            if (owner != null) {
                int amount = getAmount(p);
                owner.addMoney(amount);
                if (!p.removeMoney(amount))
                    Game.getInstance().notEnoughMoney(p, amount);
            } else
                Game.getInstance().offerToBuy(p, this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + price + " " + rentPrices.toString();
    }
}
