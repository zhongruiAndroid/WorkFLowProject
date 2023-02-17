package com.github.zr.single;

import com.github.zr.single.filter.FilterObservable;
import com.github.zr.single.filter.listener.FilterFunction;
import com.github.zr.single.map.MapObservable;
import com.github.zr.single.map.listener.MapFunction;

public class WorkObservable<T> extends BaseObservable {
    private Observable<T> observable;

    public WorkObservable(Observable<T> observable) {
        this.observable = observable;
    }

    public WorkObservable(WorkScheduler workScheduler, Observable<T> observable) {
        this.setWorkScheduler(workScheduler);
        this.observable = observable;
    }


    public static <T> WorkObservable<T> create(Observable<T> observable) {
        return create(WorkScheduler.IO, observable);
    }

    public static <T> WorkObservable<T> create(WorkScheduler workScheduler, Observable<T> observable) {
        WorkObservable workObservable = new WorkObservable(observable);
        workObservable.setWorkScheduler(workScheduler);
        return workObservable;
    }

    public <R> WorkObservable<R> map(final MapFunction<? super T, ? extends R> function) {
        return map(WorkScheduler.IO, function);
    }

    public <R> WorkObservable<R> map(WorkScheduler workScheduler, final MapFunction<? super T, ? extends R> function) {
        final MapObservable<T, R> map = new MapObservable<T, R>(workScheduler, function);
        return new WorkObservable(new Observable<R>() {
            @Override
            public void subscribe(Observer<? super R> observer) throws Exception {
                if (observable == null) {
                    return;
                }
                observable.subscribe(map.call(observer));
            }
        });
    }

    public <T> WorkObservable<T> filter(final FilterFunction<? super T> function) {
        return filter(WorkScheduler.IO, function);
    }

    public <T> WorkObservable<T> filter(WorkScheduler workScheduler, final FilterFunction<? super T> function) {
        final FilterObservable<T> filter = new FilterObservable<T>(workScheduler, function);
        return new WorkObservable(new Observable<T>() {
            @Override
            public void subscribe(final Observer<? super T> observer) throws Exception {
                if (observable == null) {
                    return;
                }
                execute(WorkObservable.this.getScheduler(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            observable.subscribe(filter.call(observer));
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (observer != null) {
                                observer.onError(0, e.getMessage());
                            }
                        }
                    }
                });
            }
        });
    }

    public void subscribe(final Observer<? super T> observer) {
        if (observable == null) {
            return;
        }
        execute(this.getScheduler(), new Runnable() {
            @Override
            public void run() {
                try {
                    observable.subscribe(new Observer<T>() {
                        boolean done;
                        @Override
                        public void onNext(final T obj) {
                            if (done) {
                                return;
                            }
                            postMain(new Runnable() {
                                @Override
                                public void run() {
                                    observer.onNext(obj);
                                }
                            });
                        }

                        @Override
                        public void onComplete(final Object obj) {
                            if (done) {
                                return;
                            }
                            done = true;
                            postMain(new Runnable() {
                                @Override
                                public void run() {
                                    observer.onComplete(obj);
                                }
                            });
                        }

                        @Override
                        public void onError(final int code, final String msg) {
                            if (done) {
                                return;
                            }
                            done = true;
                            postMain(new Runnable() {
                                @Override
                                public void run() {
                                    observer.onError(code, msg);
                                }
                            });

                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    if (observer != null) {
                        postMain(new Runnable() {
                            @Override
                            public void run() {
                                observer.onError(0, e.getMessage());
                            }
                        });
                    }
                }
            }
        });


    }

}
