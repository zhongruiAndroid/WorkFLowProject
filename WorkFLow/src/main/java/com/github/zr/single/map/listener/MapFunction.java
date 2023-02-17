package com.github.zr.single.map.listener;

public interface MapFunction<T,R> {
    R call(T obj) throws Exception;
}
