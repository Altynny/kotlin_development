package com.example.memorina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.ImageView
import android.view.View
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var cardIds: IntArray
    lateinit var cardsNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1F // единичный вес

        cardsNames = resources.getStringArray(R.array.card_names)
        val cardFiles = resources.obtainTypedArray(R.array.card_files)
        cardIds = IntArray(cardFiles.length()) { i -> cardFiles.getResourceId(i, 0) }
        cardFiles.recycle()

        val cardViews = ArrayList<ImageView>()
        (1..4)
            .forEach { _ ->
                for (id in cardIds) {
                    val name = cardsNames[cardIds.indexOf(id)]
                    cardViews.add(
                        ImageView(applicationContext).apply {
                            setImageResource(R.drawable.card_back)
                            layoutParams = params
                            tag = name
                            setOnClickListener(cardClickListener)
                        })
                }
            }
        cardViews.shuffle()
        val rows = Array(4) { LinearLayout(applicationContext) }

        var count = 0
        for (view in cardViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count++
        }
        for (row in rows) {
            layout.addView(row)
        }
        setContentView(layout)
    }

    // Обработчик нажатия на карту
    private val cardClickListener = View.OnClickListener { view ->
        val imageView = view as ImageView

        if (!imageView.isEnabled) return@OnClickListener

        val name = imageView.tag as? String ?: return@OnClickListener
        val idx = cardsNames.indexOf(name)
        if (idx == -1) return@OnClickListener
        val faceRes = cardIds.getOrNull(idx) ?: return@OnClickListener

        imageView.isEnabled = false
        imageView.setImageResource(faceRes)

        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000L)
            imageView.setImageResource(R.drawable.card_back)
            imageView.isEnabled = true
        }
    }
}
