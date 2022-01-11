package com.example.pmuprojekat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;

import com.example.pmuprojekat.databinding.ActivityMainBinding;
import com.example.pmuprojekat.fragments.gameFragment;
import com.example.pmuprojekat.fragments.historyFragment;
import com.example.pmuprojekat.fragments.optionsFragment;
import com.example.pmuprojekat.fragments.startFragment;
import com.example.pmuprojekat.monopoly.Fields.ChanceChestField;
import com.example.pmuprojekat.monopoly.Game;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "MAIN_FRAGMENT";
    public static final int START_FRAGMENT = 0;
    public static final int GAME_FRAGMENT = 1;
    public static final int OPTIONS_FRAGMENT = 2;
    public static final int HISTORY_FRAGMENT = 3;
    public static final Fragment[] FRAGMENTS = {new startFragment(),
            new gameFragment(),
            new optionsFragment(),
            new historyFragment()};

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainContentFrame, new startFragment(), FRAGMENT_TAG)
                    .commit();

        String[] cardNames = getResources().getStringArray(R.array.cardName);
        int[] cardCosts = getResources().getIntArray(R.array.cardCost);
        String[] cardTypes = getResources().getStringArray(R.array.cardType);
        String[] cardHousePrices = getResources().getStringArray(R.array.cardHousePrices);
        String[] cardRents = getResources().getStringArray(R.array.cardRent);
        Game.getInstance().initiateGame(cardNames, cardCosts, cardTypes, cardHousePrices, cardRents);

        String[] chanceCards = getResources().getStringArray(R.array.chanceCards);
        String[] chestCards = getResources().getStringArray(R.array.chestCards);

        ChanceChestField.initChanceChest(chanceCards, chestCards);

        setContentView(binding.getRoot());
    }

    public void switchFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContentFrame, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }
}