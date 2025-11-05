package com.example.numberguesser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onGuessClick(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        val etLow = findViewById<EditText>(R.id.lower_limit)
        val etHigh = findViewById<EditText>(R.id.upper_limit)
        val low = etLow.text.toString().toInt()
        val high = etHigh.text.toString().toInt()
        intent.putExtra("low", low)
        intent.putExtra("high", high)
        startActivity(intent)
    }
}