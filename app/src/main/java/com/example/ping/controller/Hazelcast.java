package com.example.ping.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.Ping;
import com.example.Pong;
import com.example.Protocol;
import com.example.ping.core.Uuid;
import com.example.ping.service.PingService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ConditionalOnProperty(name = "ping.hazelcast", havingValue = "true")
public class Hazelcast implements MessageListener<Pong> {

    private String key;

    @Autowired
    private HazelcastInstance hazelcast;

    @Autowired
    private PingService pingService;

    @PostConstruct
    public void init() {
        ITopic<Pong> topic = hazelcast.getTopic(Pong.QUEUE);
        key = topic.addMessageListener(this);
    }

    @PreDestroy
    public void destroy() {
        hazelcast.getTopic(Pong.QUEUE).removeMessageListener(key);
    }

    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    public void ping() {
        Ping ping = new Ping()
            .setUuid(Uuid.generate())
            .setProtocol(Protocol.HAZELCAST);
        pingService.onPing(ping);
        hazelcast.getTopic(Ping.QUEUE).publish(ping);
    }

    @Override
    public void onMessage(Message<Pong> message) {
        pingService.onPong(message.getMessageObject());
    }

}
