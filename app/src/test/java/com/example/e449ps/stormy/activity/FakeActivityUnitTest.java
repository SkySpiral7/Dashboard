package com.example.e449ps.stormy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.inputmethod.EditorInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
[Robolectric] NOTICE: legacy resources mode is deprecated
This is impossible to resolve in Android Studio 3.2 and shouldn't print out in 3.3+.
*/
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.M)
public class FakeActivityUnitTest {
    @Test
    public void onCreate() {
        //TODO: can this boilerplate be put in a base class?
        try (ActivityScenario<FakeActivity> scenario = ActivityScenario.launch(FakeActivity.class)) {
            scenario.onActivity(testObject -> {
                String givenString = "blah";
                testObject.editText.setText(givenString);

                testObject.editText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                String actual = testObject.textView.getText().toString();
                assertEquals(givenString, actual);
            });
        }
    }

    @Test
    public void spinner_updateBackgroundColor() {
        try (ActivityScenario<FakeActivity> scenario = ActivityScenario.launch(FakeActivity.class)) {
            scenario.onActivity(testObject -> {
                int index = 2;
                int expected = Color.GREEN;

                testObject.colorSpinner.setSelection(index);

                int actual = ((ColorDrawable) testObject.linearLayout.getBackground()).getColor();
                assertEquals(expected, actual);
            });
        }
    }

    @Test
    public void button_launchesOtherActivity() {
        try (ActivityScenario<FakeActivity> scenario = ActivityScenario.launch(FakeActivity.class)) {
            scenario.onActivity(testObject -> {
                Intent expected = new Intent(testObject, OtherActivity.class);

                testObject.launchActivityButton.callOnClick();

                Intent actual = Shadows.shadowOf(testObject).getNextStartedActivity();
                assertTrue(expected.filterEquals(actual));
            });
        }
    }

    @Test
    public void hasDagger() {
        try (ActivityScenario<FakeActivity> scenario = ActivityScenario.launch(FakeActivity.class)) {
            scenario.onActivity(testObject -> {
                assertNotNull(testObject);
                assertNotNull(testObject.weatherConverter);
            });
        }
    }
}
