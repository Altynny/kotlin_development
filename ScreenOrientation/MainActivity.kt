package com.example.screenorientation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    lateinit var adapter: ArrayAdapter<CharSequence>
    lateinit var pictureIds: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getPreferences(MODE_PRIVATE)
        val previousPicture = sharedPref.getInt("picture_id", 0)

        adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item)
        val spinner = findViewById<Spinner>(R.id.pictures_list)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(previousPicture)

        val ta = resources.obtainTypedArray(R.array.picture_files)
        pictureIds = IntArray(ta.length()) { i -> ta.getResourceId(i, 0) }
        ta.recycle()

        val iv = findViewById<ImageView>(R.id.picture)
        iv.setImageResource(pictureIds[previousPicture])
    }

    fun onChangePictureClick(view: View) {
        val sharedPref = getPreferences(MODE_PRIVATE)
        var nextPicture = 1 + sharedPref.getInt("picture_id", 0)
        if (nextPicture >= pictureIds.size) nextPicture = 0
        sharedPref.edit {
            putInt("picture_id", nextPicture)
            apply()
        }
        val spinner = findViewById<Spinner>(R.id.pictures_list)
        spinner.setSelection(nextPicture)
        val iv = findViewById<ImageView>(R.id.picture)
        iv.setImageResource(pictureIds[nextPicture])
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        val sharedPref = getPreferences(MODE_PRIVATE)
        sharedPref.edit {
            putInt("picture_id", position)
            apply()
        }
        val iv = findViewById<ImageView>(R.id.picture)
        iv.setImageResource(pictureIds[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        val sharedPref = getPreferences(MODE_PRIVATE)
        sharedPref.edit {
            putInt("picture_id", 0)
            apply()
        }
        val spinner = findViewById<Spinner>(R.id.pictures_list)
        spinner.setSelection(0)
        val iv = findViewById<ImageView>(R.id.picture)
        iv.setImageResource(pictureIds[0])
    }
}