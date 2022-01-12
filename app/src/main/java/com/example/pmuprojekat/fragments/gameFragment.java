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

import com.example.pmuprojekat.R;
import com.example.pmuprojekat.databinding.FragmentGameBinding;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.ArrayList;
import java.util.List;


public class gameFragment extends Fragment {

    private enum MoveDir{
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    private FragmentGameBinding binding;
    private static final double BIG_ZONE_WIDTH = 200;
    private static final double SMALL_ZONE_WIDTH = 123;
    private int stepCount = 0;
    private MoveDir dir = MoveDir.LEFT;


    public gameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        stepCount = 0;
        dir = MoveDir.LEFT;
        List<ImageView> list = new ArrayList<>();
        list.add(binding.player1Picture);
        list.add(binding.player2Picture);
        list.add(binding.player3Picture);
        list.add(binding.player4Picture);
        list.add(binding.player5Picture);
        list.add(binding.player6Picture);
        list.add(binding.player7Picture);
        list.add(binding.player8Picture);
        for(int i = 0; i < Game.getInstance().getPlayers().size(); i++)
            list.get(i).setVisibility(View.VISIBLE);
        binding.testButton.setOnClickListener(v -> {
            movePlayer();
        });

        return binding.getRoot();
    }

    public void movePlayer()
    {
        int width = binding.board.getWidth();
        int height = binding.board.getHeight();
        int pictureWidth = binding.player1Picture.getWidth();
        int pictureHeight = binding.player1Picture.getHeight();

        BitmapDrawable b = (BitmapDrawable) this.getResources().getDrawable(R.drawable.monopoly_board);
        double scaleW = (double) width / b.getBitmap().getWidth();
        double scaleH = (double) height / b.getBitmap().getHeight();

        RelativeLayout.LayoutParams marginParams = (RelativeLayout.LayoutParams) binding.player1Layout.getLayoutParams();
        switch (dir)
        {
            case LEFT:
                if (stepCount == 0)
                    marginParams.rightMargin += BIG_ZONE_WIDTH * scaleW;
                else
                    marginParams.rightMargin += SMALL_ZONE_WIDTH * scaleW;
                if(stepCount%2 == 0)
                    marginParams.rightMargin += 1;
                break;
            case UP:
                if (stepCount == 0)
                    marginParams.bottomMargin += BIG_ZONE_WIDTH * scaleH;
                else
                    marginParams.bottomMargin += SMALL_ZONE_WIDTH * scaleH;
                if(stepCount%2 == 0)
                    marginParams.bottomMargin += 1;
                break;
            case RIGHT:
                if (stepCount == 0)
                    marginParams.rightMargin -= BIG_ZONE_WIDTH * scaleW;
                else
                    marginParams.rightMargin -= SMALL_ZONE_WIDTH * scaleW;
                if(stepCount%2 == 0)
                    marginParams.rightMargin += 1;
                break;
            case DOWN:
                if (stepCount == 0)
                    marginParams.bottomMargin -= BIG_ZONE_WIDTH * scaleH;
                else
                    marginParams.bottomMargin -= SMALL_ZONE_WIDTH * scaleH;
                if(stepCount%2 == 0)
                    marginParams.bottomMargin += 1;
                break;
        }

        stepCount++;
        if(stepCount >= 10)
        {
            stepCount = 0;
            switch (dir)
            {
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
        binding.player1Layout.setLayoutParams(marginParams);
    }
}