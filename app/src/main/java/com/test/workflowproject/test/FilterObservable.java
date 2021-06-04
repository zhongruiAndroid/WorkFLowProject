package com.test.workflowproject.test;


public class FilterObservable<T> {
    private Func<T, Boolean> func;
    public FilterObservable(Func<T, Boolean> func) {
        this.func = func;
    }
    public Observer call(final Observer subscriber) {
        return new Observer<T>() {
            @Override
            public void onNext(T o) {
                if (subscriber != null) {
                    if (func != null) {
                        Boolean call = func.call(o);
                        if (call) {
                            subscriber.onNext(o);
                        }
                    } else {
                        subscriber.onNext(o);
                    }
                }
            }
            @Override
            public void onComplete() {
                if (subscriber != null) {
                    subscriber.onComplete();
                }
            }
            @Override
            public void onError() {
                if (subscriber != null) {
                    subscriber.onError();
                }
            }
        };
    }
}
