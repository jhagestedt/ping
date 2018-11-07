package com.example.ping.controller;

import com.example.Ping;
import com.example.PingApi;
import com.example.Pong;
import com.example.Protocol;
import com.example.ping.client.PongClient;
import com.example.ping.core.Uuid;
import com.example.ping.service.PingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ConditionalOnProperty(name = "ping.rest", havingValue = "true")
public class Rest implements PingApi {

    @Autowired
    private PongClient client;

    @Autowired
    private PingService pingService;

    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    public void ping() {
        Ping ping = new Ping()
            .setUuid(Uuid.generate())
            .setProtocol(Protocol.REST);
        pingService.onPing(ping);
        client.ping(ping);
    }

    @Override
    public void pong(Pong pong) {
        pingService.onPong(pong);
    }

}
