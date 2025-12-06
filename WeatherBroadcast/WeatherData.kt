package com.example.widgetsdemo2728

import androidx.databinding.ObservableField

class WeatherData {
    val city = ObservableField<String>("")
    val temp = ObservableField<String>("")
    val description = ObservableField<String>("")
    val humidity = ObservableField<String>("")
    val wind = ObservableField<String>("")
    val iconCode = ObservableField<String>("")
    val unitSymbol = ObservableField<String>("°C")
}
