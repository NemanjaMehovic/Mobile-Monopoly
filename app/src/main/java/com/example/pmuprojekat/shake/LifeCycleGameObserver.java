package com.example.pmuprojekat.shake;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.fragments.gameFragment;

public class LifeCycleGameObserver implements DefaultLifecycleObserver {

    private MainActivity mainActivity;
    private gameFragment fragment;
    private ShakeDetector detector;
    private ShakeCheckerThread thread;
    private boolean rolling = false;
    private MediaPlayer player;
    private long timeStart;
    private long timeEnd;

    public LifeCycleGameObserver(MainActivity mainActivity, gameFragment fragment) {
        this.mainActivity = mainActivity;
        this.fragment = fragment;
        this.detector = new ShakeDetector(mainActivity, this);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d(MainActivity.LOG_TAG, "START");

        player = new MediaPlayer();
        boolean tmpFlag = true;
        while (tmpFlag) {
            try {
                AssetFileDescriptor afd = mainActivity.getAssets().openFd("dice.mp3");
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                player.prepareAsync();
                tmpFlag = false;
            } catch (Exception e) {
                Log.d(MainActivity.LOG_TAG, "Failed to load MediaPlayer trying again");
            }
        }

        thread = new ShakeCheckerThread(this, player);
        this.mainActivity.waitTimeBetweenShakesInSec.observe(fragment.getViewLifecycleOwner(), integer -> {
            thread.setWaitTime(integer * 1000);
        });
        thread.start();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d(MainActivity.LOG_TAG, "RESUME");
        this.mainActivity.shakeThreshold.observe(fragment.getViewLifecycleOwner(), aDouble -> {
            detector.setSHAKE_THRESHOLD_GRAVITY(aDouble);
        });
        timeStart = System.currentTimeMillis();
        detector.resume();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d(MainActivity.LOG_TAG, "PAUSE");
        timeEnd = System.currentTimeMillis();
        mainActivity.repository.setTime(timeEnd - timeStart);
        detector.pause();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d(MainActivity.LOG_TAG, "STOP");
        thread.stopRunning();
        thread = null;
        player.release();
    }

    public synchronized void startRoll() {
        rolling = true;
        detector.setRegisterShake(true);
    }

    public void stillRolling() {
        if (rolling)
            thread.shake();
    }

    public synchronized void stopRoll() {
        rolling = false;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            detector.setRegisterShake(false);
            fragment.rolled();
        });
    }

    public boolean isRolling() {
        return rolling;
    }
}
