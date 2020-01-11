package com.example.e449ps.stormy;

import android.app.Activity;
import android.os.Build;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ActivityScenario.ActivityAction;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.N)
public abstract class BaseRobolectricTest {
    protected <A extends Activity> void forActivity(Class<A> activityClass, ActivityAction<A> action) {
        try (ActivityScenario<A> scenario = ActivityScenario.launch(activityClass)) {
            scenario.onActivity(action);
        }
    }
}
