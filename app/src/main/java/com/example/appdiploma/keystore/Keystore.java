package com.example.appdiploma.keystore;

public interface Keystore {
    //boolean hasPin();
    boolean checkPin(String pin);
    void saveNew(String pin);
}
