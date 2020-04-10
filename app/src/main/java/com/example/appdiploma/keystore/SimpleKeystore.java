package com.example.appdiploma.keystore;

import com.example.appdiploma.App;

public class SimpleKeystore implements Keystore {

    /*@Override
    public boolean hasPin() {
        return !App.getInstance().getPinPref().isEmpty();
    }*/

    @Override
    public boolean checkPin(String pin) {
        return App.getInstance().getPinPref().equals(pin);
    }

    @Override
    public void saveNew(String pin) {
        App.getInstance().setPinPref(pin);
    }
}
