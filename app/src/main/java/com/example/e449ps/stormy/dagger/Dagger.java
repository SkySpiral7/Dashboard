package com.example.e449ps.stormy.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

// See https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9 for Dagger tips
// TODO: use dagger reflect for IDE builds (might not support MapKey): https://www.youtube.com/watch?v=6eOyCEkQ5zQ&t=37m53s
public final class Dagger {
    //yes this is static state but this is easier than using Application
    private static StormComponent INSTANCE;

    private Dagger() {
    }

    public static StormComponent get() {
        return INSTANCE;
    }

    /**
     * Convenience method for getting a ViewModel.
     * This uses the ViewModelProvider.Factory from the Component.
     * Will use the current Component instance which may be prod or test.
     */
    public static <T extends ViewModel> T getViewModel(ViewModelStoreOwner activityOrFragment, Class<T> viewModelClass) {
        ViewModelStore viewModelStore = activityOrFragment.getViewModelStore();
        InjectionViewModelFactory factory = INSTANCE.injectionViewModelFactory();
        return new ViewModelProvider(viewModelStore, factory).get(viewModelClass);
    }

    public static void set(StormComponent component) {
        INSTANCE = component;
    }
}
