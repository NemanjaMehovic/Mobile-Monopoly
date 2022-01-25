package com.example.pmuprojekat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewbinding.ViewBinding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.pmuprojekat.database.GameRepository;
import com.example.pmuprojekat.databinding.ActivityMainBinding;
import com.example.pmuprojekat.fragments.gameFragment;
import com.example.pmuprojekat.fragments.historyFragment;
import com.example.pmuprojekat.fragments.optionsFragment;
import com.example.pmuprojekat.fragments.startFragment;
import com.example.pmuprojekat.monopoly.Fields.ChanceChestField;
import com.example.pmuprojekat.monopoly.Game;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_NAME = "PMU_PROJECT_PREFERENCES";
    private static final String SHARED_PREFERENCES_WAIT_TIME = "PMU_PROJECT_PREFERENCES_TIME";
    private static final String SHARED_PREFERENCES_THRESHOLD = "PMU_PROJECT_PREFERENCES_THRESHOLD";
    public static final String LOG_TAG = "PMU_PROJECT_LOG";
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
    public MutableLiveData<Integer> waitTimeBetweenShakesInSec;
    public MutableLiveData<Double> shakeThreshold;
    private SharedPreferences sharedPreferences;
    public static GameRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        repository = new GameRepository(this);
        resetGame();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainContentFrame, new startFragment(), FRAGMENT_TAG)
                    .commit();

        waitTimeBetweenShakesInSec = new MutableLiveData<>();
        shakeThreshold = new MutableLiveData<>();

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        waitTimeBetweenShakesInSec.setValue(sharedPreferences.getInt(SHARED_PREFERENCES_WAIT_TIME, 3));
        shakeThreshold.setValue((double) sharedPreferences.getFloat(SHARED_PREFERENCES_THRESHOLD, 1.1f));

        setContentView(binding.getRoot());
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContentFrame, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void resetGame() {
        String[] cardNames = getResources().getStringArray(R.array.cardName);
        int[] cardCosts = getResources().getIntArray(R.array.cardCost);
        String[] cardTypes = getResources().getStringArray(R.array.cardType);
        String[] cardHousePrices = getResources().getStringArray(R.array.cardHousePrices);
        String[] cardRents = getResources().getStringArray(R.array.cardRent);
        Game.getInstance().initiateGame(cardNames, cardCosts, cardTypes, cardHousePrices, cardRents);

        String[] chanceCards = getResources().getStringArray(R.array.chanceCards);
        String[] chestCards = getResources().getStringArray(R.array.chestCards);

        ChanceChestField.initChanceChest(chanceCards, chestCards);
    }

    public void update(int time, double shake) {
        waitTimeBetweenShakesInSec.setValue(time);
        shakeThreshold.setValue(shake);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREFERENCES_WAIT_TIME, time);
        editor.putFloat(SHARED_PREFERENCES_THRESHOLD, (float) shake);
        editor.commit();
    }
}