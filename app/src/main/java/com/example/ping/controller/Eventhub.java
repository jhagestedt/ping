package com.example.ping.controller;

import com.example.Ping;
import com.example.Pong;
import com.example.Protocol;
import com.example.ping.core.Uuid;
import com.example.ping.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@EnableBinding({
    Eventhub.PingProcessor.class,
    Eventhub.PongProcessor.class
})
public class Eventhub {

    @Autowired
    private PingService pingService;

    @Autowired
    private PingProcessor pingProcessor;

    @Scheduled(initialDelay = 5000, fixedRate = 30000)
    public void ping() {
        Ping ping = new Ping()
            .setUuid(Uuid.generate())
            .setProtocol(Protocol.EVENTHUB);
        pingService.onPing(ping);
        pingProcessor.ping().send(MessageBuilder
            .withPayload(ping)
            .build());
    }

    @StreamListener(PongProcessor.PONG)
    public void pong(Pong pong) {
        pingService.onPong(pong);
    }

    public interface PingProcessor {

        String PING = "ping";

        @Output
        MessageChannel ping();

    }

    public interface PongProcessor {

        String PONG = "pong";

        @Input
        SubscribableChannel pong();

    }
}
