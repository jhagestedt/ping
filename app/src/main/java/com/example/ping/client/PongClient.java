package com.example.ping.client;

import com.example.Ping;
import com.example.PongApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(
    name = "pong",
    fallback = PongClient.Fallback.class
)
public interface PongClient extends PongApi {

    @Slf4j
    @Component
    class Fallback implements PongClient {

        @Override
        public void ping(Ping ping) {
            log.warn("ping() fallback used for {}", ping);
        }
    }

}
