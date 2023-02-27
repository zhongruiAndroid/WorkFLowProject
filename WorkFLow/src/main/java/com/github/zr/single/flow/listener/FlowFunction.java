package com.github.zr.single.flow.listener;

public interface FlowFunction<T,R> {
    void next(T obj, FlowNextObserver<R> observer) throws Exception;
    default void complete(Object obj, FlowCompleteObserver observer)  {observer.onComplete(obj);}
    default void error(Throwable throwable,Object obj, FlowErrorObserver observer)  {observer.onError(new Exception(),obj);}
}
