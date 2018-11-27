package com.example;

import java.io.Serializable;

import lombok.Data;

@Data
public class Cache implements Serializable {

    private Long number = 0L;

    public Cache increase() {
        number++;
        return this;
    }

}
