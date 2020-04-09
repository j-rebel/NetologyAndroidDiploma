package com.example.appdiploma;

import android.app.Application;
import android.graphics.drawable.Drawable;

import java.time.LocalDateTime;

public class App extends Application {

    private static App instance;
    private int DIALOG_DATE;
    private LocalDateTime now = LocalDateTime.now();
    private Drawable imgEmpty;
    private Drawable imgFilled;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        instance.DIALOG_DATE = 1;
        instance.now = LocalDateTime.now();
        imgEmpty = getDrawable(R.drawable.empty);
        imgFilled = getDrawable(R.drawable.filled);
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

    public Drawable getEmpty() {
        return imgEmpty;
    }

    public Drawable getFilled() {
        return imgFilled;
    }
}
