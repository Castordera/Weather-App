package com.architechcoders.weather.ui.fragments.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageSrc")
fun ImageView.loadImageFromSrc(src: String?) {
    if (src != null) {
        loadSrc(src)
    }
}