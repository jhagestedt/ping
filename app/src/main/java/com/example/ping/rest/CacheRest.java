package com.example.ping.rest;

import com.example.Cache;
import com.example.CacheApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CacheRest implements CacheApi {

    private Cache data = new Cache();

    @Override
    @Cacheable(value = "cache-data")
    public Cache get() {
        return data.increase();
    }

    @Override
    @CachePut(value = "cache-data")
    public Cache update() {
        return data.increase();
    }

    @Override
    @CacheEvict(value = "cache-data")
    public Cache delete() {
        return data;
    }

}
