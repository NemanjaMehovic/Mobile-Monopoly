package com.example.pmuprojekat.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.example.pmuprojekat.dialog.infoDialog;
import com.example.pmuprojekat.dialog.newGameDialog;
import com.example.pmuprojekat.dialog.promptDialog;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Fields.Field;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

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


    public gameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        Game.getInstance().setFragment(this);

        stepCounts = new ArrayList<>();
        dirs = new ArrayList<>();
        listResetPairs = new ArrayList<>();

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
            listImageView.get(i).setVisibility(View.VISIBLE);

        binding.testButton.setOnClickListener(v -> {
            Game.getInstance().rollDice();
            Game.getInstance().nextPlayer();
        });

        return binding.getRoot();
    }

    public void updatePlayers()
    {
        for (int i = 0; i < Game.getInstance().getPlayers().size(); i++)
            if(Game.getInstance().getPlayers().get(i).isLost())
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
        if(rentPrices != null)
            for(int i = 0; i < rentPrices.size(); i++)
            {
                if((i >0) && i < (rentPrices.size()-1)) {
                    String format = String.format("%04d", rentPrices.get(i));
                    rent.append(i).append(":").append(format).append((i % 2 == 0) ? System.lineSeparator() : " ");
                }
                else if(i == (rentPrices.size()-1))
                    rent.append("Hotel ").append(rentPrices.get(i));
                else
                    rent.append(rentPrices.get(i)).append(System.lineSeparator()).append("With houses").append(System.lineSeparator());
            }
        binding.cardRentInfo.setText(rent.toString());
    }

    public void setPromptDialog(promptDialog.Callback accept, promptDialog.Callback cancel, String info)
    {
        promptDialog dialog = new promptDialog(accept, cancel, info);
        dialog.setCancelable(false);
        dialog.show(mainActivity.getSupportFragmentManager(), "Prompt");
    }

    public void setInfoDIalog(promptDialog.Callback accept, String info)
    {
        infoDialog dialog = new infoDialog(accept, info);
        dialog.setCancelable(false);
        dialog.show(mainActivity.getSupportFragmentManager(), "Info");
    }

    public void startAuction(BuyableField field)
    {
        auctionDialog dialog = new auctionDialog(mainActivity, field);
        dialog.setCancelable(false);
        dialog.show(mainActivity.getSupportFragmentManager(), "Auction");
    }

    public void setToast(String info)
    {
        Toast.makeText(mainActivity, info, Toast.LENGTH_SHORT).show();
    }

    public void jailOptions()
    {
        //TODO implement
    }
}