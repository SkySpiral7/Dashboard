package com.example.e449ps.stormy.activity;

import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.DaggerTestStormComponent;
import com.example.e449ps.stormy.dagger.StormComponent;
import com.example.e449ps.stormy.dagger.TestStormModule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Singleton;

import androidx.test.rule.ActivityTestRule;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(JUnit4.class)
public class FakeActivity_CustomDaggerUiTest {
    @Rule
    public ActivityTestRule<FakeActivity> rule = new ActivityTestRule<>(FakeActivity.class);

    private ForecastService forecastService = Dagger.get().forecastService();

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
        //note order: TestApplication, @BeforeClass, @Before
        Dagger.set(DaggerFakeActivity_CustomDaggerUiTest_DoubleTestStormComponent.create());
    }

    @AfterClass
    public static void tearDownOnce() {
        Dagger.set(DaggerTestStormComponent.create());
    }

    @Test
    public void hasTestDagger() {
        FakeActivity testObject = rule.getActivity();
        assertNotNull(testObject);
        assertNotNull(Dagger.get());
        assertNotNull(testObject.weatherConverter);
        assertThat(Dagger.get(), is(instanceOf(DaggerFakeActivity_CustomDaggerUiTest_DoubleTestStormComponent.class)));
        assertNotNull(testObject.forecastService);
        assertSame(testObject.forecastService, forecastService);
    }
}
