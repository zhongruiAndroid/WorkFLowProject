package com.github.zr.single.listener;

import com.github.zr.single.Observer;

public abstract class Fun3<T> implements Observer<T> {
    @Override
    public void onNext(T obj) {
    }
    @Override
    public void onComplete(Object obj) {
    }
    @Override
    public abstract void onError(Throwable throwable, Object obj);
}
