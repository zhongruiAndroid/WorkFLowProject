package com.github.zr.single;

public interface Observer<T> {
    void onNext(T obj);
    void onComplete(Object obj);
    void onError(int code,String msg);
}
