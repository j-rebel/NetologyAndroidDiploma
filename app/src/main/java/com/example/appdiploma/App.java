package com.example.appdiploma;

import android.app.Application;

import java.time.LocalDateTime;

public class App extends Application {

    private static int DIALOG_DATE;
    private static LocalDateTime now = LocalDateTime.now();

    public static int getDIALOG_DATE() {
        return DIALOG_DATE;
    }

    public static int getYear() {
        return now.getYear();
    }

    public static int getMonth() {
        return now.getMonthValue() - 1;
    }

    public static int getDay() {
        return now.getDayOfMonth();
    }
}
