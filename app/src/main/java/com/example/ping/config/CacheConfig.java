package com.example.ping.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "cluster.enabled", havingValue = "true")
public class CacheConfig {

    @Autowired
    private HazelcastInstance cluster;

    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(cluster);
    }

}