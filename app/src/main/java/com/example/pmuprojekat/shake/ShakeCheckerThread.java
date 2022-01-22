package com.example.pmuprojekat.shake;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.pmuprojekat.MainActivity;

public class ShakeCheckerThread extends Thread{

    private boolean shaking = false;
    private boolean running = true;
    private int waitTime = 3000;
    private LifeCycleGameObserver lifeCycle;
    private MediaPlayer player;

    public ShakeCheckerThread(LifeCycleGameObserver lifeCycle, MediaPlayer player) {
        this.player = player;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void run() {
        synchronized (this)
        {
            try {
                while (running) {
                    //waiting for first shake event
                    while(!shaking && running) {
                        wait();
                    }

                    while (shaking && running)
                    {
                        shaking = false;
                        playMusic();
                        wait(waitTime);
                    }

                    if(running)
                        lifeCycle.stopRoll();

                }
            }catch (Exception e)
            {
                Log.d(MainActivity.LOG_TAG,"Thread error");
            }
        }
    }

    private void playMusic(){
        if(!player.isPlaying()){
            player.start();
        }
    }

    public synchronized void shake(){
        if(lifeCycle.isRolling()) {
            shaking = true;
            notify();
            Log.d(MainActivity.LOG_TAG,"Thread update");
        }
    }

    public synchronized void setWaitTime(int waitTime){
        if(waitTime>0)
            this.waitTime = waitTime;
    }

    public  synchronized  void stopRunning(){
        running = false;
        notify();
    }
}
