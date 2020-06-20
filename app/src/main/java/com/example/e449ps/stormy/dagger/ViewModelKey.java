package com.example.e449ps.stormy.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.lifecycle.ViewModel;
import dagger.MapKey;
import dagger.multibindings.ClassKey;

/**
 * {@link ClassKey} could be used instead by changing
 * {@link InjectionViewModelFactory} to use {@code Map<Class<?>, Provider<?>>}.
 * But might as well get a slight benefit instead since the boilerplate annotation is small.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
@interface ViewModelKey {
    Class<? extends ViewModel> value();
}
