package com.github.zr;

import android.os.Handler;
import android.os.Looper;

import com.github.zr.multi.Flow;
import com.github.zr.single.WorkObservable;
import com.github.zr.single.Observable;
import com.github.zr.single.WorkScheduler;

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

    public static Flow get() {
        return new Flow(false);
    }

    public static Flow get(boolean rightAwayNotifyError) {
        return new Flow(rightAwayNotifyError);
    }

    public static <T> WorkObservable<T> create(Observable<T> observable) {
        return new WorkObservable<T>(observable);
    }
    public static <T> WorkObservable<T> create(WorkScheduler scheduler,Observable<T> observable) {
        return new WorkObservable<T>(scheduler,observable);
    }
}
