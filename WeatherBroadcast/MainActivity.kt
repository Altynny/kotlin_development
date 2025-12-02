package com.example.widgetsdemo2728

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.widgetsdemo2728.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherData = WeatherData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weather = weatherData

        binding.buttonFetch.setOnClickListener {
            val city = binding.editCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Введите город", Toast.LENGTH_SHORT).show()
            } else {
                fetchWeather(city)
            }
        }

        binding.editCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.buttonFetch.performClick()
                true
            } else false
        }
    }

    private fun fetchWeather(cityName: String) {
        Thread {
            try {
                val apiKey = getString(R.string.openweather_api_key).trim()
                if (apiKey.isEmpty() || apiKey == "ВАШ_API_КЛЮЧ") {
                    runOnUiThread {
                        Toast.makeText(this, "Положите API ключ в strings.xml (openweather_api_key)", Toast.LENGTH_LONG).show()
                    }
                    return@Thread
                }

                val q = URLEncoder.encode(cityName, "UTF-8")
                val urlStr = "https://api.openweathermap.org/data/2.5/weather?q=$q&appid=$apiKey&units=metric"
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

                val json = JSONObject(body)
                Log.d("WeatherFetch", body)

                val name = json.optString("name", cityName)
                val main = json.optJSONObject("main")
                val temp = main?.optDouble("temp", Double.NaN) ?: Double.NaN
                val humidity = main?.optInt("humidity", -1) ?: -1

                val weatherArr = json.optJSONArray("weather")
                val desc = if (weatherArr != null && weatherArr.length() > 0) weatherArr.getJSONObject(0).optString("description", "") else ""
                val icon = if (weatherArr != null && weatherArr.length() > 0) weatherArr.getJSONObject(0).optString("icon", "") else ""

                val windObj = json.optJSONObject("wind")
                val windSpeed = windObj?.optDouble("speed", Double.NaN) ?: Double.NaN
                val windDeg = windObj?.optInt("deg", -1) ?: -1

                // Обновляем UI через main thread
                runOnUiThread {
                    weatherData.city.set(name)
                    weatherData.temp.set(if (!temp.isNaN()) String.format(Locale.getDefault(), "%.1f °C", temp) else "")
                    weatherData.description.set(desc.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                    weatherData.humidity.set(if (humidity >= 0) "Влажность: $humidity%" else "")
                    weatherData.wind.set(if (!windSpeed.isNaN()) "Ветер: ${windSpeed} м/с ${if (windDeg >= 0) "($windDeg°)" else ""}" else "")
                    weatherData.iconCode.set(icon)
                }

            } catch (t: Throwable) {
                t.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Ошибка: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}
