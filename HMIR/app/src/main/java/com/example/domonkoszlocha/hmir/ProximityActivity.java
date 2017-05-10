package com.example.domonkoszlocha.hmir;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;


public class ProximityActivity extends AppCompatActivity implements SensorEventListener {
    private Sensor ls_sensor;
    private SensorManager ls_mSensorManager;
    private TextView lv_txt;
    private ImageView ls_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        ls_mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (ls_mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            ls_sensor = ls_mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            ls_mSensorManager.registerListener(this, ls_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            setContentView(R.layout.activity_proximity);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            lv_txt = (TextView) findViewById(R.id.txt);
            lv_txt.setText("Buy a new phone with Proximity sensor!");

            return;
        }

        ls_img = (ImageView) findViewById(R.id.imgView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ls_mSensorManager.registerListener(this, ls_sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ls_mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0) {
            ls_img.setImageResource(R.drawable.trol);
        } else {
            ls_img.setImageResource(R.drawable.mid);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
