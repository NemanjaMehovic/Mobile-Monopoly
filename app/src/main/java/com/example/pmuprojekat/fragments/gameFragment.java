package com.example.pmuprojekat.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.R;
import com.example.pmuprojekat.databinding.FragmentGameBinding;
import com.example.pmuprojekat.dialog.auctionDialog;
import com.example.pmuprojekat.dialog.fieldInfoDialog;
import com.example.pmuprojekat.dialog.infoDialog;
import com.example.pmuprojekat.dialog.jailDialog;
import com.example.pmuprojekat.dialog.mortageDialog;
import com.example.pmuprojekat.dialog.optionsDialog;
import com.example.pmuprojekat.dialog.promptDialog;
import com.example.pmuprojekat.dialog.tradeDialog;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Fields.Field;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;
import com.example.pmuprojekat.shake.LifeCycleGameObserver;

import java.util.ArrayList;
import java.util.List;


public class gameFragment extends Fragment {

    private enum MoveDir {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    private class PairReset {
        public boolean resetWidth;
        public boolean resetHeight;

        public PairReset(boolean resetWidth, boolean resetHeight) {
            this.resetWidth = resetWidth;
            this.resetHeight = resetHeight;
        }
    }

    private static final double BIG_ZONE_WIDTH = 200;
    private static final double SMALL_ZONE_WIDTH = 123;

    public MainActivity mainActivity;

    private FragmentGameBinding binding;
    private List<Integer> stepCounts;
    private List<MoveDir> dirs;
    private List<ImageView> listImageView;
    private List<RelativeLayout> listRelativeLayouts;
    private List<PairReset> listResetPairs;
    private LifeCycleGameObserver gameObserver;

    public DialogFragment dialog;
    public static boolean buyDialogWasActive;
    public static boolean auctionDialogWasActive;

    public gameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
        gameObserver = new LifeCycleGameObserver(mainActivity, this);
        getLifecycle().addObserver(gameObserver);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        dialog = null;
        buyDialogWasActive = false;
        auctionDialogWasActive = false;
        Game.getInstance().setFragment(this);


        stepCounts = new ArrayList<>();
        dirs = new ArrayList<>();
        listResetPairs = new ArrayList<>();

        if (Game.getInstance().getPlayers().size() == 0)
            MainActivity.repository.loadGameFromDataBase();


        for (int i = 0; i < Game.getInstance().getPlayers().size(); i++) {
            stepCounts.add(0);
            dirs.add(MoveDir.LEFT);
            listResetPairs.add(new PairReset((i % 2) != 0, i >= 2));
        }

        listImageView = new ArrayList<>();
        listImageView.add(binding.player1Picture);
        listImageView.add(binding.player2Picture);
        listImageView.add(binding.player3Picture);
        listImageView.add(binding.player4Picture);
        listImageView.add(binding.player5Picture);
        listImageView.add(binding.player6Picture);
        listImageView.add(binding.player7Picture);
        listImageView.add(binding.player8Picture);

        listRelativeLayouts = new ArrayList<>();
        listRelativeLayouts.add(binding.player1Layout);
        listRelativeLayouts.add(binding.player2Layout);
        listRelativeLayouts.add(binding.player3Layout);
        listRelativeLayouts.add(binding.player4Layout);
        listRelativeLayouts.add(binding.player5Layout);
        listRelativeLayouts.add(binding.player6Layout);
        listRelativeLayouts.add(binding.player7Layout);
        listRelativeLayouts.add(binding.player8Layout);

        for (int i = 0; i < Game.getInstance().getPlayers().size(); i++)
            if (!Game.getInstance().getPlayers().get(i).isLost())
                listImageView.get(i).setVisibility(View.VISIBLE);

        setOnClickListeners();

        binding.getRoot().post(() -> {
            gameLoadPositions();
        });

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        Player p = Game.getInstance().getCurrPlayer();
        if(buyDialogWasActive){
            BuyableField f = (BuyableField) Game.getInstance().getFields().get(p.getPosition());
            Game.getInstance().offerToBuy(p, f);
        }
        else if(auctionDialogWasActive){
            BuyableField f = (BuyableField) Game.getInstance().getFields().get(p.getPosition());
            startAuction(f);
        }

    }

    private void gameLoadPositions() {
        Game game = Game.getInstance();
        Player trueCurrPlayer = game.getCurrPlayer();
        int trueCurrPlayerNumber = game.getCurrPlayerNum();
        List<Player> players = game.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (p.isLost())
                continue;
            game.setCurrPlayer(p);
            game.setCurrPlayerNum(i);
            movePlayer(p.getPosition(), 0);
        }

        game.setCurrPlayer(trueCurrPlayer);
        game.setCurrPlayerNum(trueCurrPlayerNumber);

        jailOptions();
        updateCurrData(trueCurrPlayer);

        if (game.getFields().get(trueCurrPlayer.getPosition()) instanceof BuyableField) {
            BuyableField field = (BuyableField) game.getFields().get(trueCurrPlayer.getPosition());
            if (field.getOwner() == null)
                game.offerToBuy(trueCurrPlayer, field);
        }
    }

    private void setOnClickListeners() {
        binding.rollButton.setOnClickListener(v -> {
            binding.rollButton.setEnabled(false);
            binding.infoButton.setEnabled(false);
            binding.mortgageButton.setEnabled(false);
            binding.payOffMortgage.setEnabled(false);
            binding.buyHouseButton.setEnabled(false);
            binding.sellHouseButton.setEnabled(false);
            binding.tradeButtond.setEnabled(false);
            gameObserver.startRoll();
        });

        binding.nextPlayerButton.setOnClickListener(v -> {
            binding.nextPlayerButton.setEnabled(false);
            Game.getInstance().nextPlayer();
        });

        binding.jailOptionsButton.setOnClickListener(v -> {
            dialog = new jailDialog();
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "JailOptions");
        });

        binding.infoButton.setOnClickListener(v -> {
            dialog = new fieldInfoDialog(mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "FieldInfo");
        });

        binding.mortgageButton.setOnClickListener(v -> {
            List<BuyableField> list = Game.getInstance().getCurrPlayer().getAllFieldsThatCanBeMortgaged();
            if (list.size() == 0) {
                setToast("You have nothing that can be mortgaged");
                return;
            }
            dialog = new mortageDialog(list, mortageDialog.dialogType.MORTAGE, mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "MortgageDialog");
        });

        binding.payOffMortgage.setOnClickListener(v -> {
            List<BuyableField> list = Game.getInstance().getCurrPlayer().getAllMortgagedFields();
            if (list.size() == 0) {
                setToast("You have nothing that is mortgaged");
                return;
            }
            dialog = new mortageDialog(list, mortageDialog.dialogType.MORTAGE_PAY, mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "MortgagePayDialog");
        });

        binding.buyHouseButton.setOnClickListener(v -> {
            List<BuyableField> list = Game.getInstance().getCurrPlayer().getAllFieldsThatCanHaveAProperty();
            if (list.size() == 0) {
                setToast("You have nothing that can buy a house/hotel");
                return;
            }
            dialog = new mortageDialog(list, mortageDialog.dialogType.HOUSE_BUY, mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "HouseBuyDialog");
        });

        binding.sellHouseButton.setOnClickListener(v -> {
            List<BuyableField> list = Game.getInstance().getCurrPlayer().getAllFieldsWithProperty();
            if (list.size() == 0) {
                setToast("You have no houses/hotels to sell");
                return;
            }
            dialog = new mortageDialog(list, mortageDialog.dialogType.HOUSE_SELL, mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "HouseSellDialog");
        });

        binding.tradeButtond.setOnClickListener(v -> {
            dialog = new tradeDialog(mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "TradeDialog");

        });

        binding.infoOptiobsButton.setOnClickListener(v -> {
            dialog = new optionsDialog(mainActivity);
            dialog.setCancelable(false);
            dialog.show(mainActivity.getSupportFragmentManager(), "OptionsDialog");
        });
    }

    public void rolled() {
        binding.nextPlayerButton.setEnabled(true);
        binding.infoButton.setEnabled(true);
        binding.mortgageButton.setEnabled(true);
        binding.payOffMortgage.setEnabled(true);
        binding.buyHouseButton.setEnabled(true);
        binding.sellHouseButton.setEnabled(true);
        binding.tradeButtond.setEnabled(true);
        Game.getInstance().rollDice();
    }

    public void updatePlayers() {
        for (int i = 0; i < Game.getInstance().getPlayers().size(); i++)
            if (Game.getInstance().getPlayers().get(i).isLost())
                listImageView.get(i).setVisibility(View.GONE);
    }

    public void movePlayer(int numOfTimes, int prevPosition) {
        int currPlayerNum = Game.getInstance().getCurrPlayerNum();

        Player currPlayer = Game.getInstance().getCurrPlayer();
        int width = binding.board.getWidth();
        int height = binding.board.getHeight();
        int pictureWidth = listImageView.get(currPlayerNum).getWidth();
        int pictureHeight = listImageView.get(currPlayerNum).getHeight();

        BitmapDrawable b = (BitmapDrawable) this.getResources().getDrawable(R.drawable.monopoly_board);
        double scaleW = (double) width / b.getBitmap().getWidth();
        double scaleH = (double) height / b.getBitmap().getHeight();

        RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) listRelativeLayouts.get(currPlayerNum).getLayoutParams();
        MoveDir dir = dirs.get(currPlayerNum);
        int stepCount = stepCounts.get(currPlayerNum);

        updateReset(currPlayerNum, marginParams);

        for (int i = 0; i < numOfTimes; i++) {
            switch (dir) {
                case LEFT:
                    if (stepCount == 0)
                        marginParams.rightMargin += BIG_ZONE_WIDTH * scaleW;
                    else
                        marginParams.rightMargin += SMALL_ZONE_WIDTH * scaleW;
                    if (stepCount % 2 == 0)
                        marginParams.rightMargin += 1;
                    break;
                case UP:
                    if (stepCount == 0)
                        marginParams.bottomMargin += BIG_ZONE_WIDTH * scaleH;
                    else
                        marginParams.bottomMargin += SMALL_ZONE_WIDTH * scaleH;
                    if (stepCount % 2 == 0)
                        marginParams.bottomMargin += 1;
                    break;
                case RIGHT:
                    if (stepCount == 0)
                        marginParams.rightMargin -= BIG_ZONE_WIDTH * scaleW;
                    else
                        marginParams.rightMargin -= SMALL_ZONE_WIDTH * scaleW;
                    if (stepCount % 2 == 0)
                        marginParams.rightMargin += 1;
                    break;
                case DOWN:
                    if (stepCount == 0)
                        marginParams.bottomMargin -= BIG_ZONE_WIDTH * scaleH;
                    else
                        marginParams.bottomMargin -= SMALL_ZONE_WIDTH * scaleH;
                    if (stepCount % 2 == 0)
                        marginParams.bottomMargin += 1;
                    break;
            }

            stepCount++;
            if (stepCount >= 10) {
                stepCount = 0;
                switch (dir) {
                    case LEFT:
                        dir = MoveDir.UP;
                        marginParams.bottomMargin = 0;
                        marginParams.rightMargin = width - pictureWidth;
                        break;
                    case UP:
                        dir = MoveDir.RIGHT;
                        marginParams.bottomMargin = height - pictureHeight;
                        marginParams.rightMargin = width - pictureWidth;
                        break;
                    case RIGHT:
                        dir = MoveDir.DOWN;
                        marginParams.bottomMargin = height - pictureHeight;
                        marginParams.rightMargin = 0;
                        break;
                    case DOWN:
                        dir = MoveDir.LEFT;
                        marginParams.bottomMargin = 0;
                        marginParams.rightMargin = 0;
                        break;
                }
            }
        }

        dirs.set(currPlayerNum, dir);
        stepCounts.set(currPlayerNum, stepCount);
        listRelativeLayouts.get(currPlayerNum).setLayoutParams(marginParams);

        updateOnField(prevPosition);
        updateOnField(currPlayer.getPosition());


    }

    private void updateOnField(int id) {
        int numOfPlayerOnSameField = 0;
        for (int currPlayerNum = 0; currPlayerNum < Game.getInstance().getPlayers().size(); currPlayerNum++) {
            Player p = Game.getInstance().getPlayers().get(currPlayerNum);
            if (p.getPosition() != id)
                continue;

            int pictureWidth = listImageView.get(currPlayerNum).getWidth();
            int pictureHeight = listImageView.get(currPlayerNum).getHeight();
            RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) listRelativeLayouts.get(currPlayerNum).getLayoutParams();
            MoveDir dir = dirs.get(currPlayerNum);
            PairReset currReset = listResetPairs.get(currPlayerNum);

            updateReset(currPlayerNum, marginParams);

            switch (dir) {
                case LEFT:
                    currReset.resetHeight = numOfPlayerOnSameField >= 2;
                    currReset.resetWidth = (numOfPlayerOnSameField % 2) != 0;
                    if (currReset.resetWidth)
                        marginParams.rightMargin += pictureWidth;
                    if (currReset.resetHeight)
                        marginParams.bottomMargin += (numOfPlayerOnSameField / 2) * pictureHeight;
                    break;
                case UP:
                    currReset.resetWidth = numOfPlayerOnSameField >= 2;
                    currReset.resetHeight = (numOfPlayerOnSameField % 2) != 0;
                    if (currReset.resetWidth)
                        marginParams.rightMargin -= (numOfPlayerOnSameField / 2) * pictureWidth;
                    if (currReset.resetHeight)
                        marginParams.bottomMargin += pictureHeight;
                    break;
                case RIGHT:
                    currReset.resetHeight = numOfPlayerOnSameField >= 2;
                    currReset.resetWidth = (numOfPlayerOnSameField % 2) != 0;
                    if (currReset.resetWidth)
                        marginParams.rightMargin -= pictureWidth;
                    if (currReset.resetHeight)
                        marginParams.bottomMargin -= (numOfPlayerOnSameField / 2) * pictureHeight;
                    break;
                case DOWN:
                    currReset.resetWidth = numOfPlayerOnSameField >= 2;
                    currReset.resetHeight = (numOfPlayerOnSameField % 2) != 0;
                    if (currReset.resetWidth)
                        marginParams.rightMargin += (numOfPlayerOnSameField / 2) * pictureWidth;
                    if (currReset.resetHeight)
                        marginParams.bottomMargin -= pictureHeight;
                    break;
            }

            numOfPlayerOnSameField++;
            listRelativeLayouts.get(currPlayerNum).setLayoutParams(marginParams);
        }
    }

    public void updateReset(int currPlayerNum, RelativeLayout.LayoutParams marginParams) {
        int width = binding.board.getWidth();
        int height = binding.board.getHeight();

        int pictureWidth = listImageView.get(currPlayerNum).getWidth();
        int pictureHeight = listImageView.get(currPlayerNum).getHeight();
        MoveDir dir = dirs.get(currPlayerNum);
        PairReset currReset = listResetPairs.get(currPlayerNum);

        switch (dir) {
            case LEFT:
                if (currReset.resetWidth)
                    marginParams.rightMargin -= pictureWidth;
                if (currReset.resetHeight)
                    marginParams.bottomMargin = 0;
                break;
            case UP:
                if (currReset.resetWidth)
                    marginParams.rightMargin = width - pictureWidth;
                if (currReset.resetHeight)
                    marginParams.bottomMargin -= pictureHeight;
                break;
            case RIGHT:
                if (currReset.resetWidth)
                    marginParams.rightMargin += pictureWidth;
                if (currReset.resetHeight)
                    marginParams.bottomMargin = height - pictureHeight;
                break;
            case DOWN:
                if (currReset.resetWidth)
                    marginParams.rightMargin = 0;
                if (currReset.resetHeight)
                    marginParams.bottomMargin += pictureHeight;
                break;
        }

        currReset.resetWidth = false;
        currReset.resetHeight = false;
    }

    public void numberRolled(int dice1, int dice2, int prevPosition) {
        binding.numbRolledInfo.setText("Rolled: " + dice1 + " and " + dice2);
        movePlayer(dice1 + dice2, prevPosition);
    }

    public void updateCurrData(Player currPlayer) {
        binding.playerNameInfo.setText("Player name: " + currPlayer.getPlayerName());
        binding.playerMoneyInfo.setText("Money: " + currPlayer.getCurrMoney());
        Field currField = Game.getInstance().getFields().get(currPlayer.getPosition());
        List<Integer> rentPrices = null;

        String cardName = "Location: " + currField.getName() + System.lineSeparator();
        String cardPrice = "Price: ";
        String cardOwner = "Owner: ";
        String houseprice = "House price: ";
        String numOfHouses = "Houses owned: ";
        String hotelOwned = "Hotel owned: ";

        String tmpPrice = "None";
        String tmpOwner = "None";
        String tmpHousePrice = "None";
        String tmpHouses = "0";
        String tmpHotel = "No";

        if (currField instanceof BuyableField) {
            BuyableField field = (BuyableField) currField;
            if (field.getOwner() != null) {
                tmpOwner = field.getOwner().getPlayerName();
                int tmp = field.getHousesOwned();
                tmpHouses = "" + (tmp % 5);
                tmpHotel = tmp == 5 ? "Yes" : tmpHotel;
            }
            tmpPrice = "" + field.getPrice();
            tmpHousePrice = "" + field.getHouseHotelPrice();
            rentPrices = field.getRentPrices();
        }
        cardPrice += tmpPrice + System.lineSeparator();
        cardOwner += tmpOwner + System.lineSeparator();
        houseprice += tmpHousePrice + System.lineSeparator();
        numOfHouses += tmpHouses + System.lineSeparator();
        hotelOwned += tmpHotel;
        String cardInfo = cardName + cardPrice + cardOwner + houseprice + numOfHouses + hotelOwned;
        binding.cardNameInfo.setText(cardInfo);


        StringBuilder rent = new StringBuilder("Rent ");
        if (rentPrices != null) {
            if (rentPrices.size() > 5)
                for (int i = 0; i < rentPrices.size(); i++) {
                    if ((i > 0) && i < (rentPrices.size() - 1)) {
                        String format = String.format("%04d", rentPrices.get(i));
                        rent.append(i).append(":").append(format).append((i % 2 == 0) ? System.lineSeparator() : " ");
                    } else if (i == (rentPrices.size() - 1))
                        rent.append("Hotel ").append(rentPrices.get(i));
                    else
                        rent.append(rentPrices.get(i)).append(System.lineSeparator()).append("With houses").append(System.lineSeparator());
                }
            else
                for (int i = 0; i < rentPrices.size(); i++) {
                    rent.append(rentPrices.get(i));
                    if (i != (rentPrices.size() - 1))
                        rent.append(", ");
                }
        }
        binding.cardRentInfo.setText(rent.toString());
    }

    public void setPromptDialog(promptDialog.Callback accept, promptDialog.Callback cancel, String info) {
        dialog = new promptDialog(accept, cancel, info);
        dialog.setCancelable(false);
        dialog.show(mainActivity.getSupportFragmentManager(), "Prompt");
    }

    public void setInfoDIalog(promptDialog.Callback accept, String info) {
        dialog = new infoDialog(accept, info);
        dialog.setCancelable(false);
        dialog.show(mainActivity.getSupportFragmentManager(), "Info");
    }

    public void startAuction(BuyableField field) {
        dialog = new auctionDialog(mainActivity, field);
        dialog.setCancelable(false);
        dialog.show(mainActivity.getSupportFragmentManager(), "Auction");
    }

    public void setToast(String info) {
        Toast.makeText(mainActivity, info, Toast.LENGTH_SHORT).show();
    }

    public void jailOptions() {
        Player currPlayer = Game.getInstance().getCurrPlayer();
        binding.jailOptionsButton.setEnabled(currPlayer.getJailTime() > 0);
        binding.nextPlayerButton.setEnabled(currPlayer.getJailTime() > 0 || Game.getInstance().isAlreadyRolled());
        binding.rollButton.setEnabled(!(currPlayer.getJailTime() > 0) && !Game.getInstance().isAlreadyRolled());
    }

    public void finishedGame() {
        Log.d(MainActivity.LOG_TAG, "Num on back stack before pop " + mainActivity.getSupportFragmentManager().getBackStackEntryCount());
        mainActivity.getSupportFragmentManager().popBackStackImmediate();
        Log.d(MainActivity.LOG_TAG, "Num on back stack after pop " + mainActivity.getSupportFragmentManager().getBackStackEntryCount());
    }
}