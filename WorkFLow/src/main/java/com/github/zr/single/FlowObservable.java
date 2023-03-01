package com.github.zr.single;

public class FlowObservable<T> {
    private Observable observable;

    public <T>FlowObservable(Observable<T> observable) {
        this.observable = observable;
    }
    public <R>FlowObservable<R> addFlow(FlowFunction<T,? extends R> flowFunction){
        return new FlowObservable<R>(new Observable<R>() {
            @Override
            public void subscribe(Observer<? super R> observer) {
                if (observable != null) {
                    observable.subscribe(new Observer<T>() {
                        @Override
                        public void onSuccess(T obj) {
                            flowFunction.onSuccess(obj,observer);
                        }
                        @Override
                        public void onError(Throwable throwable, Object object) {
                            flowFunction.onError(throwable,object,observer);
                        }
                    });
                }
            }
        });
        /*return new FlowObservable<T>(new Observable<T>() {
            @Override
            public void subscribe(final Observer<? super R> observer) {
                observable.subscribe(new Observer<T>() {
                    @Override
                    public void onSuccess(T obj) {
                        if (flowFunction != null) {
                            flowFunction.onSuccess(obj,observer);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable, Object object) {
                        if (flowFunction != null) {
                            flowFunction.onError(throwable,object,observer);
                        }
                    }
                });
            }

        });*/
    }
    public void start(Observer<T> observer){
        if (observable != null) {
            observable.subscribe(observer);
        }
    }
}
