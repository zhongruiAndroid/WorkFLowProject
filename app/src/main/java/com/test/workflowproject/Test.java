package com.test.workflowproject;

import com.github.zr.WorkCallback;
import com.github.zr.WorkFlow;
import com.github.zr.WorkListener;
import com.github.zr.WorkNotify;

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
        }).start(new WorkCallback() {
            @Override
            public void onSuccess(Map<Integer, Object> map) {
                map.get(1);
            }
            @Override
            public void onError() {

            }
        });
    }
}
