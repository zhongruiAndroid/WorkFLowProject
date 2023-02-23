package com.github.zr.single.listener;

import com.github.zr.single.Observer;

public abstract class Fun1<T> implements Observer<T> {
    @Override
    public abstract void onNext(T obj);

    @Override
    public void onComplete(Object obj) {
    }

    @Override
    public void onError(Throwable throwable, Object obj) {
    }
}
