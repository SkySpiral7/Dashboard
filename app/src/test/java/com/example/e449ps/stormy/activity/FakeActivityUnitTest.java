package com.example.e449ps.stormy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.inputmethod.EditorInfo;

import com.example.e449ps.stormy.BaseRobolectricTest;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.TestStormComponent;

import org.junit.BeforeClass;
import org.junit.Test;
import org.robolectric.Shadows;

import okhttp3.OkHttpClient;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FakeActivityUnitTest extends BaseRobolectricTest {
    private OkHttpClient client = Dagger.get().okHttpClient();

    @BeforeClass
    public static void setUpOnce() {
        //note order: @BeforeClass, TestApplication, @Before
        //dagger will not run over the test folder therefore custom daggers need to be in mockServices or implemented here
        System.out.println();

        //TODO: test overriding the service
//        Dagger.set(DaggerTestStormComponent.create());
//        Dagger.set(mock(StormComponent.class));
    }

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
    public void hasTestDagger() {
        forActivity(FakeActivity.class, testObject -> {
            assertNotNull(testObject);
            assertNotNull(Dagger.get());
            assertNotNull(testObject.weatherConverter);
            assertNotNull(testObject.forecastService);
            assertThat(Dagger.get(), is(instanceOf(TestStormComponent.class)));
            assertNotNull(testObject.forecastService.client);
            assertSame(testObject.forecastService.client, client);
        });
    }
}
