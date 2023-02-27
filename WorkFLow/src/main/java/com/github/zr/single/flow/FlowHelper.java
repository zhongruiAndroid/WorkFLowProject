package com.github.zr.single.flow;


import com.github.zr.single.BaseObservable;
import com.github.zr.single.Observer;
import com.github.zr.single.WorkScheduler;
import com.github.zr.single.flow.listener.FlowCompleteObserver;
import com.github.zr.single.flow.listener.FlowErrorObserver;
import com.github.zr.single.flow.listener.FlowFunction;
import com.github.zr.single.flow.listener.FlowNextObserver;

class FlowHelper<T, R> extends BaseObservable {
    private FlowFunction function;
    private FlowNextObserver<R> observerNext = new FlowNextObserver<R>() {
        @Override
        public void next(final R obj) throws Exception {
            if (subscriber != null) {
                if (done) {
                    return;
                }
                subscriber.onNext(obj);
            }
        }
    };
    private FlowCompleteObserver observerComplete = new FlowCompleteObserver() {
        @Override
        public void onComplete(final Object obj)   {
            if (subscriber != null) {
                if (done) {
                    return;
                }
                done = true;
                subscriber.onComplete(obj);
            }
        }
    };
    private FlowErrorObserver observerError = new FlowErrorObserver() {
        @Override
        public void onError(final Throwable code,final Object obj)  {

            if (subscriber != null) {
                if (done) {
                    return;
                }
                done = true;
                subscriber.onError(code, obj);
            }
        }
    };

    public FlowHelper(FlowFunction<? super T, ? extends R> function) {
        this.function = function;
    }

    public FlowHelper(WorkScheduler workScheduler, FlowFunction<? super T, ? extends R> function) {
        this.setWorkScheduler(workScheduler);
        this.function = function;
    }
    private volatile boolean done;
    private Observer subscriber;
    public <R> Observer call(final Observer<? super R> sub) {
        done=false;
        this.subscriber=sub;
        return new Observer<T>() {
            @Override
            public void onNext(final T obj) {
                if (function != null) {
                    try {
                        function.next(obj,observerNext);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError(e, e.getMessage());
                        return;
                    }
                }
            }

            @Override
            public void onComplete(final Object obj) {
                if (function != null) {
                    function.complete(obj, observerComplete);
                }
            }
            @Override
            public void onError(final Throwable code, final Object obj) {
                if (function != null) {
                    function.error(code, obj,observerError);
                }
            }
        };
    }
}
