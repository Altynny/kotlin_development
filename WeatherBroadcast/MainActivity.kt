package com.example.widgetsdemo2728

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.Toolbar
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 10000L
    private val fileCheckRunnable = object : Runnable {
        override fun run() {
            checkFile()
            handler.postDelayed(this, checkInterval)
        }
    }
    override fun onResume() {
        super.onResume()
        handler.post(fileCheckRunnable)
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(fileCheckRunnable)
    }
    private fun checkFile() {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "WeatherFile.json")
        if (file.exists()) {
            val isExpired = System.currentTimeMillis() - file.lastModified() > 15000
            if (isExpired) {
                val json = JSONObject(file.readText())
                val name = json.optString("name")
                runOnUiThread {
                    Toast.makeText(this, "Updating expired weather", Toast.LENGTH_SHORT).show()
                }
                fetchWeather(name)
            }
        }
    }

    val sharedViewModel: SharedViewModel by viewModels()
    private var lastCity: String? = null
    private var lastLocale: String? = null
    private var lastWindSpeed: Double = Double.NaN
    private var lastWindDeg: Int = -1
    lateinit var briefFragment: Fragment
    lateinit var detailedFragment: Fragment

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("city", lastCity)
        outState.putString("locale", lastLocale)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        briefFragment = supportFragmentManager.findFragmentByTag("BRIEF_TAG") ?: BriefFragment()
        detailedFragment = supportFragmentManager.findFragmentByTag("DETAILED_TAG") ?: DetailedFragment()

        if (savedInstanceState == null) {
            lastLocale = "ru"
            briefFragment = BriefFragment()
            detailedFragment = DetailedFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container_fragm, briefFragment, "BRIEF_TAG")
                .commit()
        } else {
            lastCity = savedInstanceState.getString("city")
            lastLocale = savedInstanceState.getString("locale")
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.languages)
        toolbar.setOnMenuItemClickListener { item ->
            lastLocale = item.toString()
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lastLocale)
            AppCompatDelegate.setApplicationLocales(appLocale)
            lastCity?.let { fetchWeather(it) }
            ; true }

        val buttonFetch =  findViewById<Button>(R.id.buttonFetch)
        val editCity =  findViewById<EditText>(R.id.editCity)
        val radioGroupUnits =  findViewById<RadioGroup>(R.id.radioGroupUnits)
        val cbShowWindDir =  findViewById<CheckBox>(R.id.cbShowWindDir)
        val rbF =  findViewById<RadioButton>(R.id.rbF)

        buttonFetch.setOnClickListener {
            val city = editCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, getString(R.string.hint_city), Toast.LENGTH_SHORT).show()
            } else {
                lastCity = city
                fetchWeather(city)
            }
        }

        editCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                buttonFetch.performClick()
                true
            } else false
        }

        radioGroupUnits.setOnCheckedChangeListener { _: RadioGroup, _: Int ->
            sharedViewModel.weatherData.unitSymbol.set(if (rbF.isChecked) "°F" else "°C")
            lastCity?.let { fetchWeather(it) }
        }

        cbShowWindDir.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            updateWindDisplay()
        }

        val changeBrief = findViewById<Button>(R.id.brief)
        val changeDetailed = findViewById<Button>(R.id.detailed)

        changeBrief.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_fragm, briefFragment, "BRIEF_TAG")
            ft.commit() }

        changeDetailed.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_fragm, detailedFragment, "DETAILED_TAG")
            ft.commit() }

        val file: File = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "WeatherFile.json")
        if (file.exists()) {
            Toast.makeText(this, "Reading weather from file", Toast.LENGTH_SHORT).show()
            updateWeather()
        }
    }

    private fun updateWeather() {
        Thread {
            try {
                val rbF =  findViewById<RadioButton>(R.id.rbF)

                val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "WeatherFile.json")
                runOnUiThread {
                    Toast.makeText(this, "Updating UI", Toast.LENGTH_SHORT).show()
                }
                val json = JSONObject(file.readText())
                val name = json.optString("name")
                val main = json.optJSONObject("main")
                val temp = main?.optDouble("temp", Double.NaN) ?: Double.NaN
                val humidity = main?.optInt("humidity", -1) ?: -1

                val weatherArr = json.optJSONArray("weather")
                val desc = if (weatherArr != null && weatherArr.length() > 0) weatherArr.getJSONObject(0).optString("description", "") else ""
                val icon = if (weatherArr != null && weatherArr.length() > 0) weatherArr.getJSONObject(0).optString("icon", "") else ""

                val windObj = json.optJSONObject("wind")
                val windSpeed = windObj?.optDouble("speed", Double.NaN) ?: Double.NaN
                val windDeg = windObj?.optInt("deg", -1) ?: -1

                lastWindSpeed = windSpeed
                lastWindDeg = windDeg

                // Обновляем UI через main thread
                runOnUiThread {
                    sharedViewModel.weatherData.city.set(name)
                    val unitSymbol = if (rbF.isChecked) "°F" else "°C"
                    sharedViewModel.weatherData.unitSymbol.set(unitSymbol)
                    sharedViewModel.weatherData.temp.set(if (!temp.isNaN()) String.format(Locale.getDefault(), "%.1f %s", temp, unitSymbol) else "")
                    sharedViewModel.weatherData.description.set(desc.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                    sharedViewModel.weatherData.humidity.set(if (humidity >= 0) getString(R.string.humidity,humidity) else "")
                    sharedViewModel.weatherData.iconCode.set(icon)
                    updateWindDisplay()
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this,
                        getString(R.string.error, t.localizedMessage), Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
    private fun fetchWeather(cityName: String) {
        Thread {
            try {
                val rbF =  findViewById<RadioButton>(R.id.rbF)
                val apiKey = getString(R.string.openweather_api_key).trim()
                val units = if (rbF.isChecked) "imperial" else "metric"
                val q = URLEncoder.encode(cityName, "UTF-8")
                val urlStr = "https://api.openweathermap.org/data/2.5/weather?q=$q&appid=$apiKey&units=$units&lang=$lastLocale"
                val url = URL(urlStr)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 10_000
                conn.readTimeout = 10_000

                val code = conn.responseCode
                val stream = if (code == 200) conn.inputStream else conn.errorStream
                val body = stream.bufferedReader().use { it.readText() }

                if (code != 200) {
                    Log.e("WeatherFetch", "code=$code body=$body")
                    runOnUiThread {
                        Toast.makeText(this, "Ошибка запроса: $code", Toast.LENGTH_LONG).show()
                    }
                    return@Thread
                }

                val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "WeatherFile.json")
                val json = JSONObject(body)
                runOnUiThread {
                    Toast.makeText(this, "Writing weather to a file", Toast.LENGTH_SHORT).show()
                }
                file.writeText(json.toString(4))
                updateWeather()
            } catch (t: Throwable) {
                t.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this,
                        getString(R.string.error, t.localizedMessage), Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun updateWindDisplay() {
        runOnUiThread {
            val cbShowWindDir = findViewById<CheckBox>(R.id.cbShowWindDir)
            val rbF =  findViewById<RadioButton>(R.id.rbF)

            if (!cbShowWindDir.isChecked) {
                sharedViewModel.weatherData.wind.set("")
                return@runOnUiThread
            }

            val speedPart = if (!lastWindSpeed.isNaN()) {
                val unitSpeed = if (rbF.isChecked) getString(R.string.mph) else getString(R.string.mps)
                String.format(Locale.getDefault(),
                    getString(R.string.wind_speed), lastWindSpeed, unitSpeed)
            } else {""}

            val dirPart = if (lastWindDeg >= 0) {
                getString(R.string.direction, lastWindDeg)
            } else {""}

            val windText = if (speedPart.isNotEmpty()) speedPart + dirPart else ""
            sharedViewModel.weatherData.wind.set(windText)
        }
    }
}
