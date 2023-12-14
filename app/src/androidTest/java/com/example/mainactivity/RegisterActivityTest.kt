package com.example.mainactivity

import android.view.View
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import android.widget.TextView
import com.example.mainactivity.activities.RegistrationActivity
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegistrationActivity::class.java)

    @Test
    fun registrationActivity_DisplayedElements() {
        onView(withId(R.id.registration_title_textView)).check(matches(isDisplayed()))
        onView(withId(R.id.reg_usermame_textinput)).check(matches(isDisplayed()))
        onView(withId(R.id.reg_password_textinput)).check(matches(isDisplayed()))
        onView(withId(R.id.reg_retype_pass_textinput)).check(matches(isDisplayed()))
        onView(withId(R.id.student_faculty_switch)).check(matches(isDisplayed()))
        onView(withId(R.id.faculty_label)).check(matches(isDisplayed()))
        onView(withId(R.id.student_label)).check(matches(isDisplayed()))
        onView(withId(R.id.reg_register_button)).check(matches(isDisplayed()))
        onView(withId(R.id.existing_user_textView)).check(matches(isDisplayed()))
        onView(withId(R.id.reg_login_button)).check(matches(isDisplayed()))
    }

    @Test
    fun registrationActivity_InteractionWithSwitch() {
        // Initial state should be 'Student'
        onView(withId(R.id.student_label)).check(matches(withTextColor(R.color.PA_Creek)))
        onView(withId(R.id.faculty_label)).check(matches(withTextColor(R.color.White_Out)))

        // Click the switch to change to 'Faculty'
        onView(withId(R.id.student_faculty_switch)).perform(click())

        // Check colors are updated
        onView(withId(R.id.faculty_label)).check(matches(withTextColor(R.color.PA_Creek)))
        onView(withId(R.id.student_label)).check(matches(withTextColor(R.color.White_Out)))

        // Click again to change back to 'Student'
        onView(withId(R.id.student_faculty_switch)).perform(click())

        // Check colors are reverted
        onView(withId(R.id.student_label)).check(matches(withTextColor(R.color.PA_Creek)))
        onView(withId(R.id.faculty_label)).check(matches(withTextColor(R.color.White_Out)))
    }

    @Test
    fun registrationActivity_CompleteFormAndRegister() {
        onView(withId(R.id.reg_usermame_textinput)).perform(typeText("NewUser"), closeSoftKeyboard())
        onView(withId(R.id.reg_password_textinput)).perform(typeText("Password123"), closeSoftKeyboard())
        onView(withId(R.id.reg_retype_pass_textinput)).perform(typeText("Password123"), closeSoftKeyboard())
        onView(withId(R.id.reg_register_button)).perform(click())
    }

    @Test
    fun registrationActivity_NavigateBackToLogin() {
        onView(withId(R.id.reg_login_button)).perform(click())
    }

    // Custom matcher for checking text color
    fun withTextColor(expectedColor: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with text color: ")
                description.appendValue(expectedColor)
            }

            override fun matchesSafely(view: View): Boolean {
                val textView = view as TextView
                val colorId = ContextCompat.getColor(view.context, expectedColor)
                return textView.currentTextColor == colorId
            }
        }
    }
}
