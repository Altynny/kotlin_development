package com.example.numberguesser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class FinalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)
        val guessedNumber = intent.getIntExtra("guessed_number", 50)
        val tvGuessedNumber = findViewById<TextView>(R.id.guessed_number)
        tvGuessedNumber.text = String.format(resources.getString(R.string.final_answer), guessedNumber)
    }

    fun onRestartClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}