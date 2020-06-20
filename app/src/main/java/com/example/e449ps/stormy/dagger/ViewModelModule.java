package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.activity.MainViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Every ViewModel created will need to have a binds method to populate the map.
 */
//TODO: might be able to generate VM map: https://youtu.be/6eOyCEkQ5zQ?t=28m49s https://github.com/square/AssistedInject
@Module
abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(InjectionViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel repoViewModel);
}
