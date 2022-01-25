package com.example.pmuprojekat.monopoly;

import android.graphics.RadialGradient;
import android.util.Log;

import com.example.pmuprojekat.MainActivity;
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
    private boolean back;
    private int numOfHousesLeft;
    private int numOfHotelsLeft;
    private boolean gameFinished;
    private boolean alreadyRolled;

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
        numOfHotelsLeft = NumOfHotels;
        numOfHousesLeft = NumOfHouses;
        gameFinished = false;
        alreadyRolled = false;
        back = false;
        fields = new ArrayList<>();
        buyableFields = new ArrayList<>();
        players = new ArrayList<>();
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
                            Log.d(MainActivity.LOG_TAG, "Error unknown type of buyable.");
                            break;
                    }
                    fields.add(tmp);
                    buyableFields.add(tmp);
                    break;
                default:
                    Log.d(MainActivity.LOG_TAG, "Error unknown type.");
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
            Log.d(MainActivity.LOG_TAG, f.toString());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        currPlayer = this.players.get(0);
        currPlayerNum = 0;
    }

    public boolean isAlreadyRolled() {
        return alreadyRolled;
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

    public void nextPlayer() {
        if (gameFinished)
            return;
        do {
            currPlayerNum = (currPlayerNum + 1) % players.size();
            currPlayer = players.get(currPlayerNum);
        } while (currPlayer.isLost());
        alreadyRolled = false;
        currPlayer.updateJailTime();
        playerJailUpdate();
        fragment.updateCurrData(currPlayer);
    }

    public void rollDice() {
        if (currPlayer.getJailTime() != 0 || gameFinished)
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
        fields.get(currPlayer.getPosition()).effect(currPlayer);

        int positionAfterEffect = currPlayer.getPosition();
        if (positionAfterEffect != positionAfterDiceRoll) {
            int tmpPosition = positionAfterDiceRoll;
            int i = 0;
            while (tmpPosition != positionAfterEffect) {
                tmpPosition = (tmpPosition + 1) % 40;
                i++;
            }
            fragment.movePlayer(i, positionAfterDiceRoll);
            if (positionAfterEffect < positionAfterDiceRoll && currPlayer.getJailTime() == 0 && !back)
                currPlayer.addMoney(200);
            fields.get(currPlayer.getPosition()).effect(currPlayer);
        }
        back = false;
        alreadyRolled = true;
        if (currPlayer.isLost())
            nextPlayer();
        else
            fragment.jailOptions();
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void notEnoughMoney(Player p, int needed) {
        p.setLost(true);
        if (currPlayer != p)
            takeEverything(currPlayer, p);
        else {
            int position = p.getPosition();
            Field field = fields.get(position);
            if (field instanceof BuyableField) {
                BuyableField f = (BuyableField) field;
                takeEverything(f.getOwner(), currPlayer);
            } else {
                numOfHousesLeft += p.getNumOfHouses();
                numOfHotelsLeft += p.getNumOfHotels();
                if (!p.getChestJailFree().equals("")) {
                    ChanceChestField.putBackChest(p.getChestJailFree());
                    p.setChestJailFree("");
                }
                if (!p.getChanceJailFree().equals("")) {
                    ChanceChestField.putBackChance(p.getChanceJailFree());
                    p.setChanceJailFree("");
                }
                for (BuyableField f : p.getOwned()) {
                    f.setOwner(null);
                    f.setMortgage(false);
                    f.setHousesOwned(0);
                }
                p.setOwned(null);
                p.setCurrMoney(0);
            }
        }
        fragment.updatePlayers();
        fragment.setToast("Player " + p.getPlayerName() + " went bankrupt");
        MainActivity.repository.update();
        int flag = 0;
        Player possibleWinner = null;
        for (Player player : players)
            if (!player.isLost()) {
                flag++;
                possibleWinner = player;
            }
        if (flag == 1) {
            gameFinished = true;
            MainActivity.repository.update();
            fragment.setInfoDIalog(() -> {
                fragment.finishedGame();
            }, "Winner is " + possibleWinner.getPlayerName());
        }
    }

    private void takeEverything(Player take, Player give) {
        if (!give.getChestJailFree().equals("")) {
            take.setChestJailFree(give.getChestJailFree());
            give.setChestJailFree("");
        }
        if (!give.getChanceJailFree().equals("")) {
            take.setChanceJailFree(give.getChanceJailFree());
            give.setChanceJailFree("");
        }
        for (BuyableField field : give.getOwned()) {
            field.setOwner(take);
            take.addOwned(field);
        }
        give.setOwned(null);
        take.addMoney(give.getCurrMoney());
        give.setCurrMoney(0);
    }

    public void offerToBuy(Player p, BuyableField field) {
        String info = "Do you wish to buy " + field.getName() + " for $" + field.getPrice();
        gameFragment.buyDialogWasActive = true;
        fragment.setPromptDialog(() -> {
            acceptedToBuy(p, field);
        }, () -> {
            startAuction(field);
        }, info);
    }

    private void acceptedToBuy(Player p, BuyableField field) {
        gameFragment.buyDialogWasActive = false;
        if (p.getCurrMoney() >= field.getPrice()) {
            boughtField(p, field, field.getPrice());
        } else
            startAuction(field);
    }

    private void startAuction(BuyableField field) {
        gameFragment.auctionDialogWasActive = true;
        gameFragment.buyDialogWasActive = false;
        fragment.startAuction(field);
    }

    public void boughtField(Player p, BuyableField field, int price) {
        p.removeMoney(price);
        p.addOwned(field);
        field.setOwner(p);
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void chanceChestCardGotten(String s, Boolean back) {
        this.back = back;
        fragment.setToast(s);
    }

    public void buyHouse(BuyableField field) {
        if (field.getHouseHotelPrice() > currPlayer.getCurrMoney())
            return;
        if (field.getHousesOwned() == 4 && numOfHotelsLeft > 0) {
            numOfHousesLeft += 4;
            field.setHousesOwned(5);
            currPlayer.removeMoney(field.getHouseHotelPrice());
        } else if (numOfHousesLeft > 0 && field.getHousesOwned() < 4) {
            numOfHousesLeft--;
            field.setHousesOwned(field.getHousesOwned() + 1);
            currPlayer.removeMoney(field.getHouseHotelPrice());
        }
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void sellHouse(BuyableField field) {
        if (field.getHousesOwned() == 5 && numOfHousesLeft >= 4) {
            numOfHousesLeft -= 4;
            numOfHotelsLeft++;
            field.setHousesOwned(4);
            currPlayer.addMoney(field.getHouseHotelPrice() / 2);
        } else if (field.getHousesOwned() > 0 && field.getHousesOwned() < 5) {
            numOfHousesLeft++;
            field.setHousesOwned(field.getHousesOwned() - 1);
            currPlayer.addMoney(field.getHouseHotelPrice() / 2);
        }
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void mortageField(BuyableField field) {
        field.setMortgage(true);
        currPlayer.addMoney(field.getPrice() / 2);
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void payOffMortage(BuyableField field) {
        field.setMortgage(false);
        currPlayer.removeMoney((int) (field.getPrice() / 2 * 1.1));
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void trade(Player p, List<BuyableField> offerFields, int offerMoney, List<BuyableField> receiveFields, int receiveMoney) {
        for (BuyableField field : offerFields) {
            currPlayer.removeOwned(field);
            field.setOwner(p);
            p.addOwned(field);
        }

        for (BuyableField field : receiveFields) {
            p.removeOwned(field);
            field.setOwner(currPlayer);
            currPlayer.addOwned(field);
        }

        currPlayer.removeMoney(offerMoney);
        currPlayer.addMoney(receiveMoney);
        p.removeMoney(receiveMoney);
        p.addMoney(offerMoney);
        fragment.updateCurrData(currPlayer);
        MainActivity.repository.update();
    }

    public void playerJailUpdate() {
        fragment.jailOptions();
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public void setCurrPlayerNum(int currPlayerNum) {
        this.currPlayerNum = currPlayerNum;
    }

    public void setAlreadyRolled(boolean alreadyRolled) {
        this.alreadyRolled = alreadyRolled;
    }
}
