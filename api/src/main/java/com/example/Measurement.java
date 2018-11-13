package com.example;

import lombok.Data;

@Data
public class Measurement {

    private String uuid;
    private Long request;
    private Long response;
    private Long duration;
    private Protocol protocol;

    public Measurement setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Measurement setRequest(Long request) {
        this.request = request;
        return this;
    }

    public Measurement setResponse(Long response) {
        this.response = response;
        return this;
    }

    public Measurement setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public Measurement setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

}
