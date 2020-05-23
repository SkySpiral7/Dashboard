package com.example.e449ps.stormy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class VerboseTagTree extends Timber.DebugTree {
    private final int minLogLevel;

    //for specific log levels: https://stackoverflow.com/questions/2018263/how-do-i-enable-disable-log-levels-in-android/2019563#2019563
    public VerboseTagTree(int minLogLevel) {
        this.minLogLevel = minLogLevel;
    }

    //TODO: try log4j2. for date format, better overhead without using tag, and specific loggers without using tag
    //https://stackoverflow.com/questions/28235021/log4j2-on-android except -Dlog4j.configurationFile=path/to/log4j2.xml

    @NotNull
    @Override
    protected String createStackElementTag(@NotNull StackTraceElement element) {
        //this includes anon names. which is probably better anyway since I have no tag limit
        return element.getClassName() + "." + element.getMethodName() + "():" + element.getLineNumber();
    }

    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        return priority >= minLogLevel;
    }
}
