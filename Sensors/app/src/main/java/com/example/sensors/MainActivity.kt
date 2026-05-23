package com.example.sensors

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var spinner: Spinner
    private lateinit var listSensor: TextView

    private val environmentalSensors = arrayOf(
        Sensor.TYPE_MAGNETIC_FIELD,
        Sensor.TYPE_LIGHT,
        Sensor.TYPE_PRESSURE,
        Sensor.TYPE_RELATIVE_HUMIDITY,
        Sensor.TYPE_AMBIENT_TEMPERATURE
    )

    private val positionSensors = arrayOf(
        Sensor.TYPE_ACCELEROMETER,
        Sensor.TYPE_GYROSCOPE,
        Sensor.TYPE_PROXIMITY,
        Sensor.TYPE_GRAVITY,
        Sensor.TYPE_LINEAR_ACCELERATION,
        Sensor.TYPE_ROTATION_VECTOR,
        Sensor.TYPE_GAME_ROTATION_VECTOR,
        Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
        Sensor.TYPE_SIGNIFICANT_MOTION,
        Sensor.TYPE_STEP_DETECTOR,
        Sensor.TYPE_STEP_COUNTER,
        Sensor.TYPE_MOTION_DETECT
    )

    private val humanSensors = arrayOf(
        Sensor.TYPE_HEART_RATE,
        Sensor.TYPE_HEART_BEAT,
        Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        spinner = findViewById(R.id.spinner)
        listSensor = findViewById(R.id.list_sensor)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type_sensors,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(0)
        showSensors(0)

        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                showSensors(position)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                listSensor.text = ""
            }
        }
    }

    private fun showSensors(position: Int) {
        val types = when (position) {
            0 -> environmentalSensors
            1 -> positionSensors
            2 -> humanSensors
            else -> emptyArray()
        }

        val names = mutableListOf<String>()

        for (type in types) {
            val sensors = sensorManager.getSensorList(type)
            for (sensor in sensors) {
                names.add(sensor.name)
            }
        }

        listSensor.text = if (names.isEmpty()) {
            ""
        } else {
            names.joinToString("\n")
        }
    }
}