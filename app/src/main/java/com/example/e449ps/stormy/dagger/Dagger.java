package com.example.e449ps.stormy.dagger;

public final class Dagger {
    private static ConditionComponent INSTANCE;

    private Dagger() {
    }

    public static ConditionComponent getDagger() {
        return INSTANCE;
    }

    public static void setDagger(ConditionComponent component) {
        INSTANCE = component;
    }
}
