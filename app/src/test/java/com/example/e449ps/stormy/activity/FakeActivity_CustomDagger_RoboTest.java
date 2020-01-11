package com.example.e449ps.stormy.activity;

import com.example.e449ps.stormy.BaseRobolectricTest;
import com.example.e449ps.stormy.ForecastService;
import com.example.e449ps.stormy.dagger.Dagger;
import com.example.e449ps.stormy.dagger.DaggerTestStormComponent;
import com.example.e449ps.stormy.dagger.StormComponent;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class FakeActivity_CustomDagger_RoboTest extends BaseRobolectricTest {
    private ForecastService forecastService;
    private static StormComponent myStormComponent;

    @BeforeClass
    public static void setUpOnce() {
        //note order: @BeforeClass, TestApplication, @Before
        //dagger will not run over the test folder therefore custom daggers need to be in mockServices or implemented here
        myStormComponent = Mockito.mock(StormComponent.class, AdditionalAnswers.delegatesTo(DaggerTestStormComponent.create()));
        doReturn(mock(ForecastService.class)).when(myStormComponent).forecastService();
        Dagger.set(myStormComponent);
    }

    @AfterClass
    public static void tearDownOnce() {
        Dagger.set(DaggerTestStormComponent.create());
    }

    @Before
    public void setUp() {
        forecastService = Dagger.get().forecastService();
    }

    @Test
    public void hasTestDagger() {
        forActivity(FakeActivity.class, testObject -> {
            assertNotNull(testObject);
            assertNotNull(Dagger.get());
            assertNotNull(testObject.weatherConverter);
            assertNotNull(testObject.forecastService);
            assertSame(Dagger.get(), myStormComponent);
            assertSame(testObject.forecastService, forecastService);
        });
    }
}
