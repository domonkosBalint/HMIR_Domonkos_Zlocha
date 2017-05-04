package com.example.patrik.projekts2;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class LightActivity extends AppCompatActivity implements SensorEventListener {
    private Sensor ls_sensor;
    private SensorManager ls_mSensorManager;
    private TextView lv_txt;
    private int lv_rgb, lv_last, lv_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        lv_txt = (TextView) findViewById(R.id.txt);

        ls_mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (ls_mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            ls_sensor = ls_mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            ls_mSensorManager.registerListener(this, ls_sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            setContentView(R.layout.activity_light);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            lv_txt.setText("Buy a new phone with Light sensor!");

            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ls_mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ls_mSensorManager.registerListener(this, ls_sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lv_rgb = (int) event.values[0];

        if (lv_rgb > 255) {
            lv_rgb = 255;
        }

        if (lv_rgb > lv_last) {
            lv_count++;

            for (int i=lv_last;i<lv_rgb;i++) {
                lv_txt.setText("Light: " + i + "\nPocet zmien: " + lv_count);
                lv_txt.setBackgroundColor(Color.rgb(50, 70, i));
            }
        } else if (lv_rgb < lv_last) {
            lv_count++;

            for (int i=lv_last;i>lv_rgb;i--) {
                lv_txt.setText("Light: " + i + "\nPocet zmien: " + lv_count);
                lv_txt.setBackgroundColor(Color.rgb(50, 70, i));
            }
        } else {
            lv_txt.setText("Light: " + event.values[0] + "\nPocet zmien: " + lv_count);
            lv_txt.setBackgroundColor(Color.rgb(50, 70, lv_rgb));
        }

        lv_last = lv_rgb;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
