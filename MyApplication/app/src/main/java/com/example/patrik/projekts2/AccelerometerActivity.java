package com.example.patrik.projekts2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {
    private TextView lv_txt;
    private TextView lv_x, lv_y, lv_z;
    private float lf_x, lf_y, lf_z = 0;
    private float lf_last_x, lf_last_y, lf_last_z = 0;
    private Sensor ls_sensor;
    private SensorManager ls_mSensorManager;
    private DrawCircle ls_circle;
    private Bitmap ls_object;
    private DrawCircle ls_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ls_mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (ls_mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            ls_sensor = ls_mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //ls_sensor = ls_mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            ls_mSensorManager.registerListener(this, ls_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            setContentView(R.layout.activity_accelerometer);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            lv_txt = (TextView) findViewById(R.id.txt);
            lv_txt.setText("Buy a new phone with Accelerometer!");

            return;
        }

        ls_object = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        ls_view = new DrawCircle(this);
        ls_view.resume();
        setContentView(ls_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ls_mSensorManager.registerListener(this, ls_mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), ls_mSensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ls_mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//get values from event
        lf_x = event.values[0];
        lf_y = event.values[1];
        lf_z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class DrawCircle extends SurfaceView implements Runnable {
        private SurfaceHolder ls_holder;
        private Thread ls_thread;
        private boolean ls_isRunning;
        private Paint ls_shadow;

        public DrawCircle(Context context) {
            super(context);

            ls_shadow = new Paint();
            ls_holder = getHolder();
        }

        public void pause() {
            ls_isRunning = false;

            while(true) {
                try {
                    ls_thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            ls_thread = null;
        }

        public void resume() {
            ls_isRunning = true;
            ls_thread = new Thread(this);
            ls_thread.start();
        }

        @Override
        public void run() {
            while(ls_isRunning) {
                if (!ls_holder.getSurface().isValid()) {
                    continue;
                }

                try {
                    Canvas canvas = ls_holder.lockCanvas();
                    lf_last_x = 200 + lf_x * 20;
                    lf_last_y = 350 + lf_y * 30;
                    lf_last_z = 100 + lf_z * 10;

                    if (lf_last_x < 100 || lf_last_x > 300) {
                        canvas.drawColor(Color.rgb((int) lf_last_x, (int) lf_last_y, (int) lf_last_z));
                    } else if (lf_last_y < 200 || lf_last_y > 500) {
                        canvas.drawColor(Color.rgb((int) lf_last_x, (int) lf_last_y, (int) lf_last_z));
                    } else {
                        canvas.drawColor(Color.WHITE);
                    }

                    ls_shadow.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);
                    canvas.drawBitmap(ls_object, lf_last_x, lf_last_y, ls_shadow);

                    ls_holder.unlockCanvasAndPost(canvas);
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }
        }
    }
}
