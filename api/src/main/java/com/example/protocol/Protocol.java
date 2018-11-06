package com.example.protocol;

public enum Protocol {

    REST(false), JMS(true), HAZELCAST(true);

    private boolean async;

    Protocol(boolean async) {
        this.async = async;
    }

    public boolean isAsync() {
        return async;
    }

}
