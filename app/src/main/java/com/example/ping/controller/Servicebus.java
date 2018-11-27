package com.example.ping.controller;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.example.Ping;
import com.example.Pong;
import com.example.Protocol;
import com.example.ping.core.Uuid;
import com.example.ping.service.PingService;
import com.google.gson.Gson;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ConditionalOnProperty(name = "ping.servicebus", havingValue = "true")
public class Servicebus implements IMessageHandler {

    private Gson gson = new Gson();

    @Autowired
    private QueueClient pingQueue;

    @Autowired
    private QueueClient pongQueue;

    @Autowired
    private PingService pingService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        try {
            pongQueue.registerMessageHandler(this,
                new MessageHandlerOptions(1, true, Duration.ofMinutes(1)),
                executorService);
        } catch (ServiceBusException e) {
            log.error("init() error at pong init", e);
        } catch (InterruptedException e) {
            log.error("init() general error at pong init", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            pongQueue.close();
        } catch (ServiceBusException e) {
            log.error("destroy() error at pong destroy", e);
        }
        executorService.shutdown();
    }

    @Scheduled(initialDelay = 5000, fixedRate = 30000)
    public void ping() {
        Ping ping = new Ping()
            .setUuid(Uuid.generate())
            .setProtocol(Protocol.SERVICEBUS);

        Message message = new Message();
        message.setBody(gson.toJson(ping).getBytes(StandardCharsets.UTF_8));
        message.setContentType(MediaType.APPLICATION_JSON_VALUE);
        message.setLabel(Ping.QUEUE);
        message.setMessageId(ping.getUuid());
        message.setTimeToLive(Duration.ofMinutes(2));

        pingService.onPing(ping);

        try {
            pingQueue.send(message);
        } catch (ServiceBusException e) {
            log.error("ping() error at ping send", e);
        } catch (InterruptedException e) {
            log.error("ping() general error at ping send", e);
        }
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage in) {
        Pong pong = gson.fromJson(new String(in.getBody(), StandardCharsets.UTF_8), Pong.class);
        pingService.onPong(pong);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void notifyException(Throwable throwable, ExceptionPhase phase) {
        log.error("notifyException() error occurred.", throwable);
    }
}
