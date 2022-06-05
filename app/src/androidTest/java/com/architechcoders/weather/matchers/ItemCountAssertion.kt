package com.architechcoders.weather.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers

class ItemCountAssertion(
    private val expected: Int
): ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        checkNotNull(view)
        view as RecyclerView
        assertThat(view.adapter?.itemCount, Matchers.equalTo(expected))
    }
}