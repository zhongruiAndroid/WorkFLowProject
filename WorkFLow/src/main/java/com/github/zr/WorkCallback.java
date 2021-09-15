package com.github.zr;

import java.util.Map;

public interface WorkCallback {
    void onSuccess(Map<Integer,Object> map);
    void onError();
}
