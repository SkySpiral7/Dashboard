package com.example.e449ps.stormy.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.e449ps.stormy.R;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(JUnit4.class)
public class FakeActivityUiTest {
    @Rule
    public ActivityTestRule<FakeActivity> rule = new ActivityTestRule<>(FakeActivity.class);

    @Test
    public void editTextUpdatesTextView() {
        // Arrange
        String givenString = "test123";

        // Act
        ViewInteraction editText = onView(withId(R.id.editText));
        editText.perform(typeText(givenString));
        editText.perform(pressImeActionButton());

        // Assert
        onView(withId(R.id.textView)).check(matches(withText(givenString)));
    }

    @Test
    public void spinnerUpdatesBackgroundColor() {
        // Act
        final int expectedColor = Color.GREEN;
        onView(withId(R.id.colorSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Green"))).perform(click());

        // Assert
        BoundedMatcher<View, LinearLayout> backgroundColorMatcher = new BoundedMatcher<View, LinearLayout>(LinearLayout.class) {
            @Override
            protected boolean matchesSafely(LinearLayout item) {
                int actual = ((ColorDrawable) item.getBackground()).getColor();
                return expectedColor == actual;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("background color should be " + expectedColor);
            }
        };
        onView(withId(R.id.linearLayout)).check(matches(backgroundColorMatcher));
    }

    @Test
    public void buttonLaunchesOtherActivity() {
        // Act
        onView(withId(R.id.launchActivityButton)).perform(click());

        // Assert
        onView(withId(R.id.otherText)).check(matches(notNullValue()));
    }
}
