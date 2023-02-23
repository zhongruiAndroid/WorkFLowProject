package com.github.zr.single.flow.listener;

public interface FlowFunction<T,R> {
    void next(T obj, FlowNextObserver<R> observer) throws Exception;
    default void complete(T obj, FlowCompleteObserver observer) throws Exception{observer.onComplete(obj);}
    default void error(T obj, FlowErrorObserver observer) throws Exception{observer.onError(new Exception(),obj);}
}
