package com.test.workflowproject;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.zr.multi.WorkCallback;
import com.github.zr.WorkFlow;
import com.github.zr.multi.WorkListener;
import com.github.zr.multi.WorkNotify;
import com.github.zr.single.filter.listener.FilterFunction;
import com.github.zr.single.flow.listener.FlowErrorObserver;
import com.github.zr.single.flow.listener.FlowFunction;
import com.github.zr.single.flow.listener.FlowNextObserver;
import com.github.zr.single.map.listener.MapFunction;
import com.test.workflowproject.test.Func;
import com.test.workflowproject.test.MyObservable;
import com.test.workflowproject.test.Observable;
import com.test.workflowproject.test.Observer;

import org.json.JSONArray;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONArray jsonArray = new JSONArray();
        Log.i("=====", "=====" + jsonArray);
        String[] supportedAbis = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            supportedAbis = Build.SUPPORTED_ABIS;
        }
        for (String i : supportedAbis) {
            Log.i("=====", "=====" + i);
        }
        WorkFlow.get()
                .addWork(new WorkListener() {
                    @Override
                    public void doWork(WorkNotify notify) {
                    }
                })
                .addWork(new WorkListener() {
                    @Override
                    public void doWork(WorkNotify notify) {

                    }
                })
                .start();

        WorkFlow.get(true)
                .addWork(new WorkListener() {
                    @Override
                    public void doWork(final WorkNotify notify) {
                        final int[] a = new int[1];
                        new Thread() {
                            @Override
                            public void run() {
                                while (a[0] != 3) {
                                    a[0] = a[0] + 1;
                                    SystemClock.sleep(1000);
                                }
                                Log.i("=====", "=====WorkListener1");
                                notify.onPass();
                            }
                        }.start();
                    }
                })
                .addWork(new WorkListener() {
                    @Override
                    public void doWork(final WorkNotify notify) {
                        final int[] a = new int[1];
                        new Thread() {
                            @Override
                            public void run() {
                                while (a[0] != 2) {
                                    a[0] = a[0] + 1;
                                    SystemClock.sleep(2000);
                                }
                                Log.i("=====", "=====WorkListener2");
                                notify.onPass();
                            }
                        }.start();
                    }
                })
                .addWork(new WorkListener() {
                    @Override
                    public void doWork(final WorkNotify notify) {
                        final int[] a = new int[1];
                        new Thread() {
                            @Override
                            public void run() {
                                while (a[0] != 1) {
                                    a[0] = a[0] + 1;
                                    SystemClock.sleep(1000);
                                }
                                Log.i("=====", "=====WorkListener3");
                                notify.onError();
                            }
                        }.start();
                    }
                })
                .start(new WorkCallback() {
                    @Override
                    public void onSuccess(Map<String, Object> map) {
                        Log.i("=====", "===onSuccess==");
                    }

                    @Override
                    public void onError() {
                        Log.i("=====", "===onError==");
                    }
                });

        View btTest = findViewById(R.id.btTest);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    test();
                    return;
                }
                MyObservable.create(new Observable<String>() {
                    @Override
                    public void subscribe(Observer observer) {
                        observer.onNext("1");
                        observer.onNext("2");
                        observer.onComplete();
                        observer.onError();
                    }
                }).map(new Func<String, Integer>() {
                    @Override
                    public Integer call(String o) {
                        return "1".equals(o) ? 10 : -9;
                    }
                }).filter(new Func<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer o) {
                        return true;
                    }
                }).subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer s) {
                        Log.i("====", "====onNext:" + s);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("====", "====onComplete:");
                    }

                    @Override
                    public void onError() {
                        Log.i("====", "====onError:");
                    }
                });
            }
        });
    }

    public void test1() {
        rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onError(new Exception("test"));
                subscriber.onCompleted();
            }
        }).map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                if ("2".equals(s)) {
                    return 1 / 0;
                }
                return Integer.parseInt(s);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i("=====", "===onCompleted==");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("=====", "===onError==" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.i("=====", "===onNext==" + integer);
            }
        });
    }

    public void test() {
        WorkFlow.createFlow(new com.github.zr.single.Observable<String>() {
            @Override
            public void subscribe(com.github.zr.single.Observer<? super String> subscriber) throws Exception {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
//                subscriber.onError(new Exception(), "onError2");
//                subscriber.onError(new Exception(), "onError1");
//                subscriber.onComplete("1");
//                subscriber.onComplete("2");
            }
        }).flow(new FlowFunction<String, Integer>() {
            @Override
            public void next(String obj, FlowNextObserver<Integer> observer) throws Exception {
                Log.i("=====","next1:"+obj );
                observer.next(Integer.parseInt(obj+""+obj));
            }
        }).flow(new FlowFunction<Integer, String>() {
            @Override
            public void next(Integer obj, FlowNextObserver<String> observer) throws Exception {
                Log.i("=====","next2:"+obj+"&&");
                observer.next(obj+"&&");
            }
            @Override
            public void error(Throwable throwable, Object obj, FlowErrorObserver observer) throws Exception {
                Log.i("=====","error:"+obj.toString());
                FlowFunction.super.error(throwable, obj, observer);
            }
        }).subscribe(new com.github.zr.single.Observer<String>() {
            @Override
            public void onNext(String obj) {
                Log.i("=====", "==subscribe=onNext==" + obj);
            }
            @Override
            public void onComplete(Object obj) {
                Log.i("=====", "==subscribe=onComplete==" + obj);
            }
            @Override
            public void onError(Throwable throwable, Object obj) {
                Log.i("=====", "==subscribe=onError==" + obj.toString());
            }
        });
    }

    public void testd() {
        WorkFlow.create(new com.github.zr.single.Observable<String>() {
            @Override
            public void subscribe(com.github.zr.single.Observer<? super String> subscriber) throws Exception {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onError(new Exception(), "onError1");
                subscriber.onComplete("1");
//                subscriber.onCompleted();
                subscriber.onError(new Exception(), "onError2");
                subscriber.onComplete("2");
            }
        }).map(new MapFunction<String, String>() {
            @Override
            public String call(String obj) throws Exception {

                return obj + "";
            }
        }).filter(new FilterFunction<String>() {
            @Override
            public boolean call(String obj) throws Exception {
                if (obj.equals("33")) {
                    return true;
                }
                return false;
            }
        }).subscribe(new com.github.zr.single.Observer<String>() {
            @Override
            public void onNext(String obj) {
                Log.i("=====", "==subscribe=onNext==" + obj);
            }

            @Override
            public void onComplete(Object obj) {
                Log.i("=====", "==subscribe=onComplete==" + obj);
            }

            @Override
            public void onError(Throwable throwable, Object obj) {
                Log.i("=====", "==subscribe=onError==" + obj.toString());
            }
        });
    }
}
