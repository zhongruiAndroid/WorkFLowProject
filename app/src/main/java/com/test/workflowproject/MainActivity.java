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

import org.json.JSONArray;

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
                    public void onSuccess() {
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
            }
        });
    }


}
