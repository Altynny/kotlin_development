package com.example.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_CITY = "KEY_CITY"
        const val KEY_RESULT = "KEY_RESULT"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val cityName = inputData.getString(KEY_CITY) ?: return@withContext Result.failure()
            val apiKey = applicationContext.getString(R.string.openweather_api_key).trim()

            val units = "metric"
            val lang = "ru"

            val q = URLEncoder.encode(cityName, "UTF-8")
            val urlStr = "https://api.openweathermap.org/data/2.5/weather?q=$q&appid=$apiKey&units=$units&lang=$lang"

            val conn = (URL(urlStr).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10_000
                readTimeout = 10_000
            }

            val code = conn.responseCode
            val stream = if (code == 200) conn.inputStream else conn.errorStream
            val body = stream.bufferedReader().use { it.readText() }

            if (code != 200) {
                return@withContext Result.failure(
                    workDataOf(KEY_RESULT to applicationContext.getString(
                        R.string.city_weather_fetch_error,
                        code,
                        cityName
                    ))
                )
            }

            val json = JSONObject(body)

            val name = json.optString("name", cityName)
            val main = json.optJSONObject("main")
            val temp = main?.optDouble("temp", Double.NaN) ?: Double.NaN
            val humidity = main?.optInt("humidity", -1) ?: -1

            val weatherArr = json.optJSONArray("weather")
            val desc =
                if (weatherArr != null && weatherArr.length() > 0) {
                    weatherArr.getJSONObject(0).optString("description", "")
                } else ""

            val text = buildString {
                append(name)
                append("\n")
                append(
                    if (!temp.isNaN()) String.format(Locale.getDefault(), "%.1f °C", temp)
                    else ""
                )
                append("\n")
                append(desc.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                })
                append("\n")
                append(applicationContext.getString(R.string.humidity,  humidity))
            }

            Result.success(workDataOf(KEY_RESULT to text))
        } catch (t: Throwable) {
            Result.failure(workDataOf(KEY_RESULT to applicationContext.getString(
                R.string.error,
                t.localizedMessage
            )))
        }
    }
}