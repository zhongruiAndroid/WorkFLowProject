package com.test.workflowproject.test;


public class MapObservable {
    private Func func;
    public MapObservable(Func func) {
        this.func=func;
    }
    public Observer call(final Observer subscriber){
        return new Observer(){
            @Override
            public void onNext(Object o) {
                if (subscriber != null) {
                    subscriber.onNext(func==null?o:func.call(o));
                }
            }
            @Override
            public void onComplete() {
                if (subscriber != null) {
                    subscriber.onComplete( );
                }
            }
            @Override
            public void onError() {
                if (subscriber != null) {
                    subscriber.onError( );
                }
            }
        };
    }
}
