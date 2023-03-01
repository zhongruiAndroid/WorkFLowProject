package com.github.zr.single;

public interface FlowFunction<T,R> {
    void onSuccess(T obj, Observer<? super R> observer);
    default void onError(Throwable throwable, Object object, Observer<? super R> observer){
        if (observer != null) {
            observer.onError(throwable,object);
        }
    }
}
