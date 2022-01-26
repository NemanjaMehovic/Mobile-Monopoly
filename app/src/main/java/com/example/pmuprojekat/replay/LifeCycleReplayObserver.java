package com.example.pmuprojekat.replay;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.pmuprojekat.database.ActionEntity;
import com.example.pmuprojekat.fragments.historyReplayFragment;

import java.util.List;

public class LifeCycleReplayObserver implements DefaultLifecycleObserver {

    private historyReplayFragment fragment;
    private ReplayThread thread;
    private List<ActionEntity> actions;
    private int lastAction;

    public LifeCycleReplayObserver(historyReplayFragment fragment, List<ActionEntity> actions) {
        this.fragment = fragment;
        this.actions = actions;
        lastAction = -1;
        thread = null;
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        thread = new ReplayThread(this);
        thread.start();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        thread.stopThread();
        thread = null;
        fragment.setPlayButton();
    }

    public void play() {
        if (thread != null)
            thread.play();
    }

    public void pause() {
        if (thread != null)
            thread.pause();
    }

    public synchronized ActionEntity getNextAction() {
        lastAction += (lastAction + 1) < actions.size() ? 1 : 0;
        return actions.get(lastAction);
    }

    public void setText(String data) {
        fragment.addText(data);
    }

    public void move(String newCurrPlayer, String newPosition){
        int currPlayer = Integer.parseInt(newCurrPlayer);
        int position = Integer.parseInt(newPosition);
        fragment.setCurrPlayerNumAndNewPosition(currPlayer, position);
        fragment.movePlayer();
    }

    public void winner(String name){
        fragment.winner(name);
    }

    public void bankrupt(String numPlayer){
        int num = Integer.parseInt(numPlayer);
        fragment.wentBankrupt(num);
    }
}
