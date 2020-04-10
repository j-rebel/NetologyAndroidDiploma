package com.example.appdiploma;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.example.appdiploma.keystore.Keystore;
import com.example.appdiploma.keystore.SimpleKeystore;
import com.example.appdiploma.roomedRepository.DatabaseClient;
import com.example.appdiploma.roomedRepository.NoteDAO;

import java.time.LocalDateTime;
import java.util.List;

public class App extends Application {

    private static App instance;
    private int DIALOG_DATE;
    private LocalDateTime now = LocalDateTime.now();
    private Drawable imgEmpty;
    private Drawable imgFilled;
    private Context context;
    private SharedPreferences pinPref;
    private Keystore keystore;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        instance.DIALOG_DATE = 1;
        instance.now = LocalDateTime.now();
        imgEmpty = getDrawable(R.drawable.empty_48);
        imgFilled = getDrawable(R.drawable.filled_48);
        context = this;
        pinPref = context.getSharedPreferences(
                getString(R.string.pin_pref), Context.MODE_PRIVATE);
        keystore = new SimpleKeystore();
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

    public Context getContext() {
        return context;
    }

    public String getPinPref() {
        return pinPref.getString(getString(R.string.pin_pref), "");
    }

    public void setPinPref(String newPin) {
        SharedPreferences.Editor editor = pinPref.edit();
        editor.putString(getString(R.string.pin_pref), newPin);
        editor.commit();
    }

    public Keystore getKeystore() {
        return keystore;
    }

    public NoteDAO getNoteList() {
        return DatabaseClient
                .getInstance(getApplicationContext())
                .getAppDatabase()
                .noteDAO();
    }
}
