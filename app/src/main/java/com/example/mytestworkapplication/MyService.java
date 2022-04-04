package com.example.mytestworkapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public static final String BROADCAST = "com.max.action.BROADCAST";
    public static final String BROADCAST_FLAG = "flag";
    public ThreadCounter threadCounter;
    Context serviceContext;
    public MyService() {
        serviceContext=this;
    }

    private void saveStartingTime() {
        Intent intent = new Intent(BROADCAST);
        intent.putExtra(BROADCAST_FLAG, 1);
        serviceContext.sendBroadcast(intent);
        Log.d("TAG1","MyService saveStartingTime worked");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        saveStartingTime();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int digit = MainActivity.digit;
        //ниже проверка - уже запущен или нет этот поток
        if(startId==1){
            threadCounter = new ThreadCounter(serviceContext, digit);
            threadCounter.start();
        }
        else{
            // Log.d("TAG1", "MyService ПОПЫТКА ЗАПУСКА ТРЕДА ПРИ УЖЕ РАБОТАЮЩЕМ ТРЕДЕ " + getDate());
        }
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        threadCounter.stopIt();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}