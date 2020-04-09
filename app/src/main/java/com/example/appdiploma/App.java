package com.example.appdiploma;

import android.app.Application;

import java.time.LocalDateTime;

public class App extends Application {

    private static App instance;
    private int DIALOG_DATE;
    private LocalDateTime now = LocalDateTime.now();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        instance.DIALOG_DATE = 1;
        instance.now = LocalDateTime.now();
    }

    public static App getInstance() {
        return instance;
    }

    public int getDIALOG_DATE() {
        return DIALOG_DATE;
    }

    public int getYear() {
        return now.getYear();
    }

    public int getMonth() {
        return now.getMonthValue() - 1;
    }

    public int getDay() {
        return now.getDayOfMonth();
    }
}
