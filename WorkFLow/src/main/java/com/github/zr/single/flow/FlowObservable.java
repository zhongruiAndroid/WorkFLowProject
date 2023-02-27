package com.github.zr.single.flow;

import com.github.zr.single.Observable;
import com.github.zr.single.Observer;
import com.github.zr.single.flow.listener.FlowFunction;

public class FlowObservable<T>  {
    private Observable<T> observable;

    public FlowObservable(Observable<T> observable) {
        this.observable = observable;
    }
    public static <T> FlowObservable<T> create( Observable<T> observable) {
        FlowObservable workObservable = new FlowObservable(observable);
        return workObservable;
    }

    public <R> FlowObservable<R> flow( final FlowFunction<? super T,? extends R> function) {
        final FlowHelper<T,R> flowHelper = new FlowHelper<T,R>( function);
        return new FlowObservable(new Observable<T>() {
            @Override
            public void subscribe(final Observer<? super T> observer) throws Exception {
                if (observable == null) {
                    return;
                }
                try {
                    observable.subscribe(flowHelper.call(observer));
                } catch (Exception e) {
                    e.printStackTrace();
                    if (observer != null) {
                        observer.onError(e, e.getMessage());
                    }
                }
            }
        });
    }

    public void subscribe(final Observer<? super T> observer) {
        if (observable == null) {
            return;
        }
        try {
            observable.subscribe(new Observer<T>() {
                boolean done;
                @Override
                public void onNext(final T obj) {
                    if (done) {
                        return;
                    }
                    observer.onNext(obj);
                }
                @Override
                public void onComplete(final Object obj) {
                    if (done) {
                        return;
                    }
                    done = true;
                    observer.onComplete(obj);
                }
                @Override
                public void onError(final Throwable throwable, final Object msg) {
                    if (done) {
                        return;
                    }
                    done = true;
                    observer.onError(throwable, msg);
                }
            });
        } catch (final Exception e) {
            e.printStackTrace();
            if (observer != null) {
                observer.onError(e, e.getMessage());
            }
        }


    }

}
