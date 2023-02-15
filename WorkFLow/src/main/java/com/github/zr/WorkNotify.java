package com.github.zr;

public interface WorkNotify {
    void onPass();
    void onPass(Object obj);
    void onPass(String tag,Object obj);
    void onError();
}
