package com.example.e449ps.stormy.dagger;

public final class Dagger {
    private static StormComponent INSTANCE;

    private Dagger() {
        //TODO: is this really needed? clean up all dagger
    }

    public static StormComponent getDagger() {
        return INSTANCE;
    }

    public static void setDagger(StormComponent component) {
        INSTANCE = component;
    }
}
