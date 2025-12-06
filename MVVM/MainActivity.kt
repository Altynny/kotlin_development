package com.example.mvvmdemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

lateinit var viewModel: MainViewModel
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val provider = ViewModelProvider(this)
        viewModel = provider.get(MainViewModel::class.java)

        observeViewModel()

        initView()
    }

    fun observeViewModel() {
        viewModel.counter.observe(this, Observer {
            val text_counter = findViewById<TextView>(R.id.text_counter)
            text_counter.text = it.toString()
        })
    }

    fun initView() {
        val btn_increment = findViewById<Button>(R.id.btn_increment)
        btn_increment.setOnClickListener {
            viewModel.onIncrementClicked()
        }
    }
}