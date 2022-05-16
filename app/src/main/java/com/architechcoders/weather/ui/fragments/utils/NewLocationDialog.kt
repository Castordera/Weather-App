package com.architechcoders.weather.ui.fragments.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.architechcoders.weather.R

class NewLocationDialog(
    private val onSuccess: (String) -> Unit
): DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_new_location, null)
        with(builder) {
            setTitle(R.string.dialog_add_weather_title)
            setView(view)
            setPositiveButton(R.string.dialog_add_weather_button_success) { _ ,_ ->
                val value = (view as? AppCompatEditText)?.text?.toString()?.trim() ?: ""
                onSuccess(value)
            }
            setNegativeButton(R.string.cancel, null)
        }
        return builder.create()
    }

    companion object {
        const val TAG = "tag-dialog-add-new-location"
    }
}