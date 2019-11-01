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
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.N)
public class FakeActivityUnitTest {
    private FakeActivity testObject;
/*TODO: legacy resources mode is deprecated
[Robolectric] com.example.e449ps.stormy.activity.FakeActivityUnitTest.spinner_updateBackgroundColor: sdk=24; resources=LEGACY
[Robolectric] NOTICE: legacy resources mode is deprecated; see http://robolectric.org/migrating/#migrating-to-40
*/

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
}
