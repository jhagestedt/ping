package com.example;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PingApi {

    @PostMapping(
        path = "/api/pong",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void pong(@RequestBody Pong pong);

}
