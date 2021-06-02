package com.github.zr;

import android.os.Handler;
import android.os.Looper;

public class WorkFlow {
    private static Handler handler=new Handler(Looper.getMainLooper());
    public static Handler getHandler(){
        return handler;
    }
    public static Flow get(){
        return new Flow(false);
    }
    public static Flow get(boolean rightAwayNotifyError){
        return new Flow(rightAwayNotifyError);
    }
}
