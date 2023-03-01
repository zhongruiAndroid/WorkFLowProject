package com.github.zr;

import android.os.Handler;
import android.os.Looper;

import com.github.zr.multi.WorkBean;
import com.github.zr.single.FlowObservable;
import com.github.zr.single.Observable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkFlow {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        return handler;
    }

    public static ExecutorService executors;

    public static ExecutorService getExecutors() {
        if (executors == null) {
            executors = Executors.newCachedThreadPool();
        }
        return executors;
    }

    public static WorkBean get() {
        return new WorkBean(false);
    }

    public static WorkBean get(boolean rightAwayNotifyError) {
        return new WorkBean(rightAwayNotifyError);
    }
    public static <T>FlowObservable<T> createFlow(Observable<T> observable) {
        return new FlowObservable<T>(observable);
    }
}
