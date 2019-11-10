package com.example.e449ps.stormy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.inputmethod.EditorInfo;

import org.junit.Test;
import org.robolectric.Shadows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FakeActivityUnitTest extends BaseRobolectricTest {
    @Test
    public void editTextUpdatesTextView() {
        forActivity(FakeActivity.class, testObject -> {
            String givenString = "blah";
            testObject.editText.setText(givenString);

            testObject.editText.onEditorAction(EditorInfo.IME_ACTION_DONE);

            String actual = testObject.textView.getText().toString();
            assertEquals(givenString, actual);
        });
    }

    @Test
    public void spinnerUpdateBackgroundColor() {
        forActivity(FakeActivity.class, testObject -> {
            int index = 2;
            int expected = Color.GREEN;

            testObject.colorSpinner.setSelection(index);

            int actual = ((ColorDrawable) testObject.linearLayout.getBackground()).getColor();
            assertEquals(expected, actual);
        });
    }

    @Test
    public void buttonLaunchesOtherActivity() {
        forActivity(FakeActivity.class, testObject -> {
            Intent expected = new Intent(testObject, OtherActivity.class);

            testObject.launchActivityButton.callOnClick();

            Intent actual = Shadows.shadowOf(testObject).getNextStartedActivity();
            assertTrue(expected.filterEquals(actual));
        });
    }

    @Test
    public void hasDagger() {
        forActivity(FakeActivity.class, testObject -> {
            assertNotNull(testObject);
            assertNotNull(testObject.weatherConverter);
        });
    }
}
