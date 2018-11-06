package com.example.ping.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.ping.Ping;
import com.example.ping.PingApi;
import com.example.ping.client.PongClient;
import com.example.ping.core.Time;
import com.example.ping.core.Uuid;
import com.example.ping.repository.PingRepository;
import com.example.ping.repository.entity.PingEntity;
import com.example.pong.Pong;
import com.example.protocol.Protocol;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PingService implements PingApi, MessageListener<Pong> {

    private String key;

    @Autowired
    private PongClient rest;

    @Autowired
    private JmsTemplate jms;

    @Autowired
    private HazelcastInstance hazelcast;

    @Autowired
    private PingRepository repository;

    @PostConstruct
    public void init() {
        ITopic<Pong> topic = hazelcast.getTopic(Pong.QUEUE);
        key = topic.addMessageListener(this);
    }

    @PreDestroy
    public void destroy() {
        hazelcast.getTopic(Pong.QUEUE).removeMessageListener(key);
    }

    @Scheduled(fixedRate = 5000)
    public void ping() {
        for (Protocol protocol : Protocol.values()) {
            ping(protocol);
        }
    }

    public void ping(Protocol protocol) {
        Ping ping = new Ping()
            .setUuid(Uuid.generate())
            .setProtocol(protocol);
        onPing(ping);
        switch (protocol) {
            case REST:
                Pong pong = rest.pong(ping);
                onPong(pong);
                break;
            case JMS:
                jms.convertAndSend(Ping.QUEUE, ping);
                break;
            case HAZELCAST:
                hazelcast.getTopic(Ping.QUEUE).publish(ping);
                break;
        }
    }

    private void onPing(Ping ping) {
        log.info("onPing() {}", ping);
        Long time = Time.now();
        repository.save(new PingEntity()
            .setUuid(ping.getUuid())
            .setProtocol(ping.getProtocol())
            .setRequest(time));
    }

    private void onPong(Pong pong) {
        log.info("onPong() {}", pong);
        Long time = Time.now();
        repository.save(repository.findByUuid(pong.getUuid())
            .setResponse(time));
        onMeasure(pong);
    }

    private void onMeasure(Pong pong) {
        PingEntity entity = repository.findByUuid(pong.getUuid());
        log.info("onMeasure() {} ms via {}",
            entity.getMeasure(),
            entity.getProtocol());
    }

    @JmsListener(destination = Pong.QUEUE)
    public void onMessage(Pong pong) {
        onPong(pong);
    }

    @Override
    public void onMessage(Message<Pong> message) {
        onPong(message.getMessageObject());
    }

}
