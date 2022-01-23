package com.example.pmuprojekat.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.util.Log;

import com.example.pmuprojekat.MainActivity;

public class ShakeDetector implements SensorEventListener {

    private double SHAKE_THRESHOLD_GRAVITY = 1.1;
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private boolean registerShake;
    private LifeCycleGameObserver lifeCycle;


    public ShakeDetector(Context context, LifeCycleGameObserver lifeCycle) {
        this.mContext = context;
        this.mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        this.mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.registerShake = false;
        this.lifeCycle = lifeCycle;
    }

    public void resume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double gX = x / SensorManager.GRAVITY_EARTH;
        double gY = y / SensorManager.GRAVITY_EARTH;
        double gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY && registerShake) {
            lifeCycle.stillRolling();
            Log.d(MainActivity.LOG_TAG, "SHAKE");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setRegisterShake(boolean registerShake){
        this.registerShake = registerShake;
    }

    public void setSHAKE_THRESHOLD_GRAVITY(double SHAKE_THRESHOLD_GRAVITY) {
        this.SHAKE_THRESHOLD_GRAVITY = SHAKE_THRESHOLD_GRAVITY;
    }
}
