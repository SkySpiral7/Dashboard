package com.example.e449ps.stormy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.inputmethod.EditorInfo;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.StormComponent;
import com.example.e449ps.stormy.dagger.TestStormModule;

import org.junit.BeforeClass;
import org.junit.Test;
import org.robolectric.Shadows;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FakeActivityUnitTest extends BaseRobolectricTest {
    @Module
    public static class DoubleTestStormModule {
        @Provides
        @Singleton
        public static ForecastService forecastService() {
            return mock(ForecastService.class);
        }
    }

    @Singleton
    @Component(modules = {TestStormModule.class, DoubleTestStormModule.class})
    public interface DoubleTestStormComponent extends StormComponent {
    }

    @BeforeClass
    public static void setUpOnce() {
        //note order: @BeforeClass, TestApplication, @Before
        System.out.println();

        //TODO: test overriding the service
        //Dagger.set(DaggerFakeActivityUnitTest_DoubleTestStormComponent.create());
        Dagger.set(mock(StormComponent.class));
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
    public void hasDagger() {
        forActivity(FakeActivity.class, testObject -> {
            assertNotNull(testObject);
            assertNotNull(testObject.weatherConverter);
        });
    }

    @Test
    public void hasTestDagger() {
        //TODO: make these real tests
        forActivity(FakeActivity.class, testObject -> {
            testObject.useService();
        });
    }
}
