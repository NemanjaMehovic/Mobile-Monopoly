package com.example.pmuprojekat.monopoly.Fields;

import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChanceChestField extends Field {

    public enum ChanceChestType {
        CHANCE,
        CHEST
    }

    private static List<String> Chances;
    private static List<String> Chests;
    private ChanceChestType type;

    public ChanceChestField(String name, ChanceChestType type) {
        super(name);
        this.type = type;
    }

    @Override
    public void effect(Player p) {
        String card = "";
        boolean back = false;
        if (type == ChanceChestType.CHANCE) {
            card = Chances.get(0);
            Chances.remove(0);
        } else {
            card = Chests.get(0);
            Chests.remove(0);
        }
        String[] split = card.split("-");
        String showString = "";
        boolean returnCard = true;
        List<Field> fields = Game.getInstance().getFields();
        switch (split[0]) {
            case "Move":
                String fieldName = split[1];
                showString = split[2];
                for (Field f : fields)
                    if (f.getName().equals(fieldName)) {
                        p.setPosition(f.getId());
                        break;
                    }
                break;
            case "Nearest":
                String cardType = split[1];
                showString = split[2];
                boolean check = true;
                while (check) {
                    int pos = p.move(1);
                    for (BuyableField field : Game.getInstance().getBuyableFields())
                        if (pos == field.getId() && field.getType().equals(cardType)) {
                            check = false;
                            break;
                        }
                }
                break;
            case "Back":
                int numSteps = Integer.parseInt(split[1]);
                showString = split[2];
                p.setPosition(p.getPosition() - numSteps + ((p.getPosition() - numSteps) < 0 ? 40 : 0));
                back = true;
                break;
            case "JailFree":
                showString = split[1];
                returnCard = false;
                if (type == ChanceChestType.CHANCE)
                    p.setChanceJailFree(showString);
                else
                    p.setChestJailFree(showString);
                break;
            case "Jail":
                showString = split[1];
                p.setJailTime(3);
                for (Field f : fields)
                    if (f.getName().equals("Jail")) {
                        p.setPosition(f.getId());
                        break;
                    }
                break;
            case "Repair":
                int houseCost = Integer.parseInt(split[1]);
                int hotelCost = Integer.parseInt(split[2]);
                showString = split[3];
                int numOfHouses = p.getNumOfHouses();
                int numOfHotels = p.getNumOfHotels();
                if (!p.removeMoney(numOfHouses * houseCost + numOfHotels * hotelCost))
                    Game.getInstance().notEnoughMoney(p, numOfHouses * houseCost + numOfHotels * hotelCost);
                break;
            case "Tax":
                int amountTax = Integer.parseInt(split[1]);
                showString = split[2];
                if (!p.removeMoney(amountTax))
                    Game.getInstance().notEnoughMoney(p, amountTax);
                break;
            case "Get":
                int amountGet = Integer.parseInt(split[1]);
                showString = split[2];
                p.addMoney(amountGet);
                break;
            case "Pay":
                int amountPay = Integer.parseInt(split[1]);
                int sumPay = 0;
                showString = split[2];
                for (Player p1 : Game.getInstance().getPlayers())
                    if (p1 != p) {
                        p1.addMoney(amountPay);
                        sumPay += amountPay;
                    }
                if (!p.removeMoney(sumPay))
                    Game.getInstance().notEnoughMoney(p, sumPay);
                break;
            case "Collect":
                int amountCollect = Integer.parseInt(split[1]);
                int sumCollect = 0;
                showString = split[2];
                for (Player p1 : Game.getInstance().getPlayers())
                    if (p1 != p) {
                        if (!p1.removeMoney(amountCollect))
                            Game.getInstance().notEnoughMoney(p1, amountCollect);
                        sumCollect += amountCollect;
                    }
                p.addMoney(sumCollect);
                break;
        }
        if (returnCard) {
            if (type == ChanceChestType.CHANCE)
                Chances.add(card);
            else
                Chests.add(card);
        }
        Game.getInstance().chanceChestCardGotten(showString, back);
    }

    public static void initChanceChest(String[] chanceCards, String[] chestCards) {
        Chances = new ArrayList<>();
        Chests = new ArrayList<>();
        Collections.addAll(Chances, chanceCards);
        Collections.addAll(Chests, chestCards);
        Collections.shuffle(Chances);
        Collections.shuffle(Chests);
    }

    public static void putBackChance(String s) {
        Chances.add(s);
    }

    public static void putBackChest(String s) {
        Chests.add(s);
    }

    public ChanceChestType getType() {
        return type;
    }

    public static List<String> getChances() {
        return Chances;
    }

    public static List<String> getChests() {
        return Chests;
    }
}
