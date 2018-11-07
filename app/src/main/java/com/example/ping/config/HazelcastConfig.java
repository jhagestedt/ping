package com.example.ping.config;

import com.hazelcast.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "ping.hazelcast", havingValue = "true")
public class HazelcastConfig {

    @Bean
    public Config config() {
        return new Config();
    }

}
