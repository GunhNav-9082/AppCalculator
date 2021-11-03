package com.example.appandroidcalculator;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    public int add(int a, int b)
    {
        return a + b;
    }

    public int sub(int a, int b)
    {
        return a - b;
    }

    public int multi(int a, int b) { return a * b; }

    public int div(int a, int b)
    {
        return a / b;
    }

    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        };
    }
}
