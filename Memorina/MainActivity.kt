package com.example.memorina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.ImageView
import android.view.View
import android.content.Intent
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var cardIds: IntArray
    lateinit var cardsNames: Array<String>
    private val faceMap = mutableMapOf<ImageView, Int>()
    private val openedCards = mutableListOf<ImageView>()
    private var isChecking = false
    private var matchedPairs = 0
    private var totalPairs = 0

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
                        faceMap[this] = id
                    }
                )
            }
        }

        totalPairs = cardViews.size / 2
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
        val imageView = view as? ImageView ?: return@OnClickListener
        if (isChecking || imageView.visibility != View.VISIBLE || !imageView.isEnabled) return@OnClickListener

        val faceRes = faceMap[imageView] ?: return@OnClickListener
        imageView.setImageResource(faceRes)
        imageView.isEnabled = false
        openedCards.add(imageView)

        if (openedCards.size == 2) {
            isChecking = true
            lifecycleScope.launch(Dispatchers.Main) {
                delay(1000L)

                val first = openedCards[0]
                val second = openedCards[1]

                val firstRes = faceMap[first]
                val secondRes = faceMap[second]

                if (firstRes != null && firstRes == secondRes) {
                    first.visibility = View.INVISIBLE
                    second.visibility = View.INVISIBLE
                    first.isEnabled = false
                    second.isEnabled = false

                    matchedPairs++

                    if (matchedPairs >= totalPairs) {
                        val intent = Intent(this@MainActivity, GameOverActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    first.setImageResource(R.drawable.card_back)
                    second.setImageResource(R.drawable.card_back)
                    first.isEnabled = true
                    second.isEnabled = true
                }

                openedCards.clear()
                isChecking = false
            }
        }
    }
}
