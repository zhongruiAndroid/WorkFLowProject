package com.github.zr.single.flow;


import com.github.zr.single.BaseObservable;
import com.github.zr.single.Observer;
import com.github.zr.single.WorkScheduler;
import com.github.zr.single.flow.listener.FlowCompleteObserver;
import com.github.zr.single.flow.listener.FlowErrorObserver;
import com.github.zr.single.flow.listener.FlowFunction;
import com.github.zr.single.flow.listener.FlowNextObserver;
import com.github.zr.single.map.listener.MapFunction;

public class FlowObservable<T, R> extends BaseObservable {
    private FlowFunction function;

    private FlowNextObserver<R> observerNext = new FlowNextObserver<R>() {
        @Override
        public void next(final R obj) throws Exception {
            if (done) {
                return;
            }
            if (subscriber != null) {
                postMain(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onNext(obj);
                    }
                });
            }
        }
    };

    private FlowCompleteObserver observerComplete = new FlowCompleteObserver() {
        @Override
        public void onComplete(final Object obj) throws Exception {
            if (done) {
                return;
            }
            done = true;
            if (subscriber != null) {
                postMain(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onComplete(obj);
                    }
                });
            }

        }
    };
    private FlowErrorObserver observerError = new FlowErrorObserver() {
        @Override
        public void onError(final Throwable code,final Object obj) throws Exception {
            if (done) {
                return;
            }
            done = true;
            if (subscriber != null) {
                postMain(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onError(code, obj);
                    }
                });
            }
        }
    };

    public FlowObservable(FlowFunction<? super T, ? extends R> function) {
        this.function = function;
    }

    public FlowObservable(WorkScheduler workScheduler, FlowFunction<? super T, ? extends R> function) {
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
                if (done) {
                    return;
                }
                if (function != null) {
                    execute(getScheduler(), new Runnable() {
                        @Override
                        public void run() {
                            try {
                                function.next(obj,observerNext);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onError(e, e.getMessage());
                                return;
                            }
                        }
                    });
                }
            }

            @Override
            public void onComplete(final Object obj) {
                if (done) {
                    return;
                }
                done = true;
                if (function != null) {
                    postMain(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                function.complete(obj, observerComplete);
                            } catch (Exception e) {
                                onError(e, e.getMessage());
                            }
                        }
                    });
                }
            }
            @Override
            public void onError(final Throwable code, final Object obj) {
                if (done) {
                    return;
                }
                done = true;
                if (function != null) {
                    postMain(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                function.error(obj, observerError);
                            } catch (Exception e) {
                                onError(e, e.getMessage());
                            }
                        }
                    });

                }
            }
        };
    }
}
