package com.github.zr.single.flow.listener;

public interface FlowErrorObserver{
    void onError(Throwable code, Object obj) throws Exception;
}
