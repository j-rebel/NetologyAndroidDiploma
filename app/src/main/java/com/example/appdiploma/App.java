package com.example.appdiploma;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.appdiploma.activities.AddNoteActivity;
import com.example.appdiploma.activities.LoginActivity;
import com.example.appdiploma.activities.NoteListActivity;
import com.example.appdiploma.activities.PinEditActivity;
import com.example.appdiploma.activities.UpdateNoteActivity;
import com.example.appdiploma.keystore.Keystore;
import com.example.appdiploma.keystore.SimpleKeystore;
import com.example.appdiploma.roomedRepository.DatabaseClient;
import com.example.appdiploma.roomedRepository.NoteDAO;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class App extends Application {

    private static App instance;
    private int DIALOG_DATE;
    private LocalDateTime now = LocalDateTime.now();
    private int year;
    private int month;
    private int day;
    private Drawable imgEmpty;
    private Drawable imgFilled;
    private Context context;
    private SharedPreferences pinPref;
    private SharedPreferences firstRun;
    private Keystore keystore;
    private NoteDAO noteDAO;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        instance.DIALOG_DATE = 1;
        instance.now = LocalDateTime.now();
        year = now.getYear();
        month = now.getMonthValue() - 1;
        day = now.getDayOfMonth();
        imgEmpty = getDrawable(R.drawable.empty_48);
        imgFilled = getDrawable(R.drawable.filled_48);
        pinPref = context.getSharedPreferences(
                getString(R.string.pin_pref), Context.MODE_PRIVATE);
        firstRun = context.getSharedPreferences(
                getString(R.string.first_run_pref), Context.MODE_PRIVATE);
        keystore = new SimpleKeystore(pinPref, getString(R.string.pin_pref));
        noteDAO = DatabaseClient
                .getInstance(getApplicationContext())
                .getAppDatabase()
                .noteDAO();

        final Map<Type, Injector> injectors = new HashMap<>();
        injectors.put(LoginActivity.class, new Injector<LoginActivity>() {
            @Override
            public void inject(LoginActivity component) {
                component.setKeystore(keystore);
                component.setEmpty(imgEmpty);
                component.setFilled(imgFilled);
                component.setFirstRun(firstRun);
            }
        });
        injectors.put(PinEditActivity.class, new Injector<PinEditActivity>() {
            @Override
            public void inject(PinEditActivity component) {
                component.setKeystore(keystore);
                component.setFirstRun(firstRun);
                component.setContext(context);
            }
        });
        injectors.put(NoteListActivity.class, new Injector<NoteListActivity>() {
            @Override
            public void inject(NoteListActivity component) {
                component.setNoteDAO(noteDAO);
            }
        });
        injectors.put(AddNoteActivity.class, new Injector<AddNoteActivity>() {
            @Override
            public void inject(AddNoteActivity component) {
                component.setNoteDAO(noteDAO);
                component.setDIALOG_DATE(DIALOG_DATE);
                component.setMyYear(year);
                component.setMyMonth(month);
                component.setMyDay(day);
                component.setContext(context);
            }
        });
        injectors.put(UpdateNoteActivity.class, new Injector<UpdateNoteActivity>() {
            @Override
            public void inject(UpdateNoteActivity component) {
                component.setNoteDAO(noteDAO);
                component.setDIALOG_DATE(DIALOG_DATE);
                component.setMyYear(year);
                component.setMyMonth(month);
                component.setMyDay(day);
                component.setContext(context);
            }
        });

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Injector injector = injectors.get(activity.getClass());
                if (injector == null) return;
                //noinspection unchecked
                injector.inject(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                // Do nothing
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                // Do nothing
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                // Do nothing
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                // Do nothing
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                // Do nothing
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                // Do nothing
            }
        });
    }

}