package com.example.patrik.projekts2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class GyroscopeAcitivity extends AppCompatActivity implements SensorEventListener {
    private Sensor ls_sensor;
    private SensorManager ls_mSensorManager;
    private float lf_x, lf_y, lf_z = 0;
    private float lf_last_x, lf_last_y, lf_last_z = 0;
    private TextView lv_txt;
    private static final float lf_NS2S = 1.0f / 1000000000.0f;
    private final float[] lf_deltaRotationVector = new float[4];
    private float lf_timestamp;
    private float lf_omegaMgnitude;
    private MyView ls_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_gyroscope_acitivity);

        ls_mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (ls_mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            ls_sensor = ls_mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            ls_mSensorManager.registerListener(this, ls_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            setContentView(R.layout.activity_accelerometer);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            lv_txt = (TextView) findViewById(R.id.txt);
            lv_txt.setText("Buy a new phone with Gyroscope!");

            return;
        }

        ls_view = new MyView(this);
        ls_view.resume();
        setContentView(ls_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ls_mSensorManager.registerListener(this, ls_mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), ls_mSensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ls_mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lf_timestamp != 0) {
            final float dT = (event.timestamp - lf_timestamp) * lf_NS2S;

            lf_x = event.values[0];
            lf_y = event.values[1];
            lf_z = event.values[2];

            lf_omegaMgnitude = (float) Math.sqrt(lf_x * lf_x + lf_y * lf_y + lf_z * lf_z);

            if (lf_omegaMgnitude > 0.000001) {
                lf_x /= lf_omegaMgnitude;
                lf_y /= lf_omegaMgnitude;
                lf_z /= lf_omegaMgnitude;
            }

            float thetaOverTwo = lf_omegaMgnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);

            lf_deltaRotationVector[0] = sinThetaOverTwo * lf_x;
            lf_deltaRotationVector[1] = sinThetaOverTwo * lf_y;
            lf_deltaRotationVector[2] = sinThetaOverTwo * lf_z;
            lf_deltaRotationVector[3] = cosThetaOverTwo;
        }

        lf_timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, lf_deltaRotationVector);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyView extends SurfaceView implements Runnable {
        private int lv_radius = 20;
        private Paint paint = new Paint();
        private SurfaceHolder ls_holder;
        private Thread ls_thread;
        private boolean ls_isRunning;

        public MyView(Context context) {
            super(context);

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
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override
        public void run() {
            Canvas canvas;

            lf_last_x = getWidth()/2;
            lf_last_y = getHeight()/2;

            while(ls_isRunning) {
                if (!ls_holder.getSurface().isValid()) {
                    continue;
                }

                try {
                    canvas = ls_holder.lockCanvas();

                    lf_x = lf_last_x  + lf_deltaRotationVector[1]*80;
                    lf_y = lf_last_y + lf_deltaRotationVector[0]*120;

                    if (lf_x >= getWidth()-10) {
                        lf_x = getWidth()-10;
                    } else if (lf_x <= 10) {
                        lf_x = 10;
                    }

                    if (lf_y >= getHeight()-10) {
                        lf_y = getHeight()-10;
                    } else if (lf_y <= 10) {
                        lf_y = 10;
                    }

                    lf_last_x = lf_x;
                    lf_last_y = lf_y;

                    paint.setStyle(Paint.Style.FILL);


                    if ((lf_x >= getWidth()-50 && lf_y >= getHeight()-50)
                       || (lf_x <= 50 && lf_y <= 50)
                       || (lf_x <= 50 && lf_y >= getHeight()-50)
                       || (lf_x >= getWidth()-50 && lf_y <= 50)) {

                        paint.setColor(Color.WHITE);
                        lf_last_x = lf_x = getWidth()/2;
                        lf_last_y = lf_y = getHeight()/2;

                    } else {
                        paint.setColor(Color.rgb(0, 180, 0));
                    }

                    canvas.drawPaint(paint);
                    paint.setColor(Color.WHITE);
                    paint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);
                    canvas.drawCircle(lf_x, lf_y, lv_radius, paint);

                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.BLACK);
                    paint.setShadowLayer(0, 0, 0, 0);
                    canvas.drawCircle(getWidth(), getHeight(), 60, paint);
                    canvas.drawCircle(0, 0, 60, paint);
                    canvas.drawCircle(getWidth(), 0, 60, paint);
                    canvas.drawCircle(0, getHeight(), 60, paint);

                    ls_holder.unlockCanvasAndPost(canvas);

                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }

            }
    }
}
