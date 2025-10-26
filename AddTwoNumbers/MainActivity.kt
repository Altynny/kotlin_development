package com.example.addtwonumbers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        val etA = findViewById<EditText>(R.id.numA)
        val etB = findViewById<EditText>(R.id.numB)
        val tvSum = findViewById<TextView>(R.id.sum)
        val strA = etA.text.toString()
        val strB = etB.text.toString()
        if (strA.isEmpty() or strB.isEmpty())
            tvSum.text = resources.getString(R.string.emptyField)
        else {
            val sum = strA.toFloat() + strB.toFloat()
            if (sum.rem(1f) == 0f)
            {
                tvSum.text = sum.toInt().toString()
            }
            else {
                tvSum.text = sum.toString()
            }
        }
    }
}