package com.test.workflowproject;

import com.github.zr.multi.WorkCallback;
import com.github.zr.WorkFlow;
import com.github.zr.multi.WorkListener;
import com.github.zr.multi.WorkNotify;
import com.github.zr.single.Observable;
import com.github.zr.single.Observer;
import com.github.zr.single.flow.listener.FlowFunction;
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
        }).flow(new FlowFunction<String, Integer>() {
            @Override
            public void next(String obj, Observer<Integer> observer) throws Exception {

            }
        }).flow(new FlowFunction<Integer, Integer>() {
            @Override
            public void next(Integer obj, Observer<Integer> observer) throws Exception {

            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onNext(Integer obj) {

            }

            @Override
            public void onComplete(Object obj) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        });
    }
}
