package com.example.ping.controller;

import com.example.Ping;
import com.example.Pong;
import com.example.Protocol;
import com.example.ping.core.Uuid;
import com.example.ping.service.PingService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ConditionalOnProperty(name = "ping.jms", havingValue = "true")
public class Jms {

    private static final String LOCK = "jms";

    @Autowired
    private JmsTemplate jms;

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
                .setProtocol(Protocol.JMS);
            pingService.onPing(ping);
            jms.convertAndSend(Ping.QUEUE, ping);
        } finally {
            lock.unlock();
        }
    }

    @JmsListener(destination = Pong.QUEUE)
    public void pong(Pong pong) {
        pingService.onPong(pong);
    }

}
