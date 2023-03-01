package com.test.workflowproject;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.zr.WorkFlow;
import com.github.zr.multi.WorkBean;
import com.github.zr.multi.WorkListener;
import com.github.zr.multi.WorkNotify;
import com.github.zr.single.FlowFunction;
import com.test.workflowproject.test.Func;
import com.test.workflowproject.test.MyObservable;
import com.test.workflowproject.test.Observable;
import com.test.workflowproject.test.Observer;

import org.json.JSONArray;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                });
//                .start();
        WorkBean workBean = WorkFlow.get();
//        flow.addWork()
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
                });
//                .start(new WorkCallback() {
//                    @Override
//                    public void onSuccess(Map<String, Object> map) {
//                        Log.i("=====", "===onSuccess==");
//                    }
//
//                    @Override
//                    public void onError() {
//                        Log.i("=====", "===onError==");
//                    }
//                });

        View btTestRx = findViewById(R.id.btTestRx);
        btTestRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test33();
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

    public void test33() {
        rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i("=====",(Looper.getMainLooper()==Looper.myLooper())+"==call===");
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
//                subscriber.onError(new Exception("test"));
                subscriber.onCompleted();
            }
        }).observeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                Log.i("=====",(Looper.getMainLooper()==Looper.myLooper())+"==map1==="+s);

                return s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, String>() {
            @Override
            public String call(String integer) {
                Log.i("=====",(Looper.getMainLooper()==Looper.myLooper())+"==map2==="+integer);
                return integer+"";
            }
        }).observeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                Log.i("=====",(Looper.getMainLooper()==Looper.myLooper())+"==map3==="+s);
                return s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i("=====", (Looper.getMainLooper()==Looper.myLooper())+"===onCompleted==");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("=====", (Looper.getMainLooper()==Looper.myLooper())+"===onError==" + e.getMessage());
            }

            @Override
            public void onNext(String integer) {
                Log.i("=====", (Looper.getMainLooper()==Looper.myLooper())+"===onNext==" + integer);
            }
        });
    }

    public void test() {
        WorkFlow.createFlow(new com.github.zr.single.Observable<String>() {
            @Override
            public void subscribe(com.github.zr.single.Observer<? super String> observer) {
                observer.onSuccess("111");
                observer.onError(new Exception("333"),"333");
            }
        }).addFlow(new FlowFunction<String, Integer>() {
            @Override
            public void onSuccess(String obj, com.github.zr.single.Observer<? super Integer> observer) {
                Log.i("=====","==onSuccess2==="+obj);
                observer.onSuccess(222);
            }
        }).addFlow(new FlowFunction<Integer, String>() {
            @Override
            public void onSuccess(Integer obj, com.github.zr.single.Observer<? super String> observer) {
                Log.i("=====","==onSuccess3==="+obj);
                observer.onSuccess(obj+"%"+obj);
            }
        }).start(new com.github.zr.single.Observer<String>() {
            @Override
            public void onSuccess(String obj) {
                Log.i("=====","==onSuccess4==="+obj);
            }
            @Override
            public void onError(Throwable throwable, Object object) {
                Log.i("=====","==onError4==="+object);

            }
        });
    }

    public void test3() {

    }
}
