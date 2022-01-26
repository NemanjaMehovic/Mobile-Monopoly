package com.example.pmuprojekat.replay;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.database.ActionEntity;

public class ReplayThread extends Thread {

    private static final int TIME_BETWEEN_ACTIONS = 5000;

    private boolean sleep;
    private boolean running;
    private boolean pause;
    private LifeCycleReplayObserver replayObserver;

    public ReplayThread(LifeCycleReplayObserver replayObserver) {
        this.replayObserver = replayObserver;
        this.pause = true;
        this.sleep = false;
        this.running = true;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                while (running) {
                    while (pause && running) {
                        wait();
                    }
                    if(running){
                        work();
                        if(sleep)
                            wait(TIME_BETWEEN_ACTIONS);
                    }
                }
            } catch (Exception e) {
                Log.d(MainActivity.LOG_TAG, "Thread error");
            }
        }
    }


    private void work(){
        sleep = false;
        ActionEntity action = replayObserver.getNextAction();
        Log.d(MainActivity.LOG_TAG, action.getActionPerformed());
        final String[] split = action.getActionPerformed().split("-");
        Handler handler = new Handler(Looper.getMainLooper());
        switch (split[0]){
            case "INFO":
                sleep = true;
                handler.post(() -> {
                    replayObserver.setText(split[1]);
                });
                break;
            case "ROLLED":
                handler.post(() -> {
                    replayObserver.setText(split[1]);
                });
                break;
            case "MOVE":
                sleep = true;
                handler.post(() -> {
                    replayObserver.move(split[1], split[2]);
                });
                break;
            case "BANKRUPT":
                sleep = true;
                handler.post(() -> {
                    replayObserver.bankrupt(split[1]);
                });
                break;
            case "WINNER":
                sleep = false;
                running = false;
                handler.post(() -> {
                    replayObserver.winner(split[1]);
                });
                break;
        }
    }

    public synchronized void pause() {
        pause = true;
    }

    public synchronized void play() {
        pause = false;
        notifyAll();
    }

    public synchronized void stopThread() {
        running = false;
        notifyAll();
    }
}
