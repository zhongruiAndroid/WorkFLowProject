package com.github.zr.single.flow.listener;

import com.github.zr.R;
import com.github.zr.single.Observer;

public interface FlowNextObserver<R> {
    void next(R obj) throws Exception;
}
