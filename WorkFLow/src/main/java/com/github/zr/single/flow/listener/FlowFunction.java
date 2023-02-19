package com.github.zr.single.flow.listener;

import com.github.zr.single.Observer;

public interface FlowFunction<T,R> {
    void next(T obj, Observer<R> observer) throws Exception;
}
