package com.example.e449ps.stormy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.inputmethod.EditorInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

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
    private FakeActivity testObject;

    @Before
    public void setUp() {
        testObject = Robolectric.setupActivity(FakeActivity.class);
        //TODO: upgrade off of Robolectric.setupActivity
//        androidx.test.core.app.ActivityScenario.launch(MainActivity.class).;//.?(MainActivity.class);
    }

    @Test
    public void onCreate() {
        String givenString = "blah";
        testObject.editText.setText(givenString);

        testObject.editText.onEditorAction(EditorInfo.IME_ACTION_DONE);

        String actual = testObject.textView.getText().toString();
        assertEquals(givenString, actual);
    }

    @Test
    public void spinner_updateBackgroundColor() {
        int index = 2;
        int expected = Color.GREEN;

        testObject.colorSpinner.setSelection(index);

        int actual = ((ColorDrawable) testObject.linearLayout.getBackground()).getColor();
        assertEquals(expected, actual);
    }

    @Test
    public void button_launchesOtherActivity() {
        Intent expected = new Intent(testObject, OtherActivity.class);

        testObject.launchActivityButton.callOnClick();

        Intent actual = Shadows.shadowOf(testObject).getNextStartedActivity();
        assertTrue(expected.filterEquals(actual));
    }

    @Test
    public void hasDagger() {
        assertNotNull(testObject);
        assertNotNull(testObject.weatherConverter);
    }
}
