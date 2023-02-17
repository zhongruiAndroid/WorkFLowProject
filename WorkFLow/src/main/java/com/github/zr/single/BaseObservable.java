package com.github.zr.single;

import android.os.Looper;

import com.github.zr.WorkFlow;

public class BaseObservable {
    private WorkScheduler workScheduler=WorkScheduler.IO;

    public void setWorkScheduler(WorkScheduler workScheduler) {
        this.workScheduler = workScheduler;
    }

    public WorkScheduler getScheduler() {
        return workScheduler;
    }


    public void execute(WorkScheduler scheduler, final Runnable runnable) {
        if (scheduler == WorkScheduler.IO) {
            if(Looper.getMainLooper()!=Looper.myLooper()){
                /*如果本来就在子线程*/
                runnable.run();
                return;
            }
            WorkFlow.getExecutors().execute(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            });
        } else {
            postMain(runnable);
        }
    }
    public void postMain(Runnable runnable){
        if(Looper.getMainLooper()==Looper.myLooper()){
            runnable.run();
        }else{
            WorkFlow.getHandler().post(runnable);
        }
    }
}
