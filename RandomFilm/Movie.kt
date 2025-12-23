package com.example.randomfilm

import org.json.JSONObject

data class Movie(
    val title: String,
    val year: Int,
    val director: String,
    val genres: List<String>,
    val rating: Double,
    val description: String
)

{
    companion object {
        fun fromJson(obj: JSONObject): Movie {
            val title = obj.optString("title", "Unknown")
            val year = obj.optInt("year", 0)
            val director = obj.optString("director", "")
            val genres = mutableListOf<String>()
            val gArr = obj.optJSONArray("genres")
            if (gArr != null) {
                for (i in 0 until gArr.length()) {
                    genres.add(gArr.optString(i))
                }
            }
            val rating = obj.optDouble("rating", 0.0)
            val description = obj.optString("description", "")
            return Movie(title, year, director, genres, rating, description)
        }
    }
}
