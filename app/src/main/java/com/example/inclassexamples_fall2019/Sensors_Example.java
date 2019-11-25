package com.example.inclassexamples_fall2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class Sensors_Example extends AppCompatActivity {


    SensorManager mSensorManager = null;
    Sensor mSensor = null;
    boolean flashLightStatus = false;
    boolean deviceHasCameraFlash = false;

    CameraManager camManager = null;
    String cameraId = null;

    EditText xEditText , yEditText, zEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Uncomment these lines for full screen apps:
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_sensors__example);

        //load the edit text from the screen. We will write to them later.
        xEditText = (EditText)findViewById(R.id.x_values);
        yEditText = (EditText)findViewById(R.id.y_values);
        zEditText = (EditText)findViewById(R.id.z_values);


        //Get the sensor manager and listen to
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Whenever the sensor changes, call the function from LightListener class:
        if(mSensor != null)
            mSensorManager.registerListener(new LightListener(), mSensor, SensorManager.SENSOR_DELAY_NORMAL);


        //Flashlight example
        // flashlight code  example.

        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        deviceHasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Button flashButton = (Button) findViewById(R.id.flashlight_button);
        flashButton.setOnClickListener(e ->
        {
            try {
                cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                if(deviceHasCameraFlash) {
                    flashLightStatus = !flashLightStatus;  //flip true to false, or false to true
                    camManager.setTorchMode(cameraId, flashLightStatus);
                }
            }catch(Exception ex){
                Log.e("Error", ex.getMessage());
            }
        });

        //end of flashlight example

        //Vibration example. Look at powerpoint slides on vibration
        Button vibrateButton = (Button)findViewById(R.id.vibrate_button);
        Vibrator vibrateMotor = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        vibrateButton.setOnClickListener( e ->{

            long [] pattern = new long[]{500, 2500, 500, 500};
            int [] amplitudes = new int[] {0, 255, 0, 128};
            // vibrateMotor.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1) );
//older apis:
            vibrateMotor.vibrate(pattern, -1);
        });
        //end of vibration example



    }


    class LightListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // Do something here if sensor accuracy changes.
            // You must implement this callback in your code.
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
//            float y = event.values[1];
     //       float z = event.values[2];

            xEditText.setText("X: "+x);
 //           yEditText.setText("Y: "+y);
 //           zEditText.setText("Z: "+z);
        }

    }
}
