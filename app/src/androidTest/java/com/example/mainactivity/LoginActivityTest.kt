package com.example.mainactivity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mainactivity.activities.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginActivity_DisplayedElements() {
        onView(withId(R.id.login_title_textView)).check(matches(isDisplayed()))
        onView(withId(R.id.username_textinputlayout)).check(matches(isDisplayed()))
        onView(withId(R.id.password_textinputlayout)).check(matches(isDisplayed()))
        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        onView(withId(R.id.register_button)).check(matches(isDisplayed()))
    }

    @Test
    fun loginActivity_ValidLogin() {
        onView(withId(R.id.login_username_edittext)).perform(typeText("validUsername"), closeSoftKeyboard())
        onView(withId(R.id.login_password_edittext)).perform(typeText("validPassword"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())

        // Verify that the app navigates to the main activity or shows a success message
    }

    @Test
    fun loginActivity_InvalidLogin() {
        onView(withId(R.id.login_username_edittext)).perform(typeText("invalidUsername"), closeSoftKeyboard())
        onView(withId(R.id.login_password_edittext)).perform(typeText("invalidPassword"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())

        // Verify that the app shows an error message or stays on the login screen
    }

    @Test
    fun loginActivity_NavigateToRegister() {
        onView(withId(R.id.register_button)).perform(click())

        // Verify that the app navigates to the register activity
    }

}