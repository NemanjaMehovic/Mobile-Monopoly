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
import java.util.List;

public class Game {

    private static Game instance;
    private List<Field> fields;

    public static Game getInstance()
    {
        if(instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
    }

    public void initiateGame(String[] cardNames, int[] cardCosts, String[] cardTypes, String[] cardHousePrices, String[] cardRents)
    {
        Field.reset();
        fields = new ArrayList<>();
        List<BuyableField> buyableFields = new ArrayList<>();
        List<PropertyBuyableField> propertyBuyableFields = new ArrayList<>();
        for(int i = 0; i < cardNames.length; i++)
        {
            String[] splitType = cardTypes[i].split("-");
            switch (splitType[0])
            {
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
                    switch (splitType[1])
                    {
                        case "House":
                            tmp = new PropertyBuyableField(cardNames[i], cardCosts[i], splitType[2]);
                            propertyBuyableFields.add((PropertyBuyableField) tmp);
                            break;
                        case "Railroad":
                            tmp = new RailroadBuyableField(cardNames[i], cardCosts[i]);
                            break;
                        case "Utility":
                            tmp = new UtilityBuyableField(cardNames[i], cardCosts[i]);
                            break;
                        default:
                            System.out.println("Error unknown type of buyable.");
                            break;
                    }
                    buyableFields.add(tmp);
                    fields.add(tmp);
                    break;
                default:
                    System.out.println("Error unknown type.");
                    break;
            }
        }
        for(Field f : fields)
            System.out.println(f);
        //TODO add house prices and rent prices
    }
}
