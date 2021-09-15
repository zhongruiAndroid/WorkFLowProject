package com.test.workflowproject;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.zr.WorkCallback;
import com.github.zr.WorkFlow;
import com.github.zr.WorkListener;
import com.github.zr.WorkNotify;
import com.test.workflowproject.test.Func;
import com.test.workflowproject.test.MyObservable;
import com.test.workflowproject.test.Observable;
import com.test.workflowproject.test.Observer;

import org.json.JSONArray;

import java.util.Map;

import rx.Subscriber;
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
                    public void onSuccess(Map<Integer, Object> map) {
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
                MyObservable.create(new Observable<String>() {
                    @Override
                    public void subscribe(Observer observer) {
                        observer.onNext("1");
                        observer.onNext("2");
                        observer.onComplete();
                        observer.onError();
                    }
                }).map(new Func<String,Integer>() {
                    @Override
                    public Integer call(String o) {
                        return "1".equals(o)?10:-9;
                    }
                }).filter(new Func<Integer,Boolean>() {
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

    public void test() {
        rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        }).map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return null;
            }
        }).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return null;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

            }
        });
    }

}
