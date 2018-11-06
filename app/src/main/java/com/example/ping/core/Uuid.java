package com.example.ping.core;

import java.util.UUID;

public class Uuid {

    private Uuid() {
    }

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
