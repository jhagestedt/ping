package com.example.ping.controller;

import com.example.Ping;
import com.example.PingApi;
import com.example.Pong;
import com.example.Protocol;
import com.example.ping.client.PongClient;
import com.example.ping.core.Uuid;
import com.example.ping.service.PingService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ConditionalOnProperty(name = "ping.rest", havingValue = "true")
public class Rest implements PingApi {

    private static final String LOCK = "rest";

    @Autowired
    private PongClient client;

    @Autowired
    private PingService pingService;

    @Autowired
    private HazelcastInstance cluster;

    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    public void ping() {
        ILock lock = cluster.getLock(LOCK);
        if (lock.isLocked()) {
            log.warn("ping() lock for {} is set.", LOCK);
            return;
        }
        lock.lock();
        try {
            Ping ping = new Ping()
                .setUuid(Uuid.generate())
                .setProtocol(Protocol.REST);
            pingService.onPing(ping);
            client.ping(ping);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void pong(Pong pong) {
        pingService.onPong(pong);
    }

}
