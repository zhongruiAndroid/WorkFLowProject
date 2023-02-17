package com.github.zr.single;

public interface Observable<T> {
    void subscribe(Observer<? super T> observer) throws Exception;
}
