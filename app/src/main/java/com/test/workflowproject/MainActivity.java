package com.test.workflowproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.github.zr.WorkFlow;
import com.github.zr.WorkInterceptor;
import com.github.zr.WorkListener;
import com.github.zr.WorkNotify;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] supportedAbis = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            supportedAbis = Build.SUPPORTED_ABIS;
        }
        for (String i:supportedAbis){
            Log.i("=====","====="+i);
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
                .setWorkInterceptor(new WorkInterceptor() {
                    @Override
                    public void interceptor(WorkListener workListener) {

                    }
                })
                .start(null);

        View btTest = findViewById(R.id.btTest);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


}
