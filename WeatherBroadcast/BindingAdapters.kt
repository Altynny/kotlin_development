package com.example.widgetsdemo2728

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.net.URL

@BindingAdapter("iconCode")
fun bindIcon(imageView: ImageView, iconCode: String?) {
    if (iconCode.isNullOrEmpty()) {
        imageView.setImageDrawable(null)
        return
    }
    // Загружаем в фоновом потоке и ставим через post()
    Thread {
        try {
            val url = URL("https://openweathermap.org/img/wn/${iconCode}@2x.png")
            val conn = url.openConnection()
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            val stream = conn.getInputStream()
            val bmp = BitmapFactory.decodeStream(stream)
            imageView.post {
                imageView.setImageBitmap(bmp)
            }
            stream.close()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }.start()
}
