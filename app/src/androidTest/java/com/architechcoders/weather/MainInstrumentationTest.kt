package com.architechcoders.weather

import android.Manifest
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.architechcoders.weather.matchers.ItemCountAssertion
import com.architechcoders.weather.ui.activities.MainActivity
import com.architechcoders.weather.utils.MockWebServerRule
import com.architechcoders.weather.utils.OkHttp3IdlingResource
import com.architechcoders.weather.utils.fromJson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get: Rule
    val mockWebServerRule = MockWebServerRule()

    @get: Rule
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule
        .grant(Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServerRule.server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val url = request.path!!
                return when {
                    url.contains("q=") -> MockResponse().fromJson("weather_2.json")
                    else -> MockResponse().fromJson("weather.json")
                }
            }
        }
        hiltRule.inject()
        val resource = OkHttp3IdlingResource.create(okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun test_default_location_and_details() {
        Thread.sleep(1000)
        onView(withId(R.id.recycler_weather))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.movie_detail_toolbar)).check(matches(hasDescendant(withText("Warsaw"))))
    }

    @Test
    fun test_add_a_new_city() {
        Thread.sleep(1000)
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(1))
        onView(withId(R.id.add_location)).perform(click())
        onView(withId(R.id.new_location)).perform(typeText("San Luis Potosi"))
        onView(withText("Add")).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(2))
    }

    @Test
    fun attempt_to_duplicate_a_city() {
        Thread.sleep(1000)
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(1))
        onView(withId(R.id.add_location)).perform(click())
        onView(withId(R.id.new_location)).perform(typeText("San Luis Potosi"))
        onView(withText("Add")).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(2))
        onView(withId(R.id.add_location)).perform(click())
        onView(withId(R.id.new_location)).perform(typeText("San Luis Potosi"))
        onView(withText("Add")).perform(click())
        Thread.sleep(1000)
        onView(withText("City already added")).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(2))
    }

    @Test
    fun test_delete_a_city() {
        onView(withId(R.id.add_location)).perform(click())
        onView(withId(R.id.new_location)).perform(typeText("San Luis Potosi"))
        onView(withText("Add")).perform(click())
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(2))
        onView(withId(R.id.recycler_weather))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(withId(R.id.delete)).perform(click())
        onView(withId(R.id.recycler_weather))
            .check(matches(hasDescendant(withText("Warsaw"))))
        onView(withId(R.id.recycler_weather))
            .check(ItemCountAssertion(1))
    }
}