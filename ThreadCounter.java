package com.test.max.testwork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


class ThreadCounter extends Thread {
    private boolean isStopped;
    public static final String BROADCAST = "com.max.action.BROADCAST";
    public static final String BROADCAST_COUNTER ="broad_counter";
    public static final String BROADCAST_FLAG = "flag";
    private int localCounter;
    Context serviceContext;
    public ThreadCounter(Context serviceContextOut, int dig){
        this.isStopped = false;
        this.serviceContext = serviceContextOut;
        this.localCounter = dig;
        //sendDateToMA();
    }
    @Override
    public void run() {
        while (!isStopped){
            try {
                Thread.sleep(1000);
                if(!isStopped) {
                    localCounter++;
                    //Log.d("TAG1", "ThreadCounter localCounter=" + localCounter);
                    Intent intent = new Intent(BROADCAST);
                    intent.putExtra(BROADCAST_COUNTER, localCounter);
                    intent.putExtra(BROADCAST_FLAG, 2);
                    serviceContext.sendBroadcast(intent);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopIt(){
        this.isStopped = true;
    }

   /* public void sendDateToMA(){
        Intent intent = new Intent(BROADCAST);
        intent.putExtra(BROADCAST_FLAG, 1);
        serviceContext.sendBroadcast(intent);
    }*/
}
