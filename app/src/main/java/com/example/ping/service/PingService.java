package com.example.ping.service;

import java.util.Optional;

import com.example.Ping;
import com.example.Pong;
import com.example.ping.core.Time;
import com.example.ping.repository.PingRepository;
import com.example.ping.repository.entity.PingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PingService {

    @Autowired
    private PingRepository repository;


    public void onPing(Ping ping) {
        log.info("onPing() {}", ping);
        Long time = Time.now();
        repository.save(new PingEntity()
            .setUuid(ping.getUuid())
            .setProtocol(ping.getProtocol())
            .setRequest(time));
    }

    public void onPong(Pong pong) {
        log.info("onPong() {}", pong);
        Long time = Time.now();
        Optional<PingEntity> optional = repository.findByUuid(pong.getUuid());
        if (!optional.isPresent()) {
            log.warn("onPong() could not find ping for {}", pong);
            return;
        }
        PingEntity entity = optional.get();
        repository.save(entity.setResponse(time));
        log.info("onPong() -> {} ms via {}",
            entity.getMeasure(),
            entity.getProtocol());
    }

}
