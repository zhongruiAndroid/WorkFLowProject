package com.github.zr;

public abstract class WorkListener {
    public int index;
    public int type=-1;
    public WorkListener() {
    }
    public WorkListener(int type) {
        this.type = type;
    }
    public abstract void doWork(WorkNotify notify);
}
