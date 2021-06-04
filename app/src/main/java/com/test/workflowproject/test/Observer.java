package com.test.workflowproject.test;

public interface Observer<T> {
    void onNext(T t);
    void onComplete();
    void onError();
}
