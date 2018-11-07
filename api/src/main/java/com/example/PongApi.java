package com.example;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PongApi {

    @PostMapping(
        path = "/api/ping",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void ping(@RequestBody Ping ping);

}
