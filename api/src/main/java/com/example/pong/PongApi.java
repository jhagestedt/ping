package com.example.pong;

import com.example.ping.Ping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PongApi {

    @PostMapping(
        path = "/api/pong",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    Pong pong(@RequestBody Ping ping);

}
