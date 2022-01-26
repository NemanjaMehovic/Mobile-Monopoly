package com.example.pmuprojekat.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.R;
import com.example.pmuprojekat.database.ActionEntity;
import com.example.pmuprojekat.database.GameEntity;
import com.example.pmuprojekat.database.PlayerEntity;
import com.example.pmuprojekat.databinding.FragmentHistoryReplayBinding;
import com.example.pmuprojekat.dialog.infoDialog;
import com.example.pmuprojekat.replay.LifeCycleReplayObserver;

import java.util.ArrayList;
import java.util.List;


public class historyReplayFragment extends Fragment {


    private static final double BIG_ZONE_WIDTH = 200;
    private static final double SMALL_ZONE_WIDTH = 123;

    private FragmentHistoryReplayBinding binding;
    private MainActivity mainActivity;
    private GameEntity gameEntity;
    private List<PlayerEntity> players;
    private List<ActionEntity> actions;
    private boolean appWasClosed;
    private boolean playButton;
    private LifeCycleReplayObserver replayObserver;


    private List<Integer> stepCounts;
    private List<Integer> positions;
    private List<Integer> prevPositions;
    private List<gameFragment.MoveDir> dirs;
    private List<ImageView> listImageView;
    private List<RelativeLayout> listRelativeLayouts;
    private List<gameFragment.PairReset> listResetPairs;
    private int currPlayerNum;
    private int numOfTimes;

    public historyReplayFragment() {
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
        actions = MainActivity.repository.getAllActionsFromGame(gameEntity);
        players = MainActivity.repository.getALlPlayersFromGame(gameEntity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
        appWasClosed = savedInstanceState != null;
        playButton = true;
        replayObserver = new LifeCycleReplayObserver(this, actions);
        getLifecycle().addObserver(replayObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryReplayBinding.inflate(inflater, container, false);

        if (appWasClosed) {
            binding.getRoot().post(() -> {
                mainActivity.getSupportFragmentManager().popBackStack();
            });
            return binding.getRoot();
        }

        stepCounts = new ArrayList<>();
        dirs = new ArrayList<>();
        listResetPairs = new ArrayList<>();

        listImageView = new ArrayList<>();
        listImageView.add(binding.player1PictureReplay);
        listImageView.add(binding.player2PictureReplay);
        listImageView.add(binding.player3PictureReplay);
        listImageView.add(binding.player4PictureReplay);
        listImageView.add(binding.player5PictureReplay);
        listImageView.add(binding.player6PictureReplay);
        listImageView.add(binding.player7PictureReplay);
        listImageView.add(binding.player8PictureReplay);

        listRelativeLayouts = new ArrayList<>();
        listRelativeLayouts.add(binding.player1LayoutReplay);
        listRelativeLayouts.add(binding.player2LayoutReplay);
        listRelativeLayouts.add(binding.player3LayoutReplay);
        listRelativeLayouts.add(binding.player4LayoutReplay);
        listRelativeLayouts.add(binding.player5LayoutReplay);
        listRelativeLayouts.add(binding.player6LayoutReplay);
        listRelativeLayouts.add(binding.player7LayoutReplay);
        listRelativeLayouts.add(binding.player8LayoutReplay);

        positions = new ArrayList<>();
        prevPositions = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            listImageView.get(i).setVisibility(View.VISIBLE);
            positions.add(0);
            prevPositions.add(0);
            stepCounts.add(0);
            dirs.add(gameFragment.MoveDir.LEFT);
            listResetPairs.add(new gameFragment.PairReset((i % 2) != 0, i >= 2));
        }

        currPlayerNum = -1;
        numOfTimes = -1;

        binding.playReplayButtonGame.setOnClickListener(v -> {
            if (playButton) {
                setPauseButton();
                replayObserver.play();
            } else {
                setPlayButton();
                replayObserver.pause();
            }
        });

        return binding.getRoot();
    }

    public void movePlayer() {

        int width = binding.boardReplay.getWidth();
        int height = binding.boardReplay.getHeight();
        int pictureWidth = listImageView.get(currPlayerNum).getWidth();
        int pictureHeight = listImageView.get(currPlayerNum).getHeight();

        BitmapDrawable b = (BitmapDrawable) this.getResources().getDrawable(R.drawable.monopoly_board);
        double scaleW = (double) width / b.getBitmap().getWidth();
        double scaleH = (double) height / b.getBitmap().getHeight();

        RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) listRelativeLayouts.get(currPlayerNum).getLayoutParams();
        gameFragment.MoveDir dir = dirs.get(currPlayerNum);
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
                        dir = gameFragment.MoveDir.UP;
                        marginParams.bottomMargin = 0;
                        marginParams.rightMargin = width - pictureWidth;
                        break;
                    case UP:
                        dir = gameFragment.MoveDir.RIGHT;
                        marginParams.bottomMargin = height - pictureHeight;
                        marginParams.rightMargin = width - pictureWidth;
                        break;
                    case RIGHT:
                        dir = gameFragment.MoveDir.DOWN;
                        marginParams.bottomMargin = height - pictureHeight;
                        marginParams.rightMargin = 0;
                        break;
                    case DOWN:
                        dir = gameFragment.MoveDir.LEFT;
                        marginParams.bottomMargin = 0;
                        marginParams.rightMargin = 0;
                        break;
                }
            }
        }

        dirs.set(currPlayerNum, dir);
        stepCounts.set(currPlayerNum, stepCount);
        listRelativeLayouts.get(currPlayerNum).setLayoutParams(marginParams);

        updateOnField(prevPositions.get(currPlayerNum));
        updateOnField(positions.get(currPlayerNum));

    }

    private void updateOnField(int id) {
        int numOfPlayerOnSameField = 0;
        for (int currPlayerNum = 0; currPlayerNum < players.size(); currPlayerNum++) {
            int p = positions.get(currPlayerNum);
            if (p != id)
                continue;

            int pictureWidth = listImageView.get(currPlayerNum).getWidth();
            int pictureHeight = listImageView.get(currPlayerNum).getHeight();
            RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) listRelativeLayouts.get(currPlayerNum).getLayoutParams();
            gameFragment.MoveDir dir = dirs.get(currPlayerNum);
            gameFragment.PairReset currReset = listResetPairs.get(currPlayerNum);

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
        int width = binding.boardReplay.getWidth();
        int height = binding.boardReplay.getHeight();

        int pictureWidth = listImageView.get(currPlayerNum).getWidth();
        int pictureHeight = listImageView.get(currPlayerNum).getHeight();
        gameFragment.MoveDir dir = dirs.get(currPlayerNum);
        gameFragment.PairReset currReset = listResetPairs.get(currPlayerNum);

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

    public void setCurrPlayerNumAndNewPosition(int newCurrPlayerNum, int newPosition) {
        currPlayerNum = newCurrPlayerNum;
        prevPositions.set(currPlayerNum, positions.get(currPlayerNum));
        positions.set(currPlayerNum, newPosition);
        numOfTimes = 0;
        int currPosition = prevPositions.get(currPlayerNum);
        while (currPosition != newPosition) {
            numOfTimes++;
            currPosition = (currPosition + 1) % 40;
        }
    }

    public void wentBankrupt(int id) {
        listImageView.get(id).setVisibility(View.GONE);
        String data = "Player " + players.get(id).getName() + " went bankrupt";
        addText(data);
    }

    public void winner(String name){
        addText("Replay over winner is " + name);
        binding.playReplayButtonGame.setEnabled(false);
    }

    public void setText(String data) {
        binding.replayInfo.setText(data);
    }

    public void addText(String data) {
        StringBuilder builder = new StringBuilder(data);
        if(!binding.replayInfo.getText().toString().equals(""))
            builder.append(System.lineSeparator()).append(binding.replayInfo.getText().toString());
        binding.replayInfo.setText(builder.toString());
    }

    public void setPauseButton() {
        playButton = false;
        binding.playReplayButtonGame.setText("Pause");
    }

    public void setPlayButton() {
        playButton = true;
        binding.playReplayButtonGame.setText("Play");
    }
}