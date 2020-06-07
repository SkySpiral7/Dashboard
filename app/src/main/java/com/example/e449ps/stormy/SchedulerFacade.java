package com.example.e449ps.stormy;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SchedulerFacade {
    private final MockableSchedulers mockableSchedulers;

    @Inject
    public SchedulerFacade(MockableSchedulers mockableSchedulers) {
        this.mockableSchedulers = mockableSchedulers;
    }

    /**
     * @return schedule this Single to work on the IO thread and respond on the UI thread.
     */
    public <T> Single<T> ioToUi(Single<T> single) {
        return single.subscribeOn(mockableSchedulers.io()).observeOn(mockableSchedulers.ui());
    }

    /**
     * @return schedule this Single to work on the IO thread and respond on a background thread.
     */
    public <T> Observable<T> ioToBackground(Observable<T> observable) {
        return observable.subscribeOn(mockableSchedulers.io()).observeOn(mockableSchedulers.background());
    }
}
