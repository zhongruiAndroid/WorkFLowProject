package com.github.zr.single.flow;


import com.github.zr.single.BaseObservable;
import com.github.zr.single.Observer;
import com.github.zr.single.WorkScheduler;
import com.github.zr.single.flow.listener.FlowFunction;
import com.github.zr.single.map.listener.MapFunction;

public class FlowObservable<T,R> extends BaseObservable {
    private FlowFunction function;

    public FlowObservable(FlowFunction<? super T,? extends R> function) {
        this.function = function;
    }

    public FlowObservable(WorkScheduler workScheduler, FlowFunction<? super T,? extends R> function) {
        this.setWorkScheduler(workScheduler);
        this.function = function;
    }

    public <R> Observer call(final Observer<? super R> subscriber) {
        return new Observer<T>() {
            boolean done;

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
                               function.next(obj, new Observer<R>() {
                                    @Override
                                    public void onNext(final R obj) {
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

                                    @Override
                                    public void onComplete(final Object obj) {
                                        if (done) {
                                            return;
                                        }
                                        done=true;
                                        if (subscriber != null) {
                                            postMain(new Runnable() {
                                                @Override
                                                public void run() {
                                                    subscriber.onComplete(obj);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onError(final int code,final String msg) {
                                        if (done) {
                                            return;
                                        }
                                        done=true;
                                        if (subscriber != null) {
                                            postMain(new Runnable() {
                                                @Override
                                                public void run() {
                                                    subscriber.onError(code,msg);
                                                }
                                            });
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                onError(0, e.getMessage());
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
                if (subscriber != null) {
                    postMain(new Runnable() {
                        @Override
                        public void run() {
                            subscriber.onComplete(obj);
                        }
                    });
                }
            }

            @Override
            public void onError(final int code,final String msg) {
                if (done) {
                    return;
                }
                done = true;
                if (subscriber != null) {
                    postMain(new Runnable() {
                        @Override
                        public void run() {
                            subscriber.onError(code, msg);
                        }
                    });
                }
            }
        };
    }
}
