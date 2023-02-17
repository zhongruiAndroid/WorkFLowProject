package com.github.zr.multi;

import java.util.Map;

public interface WorkCallback {
    void onSuccess(Map<String,Object> map);
    void onError();
}
