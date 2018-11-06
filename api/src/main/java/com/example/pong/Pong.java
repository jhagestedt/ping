package com.example.pong;

import java.io.Serializable;

import com.example.protocol.Protocol;
import lombok.Data;

@Data
public class Pong implements Serializable {

    public static final String QUEUE = "pong";

    private String uuid;
    private Protocol protocol;

    public Pong setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Pong setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }
}
