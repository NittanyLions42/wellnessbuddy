package com.example.mainactivity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mainactivity.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun textView_recommendActAreaTitle_isDisplayed() {
        onView(withId(R.id.recommend_act_area_title_textView))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.recommended_activity_area_title)))
    }

    @Test
    fun testImageView_isDisplayed() {
        onView(withId(R.id.recommend_activity_imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testShortDescription_isDisplayed() {
        onView(withId(R.id.activity_short_desc_textView))
            .check(matches(withText(R.string.recommended_activity_short_desc_text)))
    }

    @Test
    fun testMultilineEditText_isDisplayed() {
        onView(withId(R.id.editTextTextMultiLine)).check(matches(isDisplayed()))
    }

    @Test
    fun testButton_isDisplayedAndClickable() {
        onView(withId(R.id.generate_rand_act_button)).check(matches(isDisplayed()))
        onView(withId(R.id.generate_rand_act_button)).perform(click())
    }

    @Test
    fun textView_forecastCardTitle_isDisplayed() {
        onView(withId(R.id.forecast_card_title_textview))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.weather_card_title)))
    }

    @Test
    fun editTextAndButtonInteractionTest() {
        onView(withId(R.id.zipcode_editTextNumber)).perform(typeText("12345"), closeSoftKeyboard())
        onView(withId(R.id.zipcode_enter_button)).perform(click())
    }

    @Test
    fun recyclerViewDisplayAndInteractionsTest() {
        onView(withId(R.id.horizontal_card_recyclerview)).check(matches(isDisplayed()))
        onView(withId(R.id.horizontal_card_recyclerview))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
    }

    @Test
    fun toolbarLogo_isDisplayed() {
        onView(withId(R.id.toolbarLogo))
            .check(matches(isDisplayed()))
    }

    @Test
    fun logoutButtonClickTest() {
        onView(withId(R.id.logoutButton)).perform(click())
    }

}
