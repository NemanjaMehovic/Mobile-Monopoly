package com.example.pmuprojekat.monopoly;

import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Fields.ChanceChestField;
import com.example.pmuprojekat.monopoly.Fields.Field;
import com.example.pmuprojekat.monopoly.Fields.PropertyBuyableField;
import com.example.pmuprojekat.monopoly.Fields.RailroadBuyableField;
import com.example.pmuprojekat.monopoly.Fields.TaxField;
import com.example.pmuprojekat.monopoly.Fields.ToJailField;
import com.example.pmuprojekat.monopoly.Fields.UtilityBuyableField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private static final int NumOfHouses = 32;
    private static final int NumOfHotels = 12;
    private static Game instance;
    private List<Field> fields;
    private List<BuyableField> buyableFields;
    private List<Player> players;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<BuyableField> getBuyableFields(){
        return buyableFields;
    }

    public void initiateGame(String[] cardNames, int[] cardCosts, String[] cardTypes, String[] cardHousePrices, String[] cardRents) {
        Field.reset();
        fields = new ArrayList<>();
        buyableFields = new ArrayList<>();
        List<PropertyBuyableField> propertyBuyableFields = new ArrayList<>();
        List<RailroadBuyableField> railroadBuyableFields = new ArrayList<>();
        List<UtilityBuyableField> utilityBuyableFields = new ArrayList<>();
        for (int i = 0; i < cardNames.length; i++) {
            String[] splitType = cardTypes[i].split("-");
            switch (splitType[0]) {
                case "Go":
                case "Jail":
                case "Parking":
                    fields.add(new Field(cardNames[i]));
                    break;
                case "Chest":
                    fields.add(new ChanceChestField(cardNames[i], ChanceChestField.ChanceChestType.CHEST));
                    break;
                case "Chance":
                    fields.add(new ChanceChestField(cardNames[i], ChanceChestField.ChanceChestType.CHANCE));
                    break;
                case "GoJail":
                    fields.add(new ToJailField(cardNames[i]));
                    break;
                case "Tax":
                    fields.add(new TaxField(cardNames[i], cardCosts[i]));
                    break;
                case "Buy":
                    BuyableField tmp = null;
                    switch (splitType[1]) {
                        case "House":
                            tmp = new PropertyBuyableField(cardNames[i], cardCosts[i], splitType[2]);
                            propertyBuyableFields.add((PropertyBuyableField) tmp);
                            break;
                        case "Railroad":
                            tmp = new RailroadBuyableField(cardNames[i], cardCosts[i]);
                            railroadBuyableFields.add((RailroadBuyableField) tmp);
                            break;
                        case "Utility":
                            tmp = new UtilityBuyableField(cardNames[i], cardCosts[i]);
                            utilityBuyableFields.add((UtilityBuyableField) tmp);
                            break;
                        default:
                            System.out.println("Error unknown type of buyable.");
                            break;
                    }
                    fields.add(tmp);
                    buyableFields.add(tmp);
                    break;
                default:
                    System.out.println("Error unknown type.");
                    break;
            }
        }

        HashMap<String, Integer> map = new HashMap<>();
        for (String housePrice : cardHousePrices) {
            String[] split = housePrice.split("-");
            map.put(split[0], Integer.parseInt(split[1]));
        }
        for (PropertyBuyableField property : propertyBuyableFields)
            property.setHouseHotelPrice(map.get(property.getType()));

        HashMap<String, HashMap<String, List<Integer>>> rentPricesForCollors = new HashMap<>();
        List<Integer> utilRentPrices = new ArrayList<>();
        List<Integer> railroadRentPrices = new ArrayList<>();
        for (String rentPrices : cardRents) {
            String[] split = rentPrices.split("-");
            switch (split[0]) {
                case "Railroad":
                    railroadRentPrices.add(Integer.parseInt(split[1]));
                    break;
                case "Utility":
                    utilRentPrices.add(Integer.parseInt(split[1]));
                    break;
                default:
                    HashMap<String, List<Integer>> tmp = null;
                    List<Integer> tmpList = null;
                    if (rentPricesForCollors.containsKey(split[0]))
                        tmp = rentPricesForCollors.get(split[0]);
                    else {
                        tmp = new HashMap<>();
                        rentPricesForCollors.put(split[0], tmp);
                    }
                    if (tmp.containsKey(split[1]))
                        tmpList = tmp.get(split[1]);
                    else {
                        tmpList = new ArrayList<>();
                        tmp.put(split[1], tmpList);
                    }
                    tmpList.add(Integer.parseInt(split[2]));
                    break;
            }
        }
        for (RailroadBuyableField railroad : railroadBuyableFields)
            railroad.setRentPrices(railroadRentPrices);
        for (UtilityBuyableField util : utilityBuyableFields)
            util.setRentPrices(utilRentPrices);
        for (int i = 0; i < propertyBuyableFields.size() - 1; i++) {
            PropertyBuyableField property = propertyBuyableFields.get(i);
            PropertyBuyableField propertyNext = propertyBuyableFields.get(i + 1);
            HashMap<String, List<Integer>> tmp = rentPricesForCollors.get(property.getType());
            property.setRentPrices(
                    property.getType().equals(propertyNext.getType()) ? tmp.get("Cheap") : tmp.get("Expansive")
            );
        }
        PropertyBuyableField property = propertyBuyableFields.get(propertyBuyableFields.size() - 1);
        property.setRentPrices(rentPricesForCollors.get(property.getType()).get("Expansive"));
        for(Field f:fields)
            System.out.println(f);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void notEnoughMoney(Player p, int needed)
    {
        //TODO implement
        //TODO check if the player p is current player?
    }

    public void offerToBuy(Player p, BuyableField field)
    {
        //TODO implement
    }

    public void chanceChestCardGotten(String s)
    {
        //TODO implement
    }
}
