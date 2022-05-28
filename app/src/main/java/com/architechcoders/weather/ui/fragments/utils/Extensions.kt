package com.architechcoders.weather.ui.fragments.utils

import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.architechcoders.domain.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

inline fun <T> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsTheSame(oldItem, newItem)
}

fun ImageView.loadSrc(url: String) {
    this.load(url)
}

fun LifecycleOwner.launchOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            action()
        }
    }
}

fun <T, R> LifecycleOwner.distinctCollect(
    flow: Flow<T>,
    transform: (T) -> R,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    result: (R) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.map { transform(it) }.distinctUntilChanged().collect(result)
        }
    }
}

fun Weather.shouldUpdate(minMinutes: Int = 30): Boolean {
    val time = Instant.ofEpochMilli(timeRequested)
    val currentTime = Instant.now()
    val dif = Duration.between(time, currentTime)
    return TimeUnit.MINUTES.convert(dif.seconds, TimeUnit.SECONDS) >= minMinutes
}