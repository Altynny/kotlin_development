package com.example.workmanager

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf


class MainActivity : AppCompatActivity() {
    private val workManager by lazy { WorkManager.getInstance(this) }

    private lateinit var tvCity1: TextView
    private lateinit var tvCity2: TextView
    private lateinit var tvCity3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCity1 = findViewById(R.id.tvCity1)
        tvCity2 = findViewById(R.id.tvCity2)
        tvCity3 = findViewById(R.id.tvCity3)

        val request1 = createWeatherRequest("Иркутск")
        val request2 = createWeatherRequest("Москва")
        val request3 = createWeatherRequest("Санкт-Петербург")

        observeRequest(request1.id, tvCity1)
        observeRequest(request2.id, tvCity2)
        observeRequest(request3.id, tvCity3)

        workManager.beginWith(request1)
            .then(request2)
            .then(request3)
            .enqueue()
    }

    private fun createWeatherRequest(city: String) =
        OneTimeWorkRequestBuilder<WeatherWorker>()
            .setInputData(workDataOf(WeatherWorker.KEY_CITY to city))
            .build()

    private fun observeRequest(requestId: java.util.UUID, targetView: TextView) {
        workManager.getWorkInfoByIdLiveData(requestId).observe(this) { info ->
            if (info == null) return@observe

            when (info.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> {
                    targetView.text = getString(R.string.loading)
                }

                WorkInfo.State.SUCCEEDED -> {
                    targetView.text = info.outputData.getString(WeatherWorker.KEY_RESULT)
                        ?: getString(R.string.no_data)
                }

                WorkInfo.State.FAILED -> {
                    targetView.text = info.outputData.getString(WeatherWorker.KEY_RESULT)
                        ?: getString(R.string.loading_error)
                }

                else -> Unit
            }
        }
    }
}