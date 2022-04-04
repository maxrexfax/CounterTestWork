package com.example.mytestworkapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

//region Описание тз
/*
Активити (Activity)
1) содержит текстовое поле, которое показывает актуальное время последнего запуска Сервиса (значение хранится в Настройках)
2) содержит текстовое поле, которое всегда показывает актуальное (текущее) значение счетчика (хранится в Настройках)
3) так же содержит 2 кнопки - вкл/выкл, которые соответственно включают или выключают Сервис
4) помимо прочего Сервис должен выключаться, как только закрывается приложение

Сервис (Service)
1) при своем старте запускает Поток и записывает в Настройки текущее время
2) при выключении завершает Поток

Поток (Thread)
1) при запуске получает последнее значение счетчика из Настроек
2) выполняет бесконечный цикл в котором с интервалом 5 секунд добавляет 1 к значению счетчика
3) по завершению работы потока записывать измененное значение счетчика в Настройки

Настройки (SharedPreferences)
1) содержит время последнего запуска Сервиса
2) содержит значение счетчика

Рекомендуется
1) отправлять данные из Сервиса при помощи метода sendBroadcast
2) получать (обновлять) данные в Активити при помощи BroadcastReceiver
3) предоставить исходные коды с помощью github.com

Будет плюсом
1) написать на Kotlin
2) использовать ConstraintLayout
3) использовать Architecture Components
*/
//endregion
public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST = "com.max.action.BROADCAST";
    public static final String BROADCAST_COUNTER ="broad_counter";
    public static final String BROADCAST_FLAG = "flag";
    public static final String APP_PREFERENCES_DIGIT = "myDigit";
    public static final String APP_PREFERENCES_DATE = "myDate";
    public static final String APP_PREFERENCES_BOOL = "myBoolStart";

    public static int digit;
    private BroadcastReceiver broadcastReceiver, broadcastReceiverDate;
    private TextView tvDate, tvDigit;
    public Context context;
    SharedPreferences sPref;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDate = findViewById(R.id.textViewDate);
        tvDigit = findViewById(R.id.textViewDigit);
        context = this;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int flag = intent.getIntExtra(BROADCAST_FLAG, 0);
                switch (flag)
                {
                    case 1:
                        //Log.d("TAG1", "Flag =" + flag + " it is service start");
                        saveTextToSharedPref(APP_PREFERENCES_DATE, getDate());
                        tvDate.setText(getDate());
                        break;
                    case 2:
                        //Log.d("TAG1", "Flag =" + flag + " it common service COUNT");
                        digit = intent.getIntExtra(BROADCAST_COUNTER, 0);
                        tvDigit.setText("Digit " + digit);
                        break;
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);
        sPref = getPreferences(MODE_PRIVATE);
        boolean hasVisited = sPref.getBoolean(APP_PREFERENCES_BOOL, false);
//check first run
        if (!hasVisited) {
            SharedPreferences.Editor e = sPref.edit();
            e.putBoolean(APP_PREFERENCES_BOOL, true);
            e.commit();
            saveTextToSharedPref(APP_PREFERENCES_DATE, "FirstRun");
            saveTextToSharedPref(APP_PREFERENCES_DIGIT, "0");
            Log.d("TAG1", "First run");
        }else{
            Log.d("TAG1", "NOT first run");
        }
        try{
            digit = Integer.parseInt(loadTextFromSharedPref(APP_PREFERENCES_DIGIT));
        }catch (Exception e){
            digit = 0;
        }
        tvDigit.setText("Digit " + digit);
        date = loadTextFromSharedPref(APP_PREFERENCES_DATE);
        tvDate.setText(date);
        Log.d("TAG1", "digit=" + digit+ "  date="+date);
    }

    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.buttonStart:
                i = new Intent(this, MyService.class);
                Toast.makeText(MainActivity.this, "Start service", Toast.LENGTH_SHORT).show();
                startService(i);
                //tvDate.setText("Date of start\n\n" + getDate());
                break;
            case R.id.buttonStop:
                stopService();
                Toast.makeText(MainActivity.this, "Service STOPPED", Toast.LENGTH_SHORT).show();
                saveTextToSharedPref(APP_PREFERENCES_DIGIT, ""+digit);
                break;
        }
    }

    private void stopService(){
        Intent i = new Intent(this, MyService.class);
        stopService(i);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
        unregisterReceiver(broadcastReceiver);
        saveTextToSharedPref(APP_PREFERENCES_DIGIT, ""+digit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void showToast(String str){
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    public void saveTextToSharedPref(String key, String value) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        Log.d("TAG1", "Saved KEY=" + key+ "  VALUE="+value);
        ed.putString(key, value);
        ed.commit();
    }

    String loadTextFromSharedPref(String key) {
        sPref = getPreferences(MODE_PRIVATE);
        String tmp = sPref.getString(key, "");
        Log.d("TAG1", "LOADED for KEY=" + key+ "  VALUE="+tmp);
        return tmp;
    }

    //получение даты и вывод ее в виде строки
    public static String getDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        return dateFormat.format(currentDate);
    }
}