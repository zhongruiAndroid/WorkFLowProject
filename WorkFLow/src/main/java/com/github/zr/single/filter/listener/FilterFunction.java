package com.github.zr.single.filter.listener;

public interface FilterFunction<T> {
    boolean call(T obj) throws Exception;
}
