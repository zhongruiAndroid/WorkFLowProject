package com.github.zr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Flow {
    private int count=0;
    private AtomicInteger atomicInteger=new AtomicInteger();
    private List<WorkListener> workListenerList = new ArrayList<>();
    private WorkInterceptor workInterceptor;
    private WorkCallback workCallback;

    public Flow addWork(WorkListener workListener) {
        if (workListener == null) {
            return this;
        }
        count+=1;
        workListener.index=workListenerList.size();
        WorkListener workListener1 = new WorkListener() {
            @Override
            public void doWork(WorkNotify notify) {

            }
        };
        workListener1.doWork(new WorkNotify() {
            @Override
            public void onPass() {

            }
            @Override
            public void onError() {

            }
        });
        workListenerList.add(workListener1);
        return this;
    }
    public Flow setWorkInterceptor(WorkInterceptor interceptor) {
        workInterceptor=interceptor;
        return this;
    }
    public void start(WorkCallback callback) {
        workCallback=callback;
    }
}
