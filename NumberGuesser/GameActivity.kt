package com.example.numberguesser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    var low: Int = 0
    var high: Int = 100
    var middle: Int = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        low = intent.getIntExtra("low", 0)
        high = intent.getIntExtra("high", 100)
        middle = (low+high)/2
        val tvQuestion = findViewById<TextView>(R.id.question)
        tvQuestion.text = String.format(resources.getString(R.string.question), middle)
    }

    fun onYesNoClick(view: View) {
        val tvQuestion = findViewById<TextView>(R.id.question)
        if (high - low > 2) when (view.id) {
            R.id.yes -> {
                low = middle
                middle = (middle + high) / 2
            }
            R.id.no -> {
                high = middle
                middle = (low + middle) / 2
            }
        }
        if (high - low > 2) {
            tvQuestion.text = String.format(resources.getString(R.string.question), middle)
        }
        else when (high - low) {
            1 -> {
                val intent = Intent(this, FinalActivity::class.java)
                intent.putExtra("guessed_number", when (view.id) {
                    R.id.yes -> low
                    R.id.no -> high
                    else -> null
                })
                startActivity(intent)
            }
            2 -> {
                low = high - 1
                tvQuestion.text = String.format(resources.getString(R.string.guess), low)
            }
        }
    }
}