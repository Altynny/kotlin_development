package com.example.recyclerviewk25

import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // получаем ссылку на текстовое поле в каждом элементе списка
    val tv = itemView.findViewById<TextView>(R.id.color)
    var currentColor: Int = Color.BLACK

    init {
        itemView.setOnClickListener {
            val hex = String.format("#%06X", 0xFFFFFF and currentColor)
            Toast.makeText(itemView.context, hex, Toast.LENGTH_SHORT).show()
        }
    }

    fun bindTo(color: Int) {
        currentColor = color
        tv.setBackgroundColor(color)
        tv.text = String.format("#%06X", 0xFFFFFF and color)
        val luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255.0
        val textColor = if (luminance < 0.5) Color.WHITE else Color.BLACK
        tv.setTextColor(textColor)
    }
}
