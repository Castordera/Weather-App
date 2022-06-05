package com.architechcoders.weather.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.architechcoders.weather.data.server.*
import okhttp3.mockwebserver.MockResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import com.architechcoders.domain.Weather as WeatherDomain
import com.architechcoders.weather.data.database.Weather as WeatherEntity

fun MockResponse.fromJson(jsonFile: String): MockResponse =
    setBody(readJsonFile(jsonFile))

private fun readJsonFile(jsonFilePath: String): String {
    val context = InstrumentationRegistry.getInstrumentation().context

    var br: BufferedReader? = null

    try {
        br = BufferedReader(
            InputStreamReader(
                context.assets.open(
                    jsonFilePath
                ), StandardCharsets.UTF_8
            )
        )
        var line: String?
        val text = StringBuilder()

        do {
            line = br.readLine()
            line?.let { text.append(line) }
        } while (line != null)
        br.close()
        return text.toString()
    } finally {
        br?.close()
    }
}