package com.example.adapters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import kotlin.String

lateinit var people: MutableList<String>
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstNames = resources.getStringArray(R.array.first_names)
        val secondNames = resources.getStringArray(R.array.second_names)
        people = MutableList(14) { i -> "${firstNames.random()} ${secondNames.random()}"}
        val lvPeople = findViewById<ListView>(R.id.people)
        val adapter = ArrayAdapter<String>(this, R.layout.item, people)
        lvPeople.adapter = adapter
    }

    fun onAddPersonClick(view: View) {
        val et = findViewById<EditText>(R.id.people_input)
        if (et.text.toString().isEmpty()) return
        people.add(et.text.toString())
        et.text.clear()
        val adapter = findViewById<ListView>(R.id.people).adapter as ArrayAdapter<*>
        adapter.notifyDataSetChanged()
    }
}