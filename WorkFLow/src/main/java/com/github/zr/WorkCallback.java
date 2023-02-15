package com.github.zr;

import java.util.Map;

public interface WorkCallback {
    void onSuccess(Map<String,Object> map);
    void onError();
}
