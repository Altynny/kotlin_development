package com.example.randomfilm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {
    var movies: MutableList<Movie> = mutableListOf()
    val r = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadMoviesFromRaw()
    }

    private fun loadMoviesFromRaw() {
        try {
            val inputStream = resources.openRawResource(R.raw.movies)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                sb.append(line)
                line = reader.readLine()
            }
            reader.close()
            val json = sb.toString()
            val arr = JSONArray(json)
            val list = mutableListOf<Movie>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val movie = Movie.fromJson(obj)
                list.add(movie)
            }
            movies = list
            Log.i("MainActivity", "Loaded ${movies.size} movies from raw resource")
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to load movies.json", e)
        }
    }

    fun onNextClick(view: View) {
        val tvTitle = findViewById<TextView>(R.id.title)
        val tvDirector = findViewById<TextView>(R.id.director)
        val tvYear = findViewById<TextView>(R.id.year)
        val tvRating = findViewById<TextView>(R.id.rating)
        val tvGenres = findViewById<TextView>(R.id.genres)
        val tvDescription = findViewById<TextView>(R.id.description)
        if (movies.isNotEmpty()) {
            val index = r.nextInt(movies.size)
            val movie = movies.removeAt(index)
            tvTitle.text = movie.title
            tvYear.text = movie.year.toString()
            tvDirector.text = movie.director
            tvRating.text = movie.rating.toString()
            tvGenres.text = movie.genres.joinToString(", ")
            tvDescription.text = movie.description
        } else {
            tvTitle.text = resources.getString(R.string.empty_array_of_movies)
            tvDirector.text = ""
            tvYear.text = ""
            tvRating.text = ""
            tvGenres.text = ""
            tvDescription.text = ""
        }
    }

    fun onResetClick(view: View) {
        val tvTitle = findViewById<TextView>(R.id.title)
        val tvDirector = findViewById<TextView>(R.id.director)
        val tvYear = findViewById<TextView>(R.id.year)
        val tvRating = findViewById<TextView>(R.id.rating)
        val tvGenres = findViewById<TextView>(R.id.genres)
        val tvDescription = findViewById<TextView>(R.id.description)
        loadMoviesFromRaw()
        tvTitle.text = ""
        tvDirector.text = ""
        tvYear.text = ""
        tvRating.text = ""
        tvGenres.text = ""
        tvDescription.text = ""
    }
}