package com.test.workflowproject;

import com.github.zr.multi.WorkCallback;
import com.github.zr.WorkFlow;
import com.github.zr.multi.WorkListener;
import com.github.zr.multi.WorkNotify;
import com.github.zr.single.Observable;
import com.github.zr.single.Observer;
import com.github.zr.single.WorkScheduler;
import com.github.zr.single.flow.listener.FlowCompleteObserver;
import com.github.zr.single.flow.listener.FlowErrorObserver;
import com.github.zr.single.flow.listener.FlowFunction;
import com.github.zr.single.flow.listener.FlowNextObserver;
import com.github.zr.single.listener.Fun1;

import java.util.Map;

public class Test {
    public static void test(){
        WorkFlow.get().addWork(new WorkListener() {
            @Override
            public void doWork(WorkNotify notify) {
                notify.onPass();
            }
        }).addWork(new WorkListener() {
            @Override
            public void doWork(WorkNotify notify) {
                notify.onError();
            }
        }).addWork(new WorkListener() {
            @Override
            public void doWork(WorkNotify notify) {
            }
        }).start(new WorkCallback() {
            @Override
            public void onSuccess(Map<String, Object> map) {
                map.get(1);
            }
            @Override
            public void onError() {

            }
        });

        WorkFlow.create(new Observable<String>() {
            @Override
            public void subscribe(Observer<? super String> observer) throws Exception {

            }
        }).flow(WorkScheduler.MAIN,new FlowFunction<String, Integer>() {
            @Override
            public void next(String obj, FlowNextObserver<Integer> observer) throws Exception {
                observer.next(1);
            }

            @Override
            public void complete(String obj, FlowCompleteObserver observer) throws Exception {
                observer.onComplete(obj);
            }

            @Override
            public void error(String obj, FlowErrorObserver observer) throws Exception {
                observer.onError(new Exception(),obj);
            }

        }).flow(WorkScheduler.MAIN,new FlowFunction<Integer, String>() {
            @Override
            public void next(Integer obj, FlowNextObserver<String> observer) throws Exception {

            }

        }).subscribe(new Observer<String>() {
            @Override
            public void onNext(String obj) {

            }

            @Override
            public void onComplete(Object obj) {

            }

            @Override
            public void onError(Throwable throwable, Object obj) {

            }

        });
    }
}
