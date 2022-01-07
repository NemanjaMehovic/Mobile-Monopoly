package com.example.pmuprojekat.monopoly.Fields;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class BuyableField extends Field {

    private int price;
    private boolean mortgage;
    private int housesOwned;
    private int houseHotelPrice;
    private List<Integer> rentPrices;

    public BuyableField(String name, int price) {
        super(name);
        this.price = price;
        this.mortgage = false;
        this.housesOwned = 0;
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

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " " + price;// + " " + rentPrices.toString();
    }
}
