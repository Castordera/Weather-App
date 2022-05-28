package com.architechcoders.weather.ui.fragments.detail

import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.scale
import androidx.databinding.BindingAdapter
import com.architechcoders.domain.Weather
import com.architechcoders.weather.R
import java.time.*
import java.time.format.DateTimeFormatter

@BindingAdapter("lastUpdate")
fun TextView.setLastUpdate(time: Long) {
    if (time <= 0) return
    val current = ZonedDateTime.now(ZoneId.systemDefault())
    val timeInstance = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
    val diff = Duration.between(timeInstance, current)
    text = if (diff.toHours() > 1 || diff.toMinutes() >= 30) {
        val pattern = DateTimeFormatter.ofPattern("dd/MM/yyy 'at' HH:mm")
        context.getString(R.string.detail_weather_last_update, pattern.format(timeInstance))
    } else {
        context.resources.getQuantityString(
            R.plurals.detail_weather_last_update_minutes,
            diff.toMinutes().toInt(),
            diff.toMinutes()
        )
    }
}

@BindingAdapter("timezone", "sunValue", requireAll = true)
fun TextView.setSunriseSunset(timezone: Int, sunValue: Long) {
    if (sunValue <= 0) return
    val format = DateTimeFormatter.ofPattern("HH:mm")
    val time = Instant.ofEpochSecond(sunValue).atOffset(ZoneOffset.ofTotalSeconds(timezone))
    text = format.format(time)
}

@BindingAdapter("weatherDetails")
fun TextView.setDetails(weather: Weather?) {
    if (weather == null) return
    text = buildSpannedString {
        scale(1.5f) {
            appendLine(context.getString(R.string.detail_weather_detail))
        }
        appendLine(context.getString(R.string.detail_weather_detail_wind, String.format("%.1f", weather.windSpeed)))
        appendLine(context.getString(R.string.detail_weather_detail_humidity, String.format("%d", weather.humidity)))
        appendLine(context.getString(R.string.detail_weather_detail_pressure, String.format("%d", weather.pressure)))
    }
}