package com.github.zr.single.listener;

import com.github.zr.single.Observer;

public abstract class Fun2<T> implements Observer<T> {
    @Override
    public void onNext(T obj) {
    }
    @Override
    public abstract void onComplete(Object obj);

    @Override
    public void onError(Throwable throwable, Object obj) {
    }
}
