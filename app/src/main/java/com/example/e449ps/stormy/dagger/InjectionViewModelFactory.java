package com.example.e449ps.stormy.dagger;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;

/**
 * Not limited to dagger. May work with other Providers.
 */
@Singleton
public final class InjectionViewModelFactory implements Factory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public InjectionViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            creator = findInheritedProvider(modelClass);
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        return (T) creator.get();
    }

    /**
     * If this method is being called then it probably won't work since your ViewModel should only be
     * extending ViewModel.
     */
    private <T extends ViewModel> Provider<? extends ViewModel> findInheritedProvider(
            @NotNull Class<T> modelClass) {
        for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
            if (modelClass.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
