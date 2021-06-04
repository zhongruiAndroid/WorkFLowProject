package com.test.workflowproject.test;

public interface Observable<T> {
    void subscribe(Observer<? super T> observer);
}
