package com.example.recyclerviewk25

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val colorsList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ta = resources.obtainTypedArray(R.array.color_palette)
        for (i in 0 until ta.length()) {
            val c = ta.getColor(i, Color.BLACK)
            colorsList.add(c)
        }
        ta.recycle()

        val rv = findViewById<RecyclerView>(R.id.rview)
        val colorAdapter = ColorAdapter(LayoutInflater.from(this))
        colorAdapter.submitList(colorsList.toList())
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = colorAdapter
    }
}