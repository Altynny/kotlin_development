import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import com.google.gson.Gson

class Weather (val main: Main)
class Main (val temp: Double)

fun main() {
    val API_KEY = "d6843ab8ee963f5d372296dfff62aed7"
    val cities = mapOf(
        2016764 to "Шелехов", 2023469 to "Иркутск", 524901 to "Москва",
        498817 to "Санкт-Петербург", 2013348 to "Владивосток", 1496747 to "Новосибирск",
        491422 to "Сочи", 582182 to "Анапа", 2025527 to "Черемхово", 2055166 to "Саянск")
    val temps = mutableMapOf<String, Double>()
    for ((id, name) in cities) {
        val weather_url =
            "http://api.openweathermap.org/data/2.5/weather?id=$id&appid=$API_KEY"
        println(weather_url)
        val url = URL(weather_url)
        val stream = url.getContent() as InputStream
        val gson = Gson()
        val weather:Weather =
            gson.fromJson(InputStreamReader(stream), Weather::class.java) // указать ссылку на тип данных (класс)
        println(weather.main.temp)
        temps[name] = weather.main.temp
        Thread.sleep(200)
    }
    for ((city, temp) in temps.toList().sortedBy { -it.second }) {
        println("$city, температура - ${temp}K")
    }
}