package com.example.patrik.projekts2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button lv_accelerometer;
    private Button lv_compass;
    private Button lv_proximity;
    private Button lv_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_accelerometer = (Button)findViewById(R.id.acceler);
        lv_compass = (Button)findViewById(R.id.compass);
        lv_proximity = (Button)findViewById(R.id.proxi);
        lv_camera = (Button)findViewById(R.id.camera);

        lv_accelerometer.setOnClickListener(this);
        lv_compass.setOnClickListener(this);
        lv_proximity.setOnClickListener(this);
        lv_camera.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Domonkos Zlocha", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.runFinalizersOnExit(true);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceler:
                startActivity(new Intent(MainActivity.this, AccelerometerActivity.class));
                break;
            case R.id.compass:
                startActivity(new Intent(MainActivity.this, CompassActivity.class));
                break;
            case R.id.proxi:
                startActivity(new Intent(MainActivity.this, ProximityActivity.class));
                break;
            case R.id.light:
                startActivity(new Intent(MainActivity.this, LightActivity.class));
                break;
            case R.id.camera:
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
                break;
            default:
                break;
        }
    }
}
