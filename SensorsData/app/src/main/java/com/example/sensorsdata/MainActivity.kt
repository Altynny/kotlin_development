package com.example.sensorsdata

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sensorsdata.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var dataSen: ActivityMainBinding
    private lateinit var sensorManager: SensorManager

    private var activeSensorType: Int = Sensor.TYPE_LIGHT
    private var dataSensor: String = ""

    private var lightText: String = ""
    private var rotorText: String = ""
    private var accelerometerText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataSen = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataSen.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        dataSen.l.isChecked = true
        dataSen.sensText = ""

        dataSen.radioGroupSensors.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.l -> activateSensor(Sensor.TYPE_LIGHT, R.string.sensorAbsentL)
                R.id.r -> activateSensor(Sensor.TYPE_ROTATION_VECTOR, R.string.sensorAbsentR)
                R.id.a -> activateSensor(Sensor.TYPE_ACCELEROMETER, R.string.sensorAbsentA)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerSensor(Sensor.TYPE_LIGHT)
        registerSensor(Sensor.TYPE_ROTATION_VECTOR)
        registerSensor(Sensor.TYPE_ACCELEROMETER)

        activateSensor(Sensor.TYPE_LIGHT, R.string.sensorAbsentL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_LIGHT -> {
                lightText = "${getString(R.string.light)}: ${format(event.values[0])} lx"
                if (activeSensorType == Sensor.TYPE_LIGHT) {
                    setSensorText(lightText)
                }
            }

            Sensor.TYPE_ROTATION_VECTOR -> {
                rotorText = buildThreeValueText(
                    getString(R.string.rotor),
                    event.values[0],
                    event.values[1],
                    event.values[2]
                )
                if (activeSensorType == Sensor.TYPE_ROTATION_VECTOR) {
                    setSensorText(rotorText)
                }
            }

            Sensor.TYPE_ACCELEROMETER -> {
                accelerometerText = buildThreeValueText(
                    getString(R.string.accelerometer),
                    event.values[0],
                    event.values[1],
                    event.values[2]
                )
                if (activeSensorType == Sensor.TYPE_ACCELEROMETER) {
                    setSensorText(accelerometerText)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun registerSensor(type: Int) {
        sensorManager.getDefaultSensor(type)?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun activateSensor(type: Int, absentMessageResId: Int) {
        activeSensorType = type

        val sensorExists = sensorManager.getDefaultSensor(type) != null
        if (!sensorExists) {
            Toast.makeText(this, getString(absentMessageResId), Toast.LENGTH_SHORT).show()
            setSensorText("")
            return
        }

        val text = when (type) {
            Sensor.TYPE_LIGHT -> lightText
            Sensor.TYPE_ROTATION_VECTOR -> rotorText
            Sensor.TYPE_ACCELEROMETER -> accelerometerText
            else -> ""
        }

        setSensorText(text)
    }

    private fun setSensorText(value: String) {
        dataSensor = value
        dataSen.sensText = dataSensor
        dataSen.executePendingBindings()
    }

    private fun buildThreeValueText(title: String, x: Float, y: Float, z: Float): String {
        return buildString {
            append(title)
            append('\n')
            append("X: ").append(format(x))
            append('\n')
            append("Y: ").append(format(y))
            append('\n')
            append("Z: ").append(format(z))
        }
    }

    private fun format(value: Float): String {
        return String.format(Locale.getDefault(), "%.2f", value)
    }
}