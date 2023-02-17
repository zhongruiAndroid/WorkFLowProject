package com.github.zr.single.filter;


import com.github.zr.single.BaseObservable;
import com.github.zr.single.Observer;
import com.github.zr.single.WorkScheduler;
import com.github.zr.single.filter.listener.FilterFunction;

public class FilterObservable<T> extends BaseObservable {

    private FilterFunction function;

    public FilterObservable(FilterFunction<? super T> function) {
        this.function = function;
    }

    public FilterObservable(WorkScheduler workScheduler, FilterFunction<? super T> function) {
        this.setWorkScheduler(workScheduler);
        this.function = function;
    }

    public Observer call(final Observer<? super T> subscriber) {
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
                                if (function.call(obj)) {
                                    if (subscriber != null) {
                                        postMain(new Runnable() {
                                            @Override
                                            public void run() {
                                                subscriber.onNext(obj);
                                            }
                                        });
                                    }
                                }
                            } catch (final Exception e) {
                                e.printStackTrace();
                                postMain(new Runnable() {
                                    @Override
                                    public void run() {
                                        onError(0, e.getMessage());
                                    }
                                });
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
            public void onError(final int code, final String msg) {
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
