package com.example.android.sensorapp1;

import android.app.KeyguardManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    DevicePolicyManager dpm;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    private TextView sensorText;
    private Sensor sensor;
    private SensorManager sensorManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorText = (TextView) findViewById(R.id.textView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int distance = (int) event.values[0];
        sensorText.setText("Distancia: " + distance);
        if(distance == 0){

                //screen is not locked
                dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                try {
                    dpm.lockNow();
                    finish();
                } catch (SecurityException e) {
                    Intent it = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    it.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, DeviceAdminReceiver.class));
                    startActivityForResult(it, 0);
                }
            //Reduce el brillo de la pantalla a 0
            //Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);

        }
        /*
        else if(distance == 1){

            //Aumenta el brillo de la pantalla
            //Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            dpm.lockNow();
        } else {
            Toast.makeText(this, "Se necestan permisos de administrador", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
