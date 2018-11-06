package com.example.ping;

import java.io.Serializable;

import com.example.protocol.Protocol;
import lombok.Data;

@Data
public class Ping implements Serializable {

    public static final String QUEUE = "ping";

    private String uuid;
    private Protocol protocol;

    public Ping setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Ping setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }
}
