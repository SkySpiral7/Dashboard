package com.example.e449ps.stormy.activity;

import android.app.Activity;
import android.os.Build;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.function.Function;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ActivityScenario.ActivityAction;

/*
[Robolectric] NOTICE: legacy resources mode is deprecated
This is impossible to resolve in Android Studio 3.2 and shouldn't print out in 3.3+.
*/
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.M)
public abstract class BaseRobolectricTest {
    protected <A extends Activity> void forActivity(Class<A> activityClass, ActivityAction<A> action) {
        try (ActivityScenario<A> scenario = ActivityScenario.launch(activityClass)) {
            scenario.onActivity(action);
        }
    }
}
