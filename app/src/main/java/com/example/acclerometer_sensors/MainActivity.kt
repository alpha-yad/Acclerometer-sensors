package com.example.acclerometer_sensors

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(),SensorEventListener {
    //variables setups
    private var sensorManager: SensorManager? =  null
    private var  isColor = false
    lateinit private var view : View
    private var lastUpdate:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()
        view = findViewById(R.id.textdisp)
        view.setBackgroundColor(Color.GREEN)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if(event.sensor.type === Sensor.TYPE_ACCELEROMETER ){
                getAccelerometer(event)
            }
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val value: FloatArray = event.values
        val x : Float = value[0]
        val y: Float = value[1]
        val z: Float = value[2]
        val accelerationSquareRoot:Float = (x*x + y*y + z*z)/
                (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()
        if(accelerationSquareRoot >= 2){
            if((actualTime - lastUpdate) <= 200) return
            lastUpdate = System.currentTimeMillis()
            if(isColor){
                view.setBackgroundColor(Color.RED)
            }
            else{
                view.setBackgroundColor(Color.YELLOW)
            }
            isColor = !isColor
        }

    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            this@MainActivity,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }
}