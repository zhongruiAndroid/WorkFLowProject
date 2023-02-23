package com.github.zr.single.map;


import com.github.zr.single.BaseObservable;
import com.github.zr.single.Observer;
import com.github.zr.single.WorkScheduler;
import com.github.zr.single.map.listener.MapFunction;

public class MapObservable<T, R> extends BaseObservable {
    private MapFunction function;

    public MapObservable(MapFunction<? super T, ? extends R> function) {
        this.function = function;
    }

    public MapObservable(WorkScheduler workScheduler, MapFunction<? super T, ? extends R> function) {
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
                            final R call;
                            try {
                                call = (R) function.call(obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                                onError(e, e.getMessage());
                                return;
                            }
                            if (subscriber != null) {
                                postMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        subscriber.onNext(call);
                                    }
                                });
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
            public void onError(final Throwable code,final Object msg) {
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
