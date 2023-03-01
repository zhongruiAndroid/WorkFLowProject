package com.github.zr.single;

public interface Observer<T> {
    void onSuccess(T obj);
    void onError(Throwable throwable,Object object);
}
