package com.example.ping.core;

public class Time {

    private Time() {
    }

    public static Long now() {
        return System.currentTimeMillis();
    }

}
