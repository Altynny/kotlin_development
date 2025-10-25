package com.example.randomfilm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import java.util.*
class MainActivity : AppCompatActivity() {
    lateinit var movies : Array<String>
    val r = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        movies = resources.getStringArray(R.array.movies)
    }
    fun onNextClick(view: View) {
        val tvTitle = findViewById<TextView>(R.id.title)
        if (movies.isNotEmpty()) {
            tvTitle.text = movies[r.nextInt(movies.size)]
            movies = movies.filter { it != tvTitle.text }.toTypedArray()
        }
        else {
            tvTitle.text = resources.getString(R.string.empty_array_of_movies)
        }
    }

    fun onResetClick(view: View) {
        val tvTitle = findViewById<TextView>(R.id.title)
        movies = resources.getStringArray(R.array.movies)
        tvTitle.text = ""
    }
}