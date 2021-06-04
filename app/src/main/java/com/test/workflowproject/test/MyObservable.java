package com.test.workflowproject.test;

public class MyObservable<T> {
    private Observable<T> observable;

    public MyObservable(Observable<T> observable) {
        this.observable = observable;
    }

    public static <T> MyObservable<T> create(Observable<T> observable) {
        return new MyObservable(observable);
    }

    public MyObservable map(Func func) {
        final MapObservable mapObservable = new MapObservable(func);
        return new MyObservable(new Observable() {
            @Override
            public void subscribe(Observer observer) {
                observable.subscribe(mapObservable.call(observer));
            }
        });
    }
    public MyObservable filter(Func<T,Boolean> func){
        final FilterObservable<T> tFilterObservable = new FilterObservable<T>(func);
        return new MyObservable(new Observable() {
            @Override
            public void subscribe(Observer observer) {
                observable.subscribe(tFilterObservable.call(observer));
            }
        });
    }
    public void subscribe(Observer<? super T> observer) {
        observable.subscribe(observer);
    }
}
