package com.example.e449ps.stormy;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MockableSchedulers {
    @Inject
    public MockableSchedulers() {
    }

    /**
     * @return Android's main UI thread. Only use this for UI work.
     */
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * @return for IO work such as file IO or network IO.
     */
    public Scheduler io() {
        return Schedulers.io();
    }

    /**
     * @return a background (non-UI) thread for anything that is not IO
     */
    public Scheduler background() {
        return Schedulers.computation();
    }
}
