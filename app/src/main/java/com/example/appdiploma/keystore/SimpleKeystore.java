package com.example.appdiploma.keystore;

import android.content.SharedPreferences;

public class SimpleKeystore implements Keystore {

    private SharedPreferences preferences;

    private String PIN;

    public SimpleKeystore(SharedPreferences preferences, String PIN) {
        this.preferences = preferences;
        this.PIN = PIN;
    }

    @Override
    public boolean checkPin(String pin) {
        return pin.equals(preferences.getString(PIN, null));
    }

    @Override
    public void saveNew(String pin) {
        preferences.edit().putString(PIN, pin).apply();
    }
}
