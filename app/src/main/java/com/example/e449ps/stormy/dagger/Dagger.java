package com.example.e449ps.stormy.dagger;

//See https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9 for Dagger tips
public final class Dagger {
    private static StormComponent INSTANCE;

    private Dagger() {
    }

    public static StormComponent get() {
        return INSTANCE;
    }

    public static void set(StormComponent component) {
        INSTANCE = component;
    }
}
