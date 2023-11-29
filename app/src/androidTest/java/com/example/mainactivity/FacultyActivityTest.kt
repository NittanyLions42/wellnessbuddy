package com.example.mainactivity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.mainactivity.activities.FacultyActivity
import org.junit.Rule
import org.junit.Test

class FacultyActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(FacultyActivity::class.java)

    @Test
    fun forecastCardTitle_isDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.forecast_faculty_card_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.weather_card_title)))
    }

    @Test
    fun zipCodeText_and_enterBtn_Test() {
        Espresso.onView(ViewMatchers.withId(R.id.zipcode_faculty))
            .perform(ViewActions.typeText("12345"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.zipcode_faculty_enter_button)).perform(ViewActions.click())
        // Assertions or verifications after button click
    }

    @Test
    fun recyclerViewDisplayAndInteractionsTest() {
        Espresso.onView(ViewMatchers.withId(R.id.horizontal_faculty_card_recyclerview))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.horizontal_faculty_card_recyclerview))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
    }

    @Test
    fun toolbarLogo_isDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.toolbarLogo_faculty))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun logoutButtonClickTest() {
        Espresso.onView(ViewMatchers.withId(R.id.logoutButton_faculty)).perform(ViewActions.click())
        // Assertions to verify logout behavior, like returning to the login screen
        // For example, if logout navigates to a login screen:
        // onView(withId(R.id.login_screen_id)).check(matches(isDisplayed()))
    }
}