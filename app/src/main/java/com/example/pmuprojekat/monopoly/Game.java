package com.example.pmuprojekat.monopoly;

import android.graphics.RadialGradient;

import com.example.pmuprojekat.fragments.gameFragment;
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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private static final int NumOfHouses = 32;
    private static final int NumOfHotels = 12;

    private static Game instance;
    private List<Field> fields;
    private List<BuyableField> buyableFields;
    private List<Player> players;
    private Player currPlayer;
    private int currPlayerNum;
    private gameFragment fragment;
    private boolean back = false;

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

    public List<BuyableField> getBuyableFields() {
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
        for (Field f : fields)
            System.out.println(f);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        currPlayer = this.players.get(0);
        currPlayerNum = 0;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public int getCurrPlayerNum() {
        return currPlayerNum;
    }

    public void setFragment(gameFragment fragment) {
        this.fragment = fragment;
    }

    public void nextPlayer() {
        currPlayerNum = (currPlayerNum + 1) % players.size();
        currPlayer = players.get(currPlayerNum);
        currPlayer.updateJailTime();
    }

    static boolean test = false;

    public void rollDice() {
        if (currPlayer.getJailTime() != 0)
            return;
        Random random = new Random();
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;

        currPlayer.setNumRolled(dice1 + dice2);
        int startingPosition = currPlayer.getPosition();
        currPlayer.move(dice1 + dice2);
        int positionAfterDiceRoll = currPlayer.getPosition();
        if (positionAfterDiceRoll < startingPosition)
            currPlayer.addMoney(200);

        fragment.numberRolled(dice1, dice2, startingPosition);
        fragment.updateCurrData(currPlayer);
        fields.get(currPlayer.getPosition()).effect(currPlayer);

        int positionAfterEffect = currPlayer.getPosition();
        if(positionAfterEffect != positionAfterDiceRoll) {
            int tmpPosition = positionAfterDiceRoll;
            int i = 0;
            while(tmpPosition !=positionAfterEffect)
            {
                tmpPosition = (tmpPosition + 1) % 40;
                i++;
            }
            fragment.movePlayer(i, positionAfterDiceRoll);
            if(positionAfterEffect < positionAfterDiceRoll && currPlayer.getJailTime() == 0 && !back)
                currPlayer.addMoney(200);
            fields.get(currPlayer.getPosition()).effect(currPlayer);
        }
        back = false;
        fragment.updateCurrData(currPlayer);
    }

    public void notEnoughMoney(Player p, int needed) {
        //TODO implement
        //TODO check if the player p is current player?
    }

    public void offerToBuy(Player p, BuyableField field) {
        String info = "Do you wish to buy " + field.getName() + " for $" + field.getPrice();
        fragment.setPromptDialog(() -> {
            acceptedToBuy(p, field);
        }, () -> {
            startAuction(field);
        }, info);
    }

    private void acceptedToBuy(Player p, BuyableField field) {
        if (p.getCurrMoney() >= field.getPrice()) {
            p.removeMoney(field.getPrice());
            p.addOwned(field);
            field.setOwner(p);
            fragment.updateCurrData(p);
        } else
            startAuction(field);
    }

    private void startAuction(BuyableField field) {
        //TODO implement
    }

    public void chanceChestCardGotten(String s, Boolean back) {
        this.back = back;
        fragment.setToast(s);
    }
}
