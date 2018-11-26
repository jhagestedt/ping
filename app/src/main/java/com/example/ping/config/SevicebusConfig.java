package com.example.ping.config;

import com.example.Ping;
import com.example.Pong;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "azure.servicebus")
@ConditionalOnProperty(name = "ping.servicebus", havingValue = "true")
public class SevicebusConfig {

    private String connection;

    @Bean
    QueueClient pingQueue() {
        QueueClient client = null;
        try {
            client = new QueueClient(new ConnectionStringBuilder(connection, Ping.QUEUE), ReceiveMode.PEEKLOCK);
        } catch (ServiceBusException e) {
            log.error("queue() error at ping setup", e);
        } catch (InterruptedException e) {
            log.error("queue() general error at ping setup", e);
        }
        return client;
    }

    @Bean
    QueueClient pongQueue() {
        QueueClient client = null;
        try {
            client = new QueueClient(new ConnectionStringBuilder(connection, Pong.QUEUE), ReceiveMode.PEEKLOCK);
        } catch (ServiceBusException e) {
            log.error("queue() error at pong setup", e);
        } catch (InterruptedException e) {
            log.error("queue() general error at pong setup", e);
        }
        return client;
    }
}
