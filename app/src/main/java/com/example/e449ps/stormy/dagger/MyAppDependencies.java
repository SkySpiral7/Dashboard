package com.example.e449ps.stormy.dagger;

import com.example.e449ps.stormy.ConnectionFacade;

public interface MyAppDependencies {
    ConnectionFacade connectionFacade();

    InjectionViewModelFactory injectionViewModelFactory();
}
