package com.github.zr;

import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Flow {
    private int count = 0;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private AtomicInteger errorInteger = new AtomicInteger();
    private List<WorkListener> workListenerList = new ArrayList<>();
    private Map<String,Object> dataMap = new HashMap<>();

    private AtomicBoolean rightAwayNotifyError;

    public Flow() {
        this(false);
    }

    public Flow(boolean rightAwayNotifyError) {
        this.rightAwayNotifyError = new AtomicBoolean(rightAwayNotifyError);
    }
    public void reset(){
        count=0;
        atomicInteger.set(0);
        errorInteger.set(0);
        workListenerList.clear();
        dataMap.clear();
    }
    public Flow addWork(final WorkListener workListener) {
        if (workListener == null) {
            return this;
        }
        count += 1;
        workListener.index = workListenerList.size();
        workListenerList.add(workListener);
        return this;
    }

    public void start() {
        start(null);
    }

    public void start(final WorkCallback callback) {
        if (workListenerList == null) {
            return;
        }
        for (int i = 0; i < workListenerList.size(); i++) {
            WorkListener item=workListenerList.get(i);
            if (item == null) {
                continue;
            }
            final int finalI = i;
            item.doWork(new WorkNotify() {
                @Override
                public void onPass() {
                    dealPass(callback);
                }
                @Override
                public void onPass(String tag,Object obj) {
                    if(tag!=null){
                        dataMap.put(tag,obj);
                    }
                    dealPass(callback);
                }
                @Override
                public void onPass(Object obj) {
                    dataMap.put(finalI+"",obj);
                    dealPass(callback);
                }
                @Override
                public void onError() {
                    if (rightAwayNotifyError.get()) {
                        errorNotify(callback);
                        return;
                    }
                    int errorCount = errorInteger.addAndGet(1);
                    if (errorCount + atomicInteger.get() >= count) {
                        errorNotify(callback);
                    }
                }
            });
        }
    }

    private void dealPass(final WorkCallback callback) {
        if (rightAwayNotifyError.get()) {
            return;
        }
        int andGet = atomicInteger.addAndGet(1);
        if (count == andGet) {
            if (callback != null) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    callback.onSuccess(dataMap);
                } else {
                    WorkFlow.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(dataMap);
                        }
                    });
                }
            }
        } else if (andGet + errorInteger.get() >= count) {
            errorNotify(callback);
        }
    }

    private void errorNotify(final WorkCallback callback) {
        if (callback != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                callback.onError();
            } else {
                WorkFlow.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        }
    }
}
